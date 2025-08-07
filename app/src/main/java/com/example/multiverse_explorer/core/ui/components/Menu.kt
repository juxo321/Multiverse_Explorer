package com.example.multiverse_explorer.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.multiverse_explorer.BuildConfig
import com.example.multiverse_explorer.R


@Composable
fun Menu(
    onClearData: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .size(24.dp)
    ) {
        IconButton(
            onClick = { expanded = !expanded }
        ) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.menu_description)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(R.string.menu_delete_data_label)) },
                leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                onClick = {
                    onClearData()

                }
            )
            if (BuildConfig.DEBUG ){
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.menu_settings_data_label)) },
                    leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                    onClick = {
                        navigateToSettings()
                        expanded = !expanded
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MenuPreview() {
    Menu(
        onClearData = {},
        navigateToSettings = {},
    )
}