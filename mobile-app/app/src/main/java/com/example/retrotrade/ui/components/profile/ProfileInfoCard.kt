package com.example.retrotrade.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.retrotrade.ui.theme.BackgroundColor
import com.example.retrotrade.ui.theme.RetroTextPrimary
import com.example.retrotrade.ui.theme.RetroTextSecondary

@Composable
@Preview(showBackground = true)
fun ProfileInfoCard(
    fullName: String = "Unknown",
    username: String = "Unknown",
    email: String = "Unknown"
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Account Info",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = RetroTextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoRow(label = "Full Name", value = fullName)

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = BackgroundColor
            )

            InfoRow(label = "Username", value = "@${username}")

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = BackgroundColor
            )

            InfoRow(label = "Email", value = email)
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun InfoRow(
    label: String = "Label",
    value: String = "Value"
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = RetroTextSecondary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = RetroTextPrimary
        )
    }
}