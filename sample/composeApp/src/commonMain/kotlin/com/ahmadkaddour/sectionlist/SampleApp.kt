package com.ahmadkaddour.sectionlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ahmadkaddour.sectionlist.component.AppTopBar
import com.ahmadkaddour.sectionlist.component.CategoriesTabRow
import com.ahmadkaddour.sectionlist.component.CategoryItemView
import com.ahmadkaddour.sectionlist.component.SectionHeader
import com.ahmadkaddour.sectionlist.model.Category
import com.ahmadkaddour.sectionlist.model.CategoryFactory
import com.ahmadkaddour.sectionlist.ui.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun SampleApp() {
    AppTheme {
        val coroutineScope = rememberCoroutineScope()
        val categories = CategoryFactory.categories
        val sectionListState =
            rememberSectionListState(sectionsSize = categories.map { it.items.size + 1 }) // +1 for a header in each section.
        val selectedSection =
            sectionListState.currentSectionIndex.collectAsStateWithLifecycle(0)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets.safeDrawing,
            topBar = {
                AppTopBar()
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                CategoriesTabRow(
                    values = categories.map { it.title },
                    selectedIndex = selectedSection.value,
                    onSelectionChange = {
                        coroutineScope.launch {
                            sectionListState.animateScrollToSection(it)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                CategoryList(
                    categories = categories,
                    sectionListState = sectionListState,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CategoryList(
    categories: List<Category>,
    sectionListState: SectionListState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = sectionListState.lazyListState,
        modifier = modifier
            .fillMaxWidth()
            .nestedScroll(sectionListState.nestedScrollConnection)
    ) {
        sections(
            sections = categories,
            sectionItems = { category -> category.items },
            headerContentType = { "Header" },
            headerContent = { category ->
                SectionHeader(
                    title = category.title
                )
            },
            itemKey = { item -> item.id },
            itemContentType = { "Item" },
            itemContent = { item ->
                CategoryItemView(
                    model = item,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}