package com.example.retrotrade.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.ui.components.profile.ProfileHeader
import com.example.retrotrade.ui.components.profile.ProfileInfoCard
import com.example.retrotrade.ui.components.profile.ProfileStatsRow
import com.example.retrotrade.ui.navigation.GenericUiState
import com.example.retrotrade.ui.navigation.profile.ProfileUiState

@Composable
@Preview(showBackground = true)
fun ProfileContent(
    modifier: Modifier = Modifier,
    uiState: ProfileUiState = ProfileUiState(),
    dataState: GenericUiState = GenericUiState.Idle,
    onLogout: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // ── Profile header with avatar ──────────────────────
        ProfileHeader(
            fullName = uiState.fullName,
            username = uiState.username,
            memberSince = uiState.memberSince
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (dataState is GenericUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Color.Black
                )
            }
        } else {
            // ── Stats row ───────────────────────────────────────
            ProfileStatsRow(
                soldItems = uiState.soldItems,
                completedTrades = uiState.completedTrades
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Account info card ───────────────────────────────
            ProfileInfoCard(
                fullName = uiState.fullName,
                username = uiState.username,
                email = uiState.email
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // ── Logout button ───────────────────────────────────
        LogoutButton(onLogout = onLogout)

        Spacer(modifier = Modifier.height(24.dp))
    }
}



// ─── Logout Button ──────────────────────────────────────────────────
@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFDEDED),
            contentColor = Color(0xFFE74C3C)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Log Out",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}
