package com.ahmadkaddour.sectionlist.model

import androidx.compose.runtime.Stable

@Stable
data class Category(
    val title: String,
    val items: List<CategoryItem>
)

