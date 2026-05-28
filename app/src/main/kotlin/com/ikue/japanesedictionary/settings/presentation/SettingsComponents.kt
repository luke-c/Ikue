package com.ikue.japanesedictionary.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun PreferenceCategory(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SwitchPreference(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    summary: String? = null,
) {
    ListItem(
        headlineContent = { Text(text = title) },
        supportingContent = summary?.let { { Text(text = it) } },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = null // Handled by ListItem click
            )
        },
        modifier = modifier.clickable { onCheckedChange(!checked) }
    )
}

@Composable
fun ListPreference(
    title: String,
    summary: String,
    entries: List<String>,
    entryValues: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    ListItem(
        headlineContent = { Text(text = title) },
        supportingContent = { Text(text = summary) },
        modifier = modifier.clickable { showDialog = true }
    )

    if (showDialog) {
        ListPreferenceDialog(
            title = title,
            entries = entries,
            entryValues = entryValues,
            selectedValue = selectedValue,
            onValueChange = {
                onValueChange(it)
                showDialog = false
            },
            onDismissRequest = { showDialog = false }
        )
    }
}

@Composable
fun ListPreferenceDialog(
    title: String,
    entries: List<String>,
    entryValues: List<String>,
    selectedValue: String,
    onValueChange: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = title) },
        text = {
            Column(Modifier.selectableGroup()) {
                entries.forEachIndexed { index, entry ->
                    val value = entryValues[index]
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = (value == selectedValue),
                                onClick = { onValueChange(value) },
                                role = Role.RadioButton
                            )
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (value == selectedValue),
                            onClick = null
                        )
                        Text(
                            text = entry,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}
