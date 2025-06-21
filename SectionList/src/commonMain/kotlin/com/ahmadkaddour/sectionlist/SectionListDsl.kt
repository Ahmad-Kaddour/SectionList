package com.ahmadkaddour.sectionlist

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable


fun LazyListScope.section(
    itemsCount: Int,
    headerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    footerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    isStickyHeader: Boolean = false,
    headerKey: Any? = null,
    headerContentType: Any? = null,
    itemKey: ((index: Int) -> Any)? = null,
    itemContentType: (index: Int) -> Any? = { null },
    footerKey: Any? = null,
    footerContentType: Any? = null,
    itemContent: @Composable LazyItemScope.(index: Int) -> Unit
) {
    if (headerContent != null) {
        if (isStickyHeader) {
            stickyHeader(
                content = { headerContent() },
                key = headerKey,
                contentType = headerContentType
            )
        } else {
            item(content = headerContent, key = headerKey, contentType = headerContentType)
        }
    }

    items(
        count = itemsCount,
        key = itemKey,
        contentType = itemContentType,
        itemContent = itemContent
    )

    if (footerContent != null) {
        item(content = footerContent, key = footerKey, contentType = footerContentType)
    }
}

inline fun <T> LazyListScope.section(
    items: List<T>,
    noinline headerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    noinline footerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    headerKey: Any? = null,
    headerContentType: Any? = null,
    isStickyHeader: Boolean = false,
    noinline itemKey: ((item: T) -> Any)? = null,
    crossinline itemContentType: (item: T) -> Any? = { null },
    footerKey: Any? = null,
    footerContentType: Any? = null,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) =
    section(
        itemsCount = items.size,
        headerContent = headerContent,
        footerContent = footerContent,
        isStickyHeader = isStickyHeader,
        headerKey = headerKey,
        headerContentType = headerContentType,
        itemKey = if (itemKey != null) { index: Int -> itemKey(items[index]) } else null,
        itemContentType = { index -> itemContentType(items[index]) },
        footerKey = footerKey,
        footerContentType = footerContentType,
        itemContent = { index -> itemContent(items[index]) }
    )

inline fun <T> LazyListScope.sectionIndexed(
    items: List<T>,
    noinline headerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    noinline footerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    headerKey: Any? = null,
    headerContentType: Any? = null,
    isStickyHeader: Boolean = false,
    noinline itemKey: ((index: Int, item: T) -> Any)? = null,
    crossinline itemContentType: (index: Int, item: T) -> Any? = { _, _ -> null },
    footerKey: Any? = null,
    footerContentType: Any? = null,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) =
    section(
        itemsCount = items.size,
        headerContent = headerContent,
        footerContent = footerContent,
        isStickyHeader = isStickyHeader,
        headerKey = headerKey,
        headerContentType = headerContentType,
        itemKey = if (itemKey != null) { index: Int -> itemKey(index, items[index]) } else null,
        itemContentType = { index -> itemContentType(index, items[index]) },
        footerKey = footerKey,
        footerContentType = footerContentType,
        itemContent = { index -> itemContent(index, items[index]) }
    )


inline fun LazyListScope.sections(
    count: Int,
    sectionContent: LazyListScope.(sectionIndex: Int) -> Unit
) {
    for (section in 0 until count) {
        sectionContent(section)
    }
}