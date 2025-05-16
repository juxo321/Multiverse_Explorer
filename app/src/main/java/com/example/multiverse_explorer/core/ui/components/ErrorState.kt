package com.example.multiverse_explorer.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.multiverse_explorer.R

@Composable
fun ErrorState(
    modifier: Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.testTag("error_state")
    ) {
        Text(
            text = stringResource(R.string.generic_error),
            fontWeight = FontWeight.Bold,
            color = Color.LightGray,
            modifier = Modifier.size(200.dp)
        )
        Image(
            painter = painterResource(R.drawable.error_image),
            contentDescription = stringResource(R.string.error_image_description),
        )
    }
}