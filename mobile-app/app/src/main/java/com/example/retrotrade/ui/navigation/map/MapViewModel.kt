package com.example.retrotrade.ui.navigation.map

import androidx.lifecycle.viewModelScope
import com.example.retrotrade.model.ItemCategory
import com.example.retrotrade.ui.navigation.BaseViewModel
import com.example.retrotrade.ui.navigation.GenericUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import com.example.retrotrade.repository.ItemRepository
import com.example.retrotrade.rest.model.response.LoadItemsResponse
import com.example.retrotrade.ui.navigation.NavEvent
import com.example.retrotrade.ui.navigation.Screen
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class MapViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val itemRepository = ItemRepository()

    // Items after category + radius filtering, before search query is applied
    private var radiusAndCategoryFilteredItems: List<LoadItemsResponse> = emptyList()

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    //Separate loading/error state so the UI can react without inspecting every field of MapUiState
    private val _dataState = MutableStateFlow<GenericUiState>(GenericUiState.Idle)
    val dataState: StateFlow<GenericUiState> = _dataState.asStateFlow()

    // Current device location — null until first fix
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation.asStateFlow()

    // Last camera position the user was at — persisted across navigation
    private val _lastCameraPosition = MutableStateFlow<CameraPosition?>(null)
    val lastCameraPosition: StateFlow<CameraPosition?> = _lastCameraPosition.asStateFlow()

    private val _navEvent = MutableSharedFlow<NavEvent>()
    val navEvent = _navEvent.asSharedFlow()

    init {
        loadItems()
    }

    fun onBack() {
        popBackStack()
    }

    fun startLocationTracking() {
        viewModelScope.launch {
            locationFlow(getApplication())
                .collect { _userLocation.value = it }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applySearchFilter()
    }

    fun onItemCategoryChange(category: ItemCategory) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
    }

    fun onRadiusChange(radiusKm: Float) {
        _uiState.value = _uiState.value.copy(radiusKm = radiusKm)
    }

    fun onFilterWindowClosed() {
        val state = _uiState.value
        val origin = _userLocation.value

        radiusAndCategoryFilteredItems  = state.items.filter { item ->
            val matchesCategory = state.selectedCategory == ItemCategory.ALL ||
                    item.category == state.selectedCategory

            val matchesRadius = origin == null || haversineDistanceKm(
                origin.latitude, origin.longitude,
                item.latitude, item.longitude
            ) <= state.radiusKm

            matchesCategory && matchesRadius
        }

        applySearchFilter()
    }

    private fun applySearchFilter() {
        val query = _uiState.value.searchQuery.trim()

        val result = if (query.isEmpty()) {
            radiusAndCategoryFilteredItems
        } else {
            radiusAndCategoryFilteredItems.filter { item ->
                item.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.update { it.copy(filteredItems = result) }
    }

    fun onClusterClick(items: List<LoadItemsResponse>) {
        _uiState.update { it.copy(selectedClusterItems = items) }
    }

    fun onClusterDismiss() {
        _uiState.update { it.copy(selectedClusterItems = emptyList()) }
    }

    fun onMapItemSelected(itemId: String, currentCameraPosition: CameraPosition) {
        saveCameraPosition(currentCameraPosition)
        navigate(Screen.ItemDetails.createRoute(itemId, source = Screen.Map.route))
    }

    fun saveCameraPosition(position: CameraPosition) {
        _lastCameraPosition.value = position
    }

    private fun loadItems() {
        _dataState.value = GenericUiState.Loading
        viewModelScope.launch {
            itemRepository.loadMapItems()
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(items = items)
                    _dataState.value = GenericUiState.Idle

                    viewModelScope.launch {
                        val origin = _userLocation.filterNotNull().first()
                        val state = _uiState.value

                        radiusAndCategoryFilteredItems  = state.items.filter { item ->
                            val matchesCategory = state.selectedCategory == ItemCategory.ALL ||
                                    item.category == state.selectedCategory

                            val matchesRadius = haversineDistanceKm(
                                origin.latitude, origin.longitude,
                                item.latitude, item.longitude
                            ) <= state.radiusKm

                            matchesCategory && matchesRadius
                        }

                        applySearchFilter()
                    }
                }
                .onFailure {
                    _dataState.value = GenericUiState.Error(it.message ?: "Failed to load items")
                }
        }
    }

    fun resetDataState() {
        _dataState.value = GenericUiState.Idle
    }

    private fun haversineDistanceKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val earthRadiusKm = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadiusKm * c
    }

    @SuppressLint("MissingPermission")
    private fun locationFlow(context: Context): Flow<LatLng> = callbackFlow {
        val client = LocationServices.getFusedLocationProviderClient(context)

        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            3_000L          // update every 3 seconds
        ).setMinUpdateDistanceMeters(5f)   // only if moved ≥ 5 m
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    trySend(LatLng(loc.latitude, loc.longitude))
                }
            }
        }

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { client.removeLocationUpdates(callback) }
    }

    private fun navigate(route: String) {
        viewModelScope.launch {
            _navEvent.emit(NavEvent.Navigate(route))
        }
    }

    private fun popBackStack() {
        viewModelScope.launch {
            _navEvent.emit(NavEvent.PopBackStack)
        }
    }
}