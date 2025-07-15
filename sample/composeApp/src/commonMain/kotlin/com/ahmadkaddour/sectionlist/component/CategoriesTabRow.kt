package com.ahmadkaddour.sectionlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CategoriesTabRow(
    values: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onSelectionChange: (Int) -> Unit
) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.2.dp,
            color = MaterialTheme.colorScheme.outline
        )
        ScrollableTabRow(
            selectedTabIndex = selectedIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Transparent,
            contentColor = Color.Transparent,
            edgePadding = 16.dp,
            divider = { }
        ) {
            values.forEachIndexed { index, value ->
                val isSelected = index == selectedIndex
                Tab(
                    selected = isSelected,
                    selectedContentColor = MaterialTheme.colorScheme.onBackground,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    onClick = {
                        onSelectionChange(index)
                    },
                    text = {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }
    }
}