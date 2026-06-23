package com.example.retrotrade.ui.components.scan

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sqrt


enum class TiltLevel {
    STABLE,
    ALMOST_STABLE,
    UNSTABLE
}

data class TiltState(
    val angleDegrees: Float = 0f,
    val level: TiltLevel = TiltLevel.UNSTABLE
)

private const val STABLE_THRESHOLD_DEG = 8f
private const val ALMOST_STABLE_THRESHOLD_DEG = 18f

private const val SMOOTHING_ALPHA = 0.15f

@Composable
fun rememberTiltState(): State<TiltState> {
    val context = LocalContext.current
    val tiltState = remember { mutableStateOf(TiltState()) }

    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        var smoothedX = 0f
        var smoothedY = 0f
        var smoothedZ = SensorManager.GRAVITY_EARTH

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]

                smoothedX += SMOOTHING_ALPHA * (x - smoothedX)
                smoothedY += SMOOTHING_ALPHA * (y - smoothedY)
                smoothedZ += SMOOTHING_ALPHA * (z - smoothedZ)

                val magnitude = sqrt(
                    smoothedX * smoothedX + smoothedY * smoothedY + smoothedZ * smoothedZ
                )
                if (magnitude == 0f) return

                // Angle between the device's back (z-axis) and gravity.
                // 0°  = phone lying flat (screen up or down) -> perfectly level
                // 90° = phone held fully upright/vertical
                val cosAngle = (abs(smoothedZ) / magnitude).coerceIn(-1f, 1f)
                val angleDeg = acos(cosAngle) * (180f / PI.toFloat())

                val level = when {
                    angleDeg < STABLE_THRESHOLD_DEG -> TiltLevel.STABLE
                    angleDeg < ALMOST_STABLE_THRESHOLD_DEG -> TiltLevel.ALMOST_STABLE
                    else -> TiltLevel.UNSTABLE
                }

                tiltState.value = TiltState(angleDeg, level)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        if (accelerometer != null) {
            sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_UI)
        }

        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return tiltState
}

/**
 * Small pill-shaped banner showing the current stability level.
 * Drop this anywhere in a Box with Alignment.TopCenter (or similar).
 */
@Composable
fun TiltIndicatorBanner(
    tiltLevel: TiltLevel,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label) = when (tiltLevel) {
        TiltLevel.STABLE -> Triple(
            Color(0xFF2ECC71).copy(alpha = 0.92f),
            Color.White,
            "🟢 Stable - good for capture"
        )
        TiltLevel.ALMOST_STABLE -> Triple(
            Color(0xFFF1C40F).copy(alpha = 0.92f),
            Color.Black,
            "🟡 Almost stable"
        )
        TiltLevel.UNSTABLE -> Triple(
            Color(0xFFE74C3C).copy(alpha = 0.92f),
            Color.White,
            "⚠ Unstable - keep device level"
        )
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}