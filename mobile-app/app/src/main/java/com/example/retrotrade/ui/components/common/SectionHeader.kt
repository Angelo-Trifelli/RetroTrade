package com.example.retrotrade.ui.components.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.retrotrade.ui.theme.RetroTextPrimary

@Composable
@Preview(showBackground = true)
fun SectionHeader(
    title: String = "Title"
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = RetroTextPrimary,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}