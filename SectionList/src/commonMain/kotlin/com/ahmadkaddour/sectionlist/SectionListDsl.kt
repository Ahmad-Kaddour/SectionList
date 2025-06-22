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

inline fun <T> LazyListScope.sections(
    sections: List<List<T>>,
    noinline headerContent: (@Composable LazyItemScope.(sectionIndex: Int) -> Unit)? = null,
    noinline footerContent: (@Composable LazyItemScope.(sectionIndex: Int) -> Unit)? = null,
    headerKey: (sectionIndex: Int) -> Any? = { null },
    headerContentType: (sectionIndex: Int) -> Any? = { null },
    isStickyHeader: (sectionIndex: Int) -> Boolean = { false },
    noinline itemKey: ((item: T) -> Any)? = null,
    crossinline itemContentType: (item: T) -> Any? = { null },
    footerKey: (sectionIndex: Int) -> Any? = { null },
    footerContentType: (sectionIndex: Int) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    sections(sections.count()) { section ->
        section(
            items = sections[section],
            headerContent = headerContent?.let { { it(section) } },
            footerContent = footerContent?.let { { it(section) } },
            itemContent = itemContent,
            headerKey = headerKey(section),
            headerContentType = headerContentType(section),
            isStickyHeader = isStickyHeader(section),
            itemKey = itemKey,
            itemContentType = itemContentType,
            footerKey = footerKey(section),
            footerContentType = footerContentType(section)
        )
    }
}

inline fun <T> LazyListScope.sections(
    sections: List<List<T>>,
    headerContent: (sectionIndex: Int) -> (@Composable LazyItemScope.() -> Unit)? = { null },
    footerContent: (sectionIndex: Int) -> (@Composable LazyItemScope.() -> Unit)? = { null },
    headerKey: (sectionIndex: Int) -> Any? = { null },
    headerContentType: (sectionIndex: Int) -> Any? = { null },
    isStickyHeader: (sectionIndex: Int) -> Boolean = { false },
    noinline itemKey: ((item: T) -> Any)? = null,
    crossinline itemContentType: (item: T) -> Any? = { null },
    footerKey: (sectionIndex: Int) -> Any? = { null },
    footerContentType: (sectionIndex: Int) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) {
    sections(sections.count()) { section ->
        section(
            items = sections[section],
            headerContent = headerContent(section),
            footerContent = footerContent(section),
            itemContent = itemContent,
            headerKey = headerKey(section),
            headerContentType = headerContentType(section),
            isStickyHeader = isStickyHeader(section),
            itemKey = itemKey,
            itemContentType = itemContentType,
            footerKey = footerKey(section),
            footerContentType = footerContentType(section)
        )
    }
}

inline fun <S, I> LazyListScope.sections(
    sections: List<S>,
    sectionItems: (section: S) -> List<I>,
    noinline headerContent: (@Composable LazyItemScope.(section: S) -> Unit)? = null,
    noinline footerContent: (@Composable LazyItemScope.(section: S) -> Unit)? = null,
    headerKey: (section: S) -> Any? = { null },
    headerContentType: (section: S) -> Any? = { null },
    isStickyHeader: (section: S) -> Boolean = { false },
    noinline itemKey: ((item: I) -> Any)? = null,
    crossinline itemContentType: (item: I) -> Any? = { null },
    footerKey: (section: S) -> Any? = { null },
    footerContentType: (section: S) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: I) -> Unit
) {
    sections(sections.count()) { index ->
        section(
            items = sectionItems(sections[index]),
            headerContent = headerContent?.let { { it(sections[index]) } },
            footerContent = footerContent?.let { { it(sections[index]) } },
            itemContent = itemContent,
            headerKey = headerKey(sections[index]),
            headerContentType = headerContentType(sections[index]),
            isStickyHeader = isStickyHeader(sections[index]),
            itemKey = itemKey,
            itemContentType = itemContentType,
            footerKey = footerKey(sections[index]),
            footerContentType = footerContentType(sections[index])
        )
    }
}

inline fun <S, I> LazyListScope.sections(
    sections: List<S>,
    sectionItems: (section: S) -> List<I>,
    headerContent: (section: S) -> (@Composable LazyItemScope.() -> Unit)? = { null },
    footerContent: (section: S) -> (@Composable LazyItemScope.() -> Unit)? = { null },
    headerKey: (section: S) -> Any? = { null },
    headerContentType: (section: S) -> Any? = { null },
    isStickyHeader: (section: S) -> Boolean = { false },
    noinline itemKey: ((item: I) -> Any)? = null,
    crossinline itemContentType: (item: I) -> Any? = { null },
    footerKey: (section: S) -> Any? = { null },
    footerContentType: (section: S) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(item: I) -> Unit
) {
    sections(sections.count()) { index ->
        section(
            items = sectionItems(sections[index]),
            headerContent = headerContent(sections[index]),
            footerContent = footerContent(sections[index]),
            itemContent = itemContent,
            headerKey = headerKey(sections[index]),
            headerContentType = headerContentType(sections[index]),
            isStickyHeader = isStickyHeader(sections[index]),
            itemKey = itemKey,
            itemContentType = itemContentType,
            footerKey = footerKey(sections[index]),
            footerContentType = footerContentType(sections[index])
        )
    }
}

inline fun <T> LazyListScope.sectionsIndexed(
    sections: List<List<T>>,
    noinline headerContent: (@Composable LazyItemScope.(sectionIndex: Int) -> Unit)? = null,
    noinline footerContent: (@Composable LazyItemScope.(sectionIndex: Int) -> Unit)? = null,
    headerKey: (sectionIndex: Int) -> Any? = { null },
    headerContentType: (sectionIndex: Int) -> Any? = { null },
    isStickyHeader: (sectionIndex: Int) -> Boolean = { false },
    noinline itemKey: ((sectionIndex: Int, itemIndexed: Int, item: T) -> Any)? = null,
    crossinline itemContentType: (sectionIndex: Int, itemIndexed: Int, item: T) -> Any? = { _, _, _ -> null },
    footerKey: (sectionIndex: Int) -> Any? = { null },
    footerContentType: (sectionIndex: Int) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(sectionIndex: Int, itemIndexed: Int, item: T) -> Unit
) {
    sections(sections.count()) { sectionIndex ->
        sectionIndexed(
            items = sections[sectionIndex],
            headerContent = headerContent?.let { { it(sectionIndex) } },
            footerContent = footerContent?.let { { it(sectionIndex) } },
            itemContent = { itemIndex, item -> itemContent(sectionIndex, itemIndex, item) },
            headerKey = headerKey(sectionIndex),
            headerContentType = headerContentType(sectionIndex),
            isStickyHeader = isStickyHeader(sectionIndex),
            itemKey = if (itemKey == null) null else { itemIndex, item ->
                itemKey(
                    sectionIndex,
                    itemIndex,
                    item
                )
            },
            itemContentType = { itemIndex, item -> itemContentType(sectionIndex, itemIndex, item) },
            footerKey = footerKey(sectionIndex),
            footerContentType = footerContentType(sectionIndex)
        )
    }
}

inline fun <T> LazyListScope.sectionsIndexed(
    sections: List<List<T>>,
    headerContent: (sectionIndex: Int) -> (@Composable LazyItemScope.() -> Unit)? = { null },
    footerContent: (sectionIndex: Int) -> (@Composable LazyItemScope.() -> Unit)? = { null },
    headerKey: (sectionIndex: Int) -> Any? = { null },
    headerContentType: (sectionIndex: Int) -> Any? = { null },
    isStickyHeader: (sectionIndex: Int) -> Boolean = { false },
    noinline itemKey: ((sectionIndex: Int, itemIndexed: Int, item: T) -> Any)? = null,
    crossinline itemContentType: (sectionIndex: Int, itemIndexed: Int, item: T) -> Any? = { _, _, _ -> null },
    footerKey: (sectionIndex: Int) -> Any? = { null },
    footerContentType: (sectionIndex: Int) -> Any? = { null },
    crossinline itemContent: @Composable LazyItemScope.(sectionIndex: Int, itemIndexed: Int, item: T) -> Unit
) {
    sections(sections.count()) { sectionIndex ->
        sectionIndexed(
            items = sections[sectionIndex],
            headerContent = headerContent(sectionIndex),
            footerContent = footerContent(sectionIndex),
            itemContent = { itemIndex, item -> itemContent(sectionIndex, itemIndex, item) },
            headerKey = headerKey(sectionIndex),
            headerContentType = headerContentType(sectionIndex),
            isStickyHeader = isStickyHeader(sectionIndex),
            itemKey = if (itemKey == null) null else { itemIndex, item ->
                itemKey(
                    sectionIndex,
                    itemIndex,
                    item
                )
            },
            itemContentType = { itemIndex, item -> itemContentType(sectionIndex, itemIndex, item) },
            footerKey = footerKey(sectionIndex),
            footerContentType = footerContentType(sectionIndex)
        )
    }
}

inline fun <S, I> LazyListScope.sectionsIndexed(
    sections: List<S>,
    sectionItems: (sectionIndex: Int, section: S) -> List<I>,
    noinline headerContent: (@Composable LazyItemScope.(sectionIndex: Int, section: S) -> Unit)? = null,
    noinline footerContent: (@Composable LazyItemScope.(sectionIndex: Int, section: S) -> Unit)? = null,
    headerKey: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    headerContentType: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    isStickyHeader: (sectionIndex: Int, section: S) -> Boolean = { _, _ -> false },
    noinline itemKey: ((sectionIndex: Int, itemIndexed: Int, item: I) -> Any)? = null,
    crossinline itemContentType: (sectionIndex: Int, itemIndexed: Int, item: I) -> Any? = { _, _, _ -> null },
    footerKey: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    footerContentType: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(sectionIndex: Int, itemIndexed: Int, item: I) -> Unit,
) {
    sections(sections.count()) { sectionIndex ->
        val section = sections[sectionIndex]
        sectionIndexed(
            items = sectionItems(sectionIndex, section),
            headerContent = headerContent?.let { { it(sectionIndex, section) } },
            footerContent = footerContent?.let { { it(sectionIndex, section) } },
            itemContent = { itemIndex, item -> itemContent(sectionIndex, itemIndex, item) },
            headerKey = headerKey(sectionIndex, section),
            headerContentType = headerContentType(sectionIndex, section),
            isStickyHeader = isStickyHeader(sectionIndex, section),
            itemKey = if (itemKey == null) null else { itemIndex, item ->
                itemKey(
                    sectionIndex,
                    itemIndex,
                    item
                )
            },
            itemContentType = { itemIndex, item -> itemContentType(sectionIndex, itemIndex, item) },
            footerKey = footerKey(sectionIndex, section),
            footerContentType = footerContentType(sectionIndex, section)
        )
    }
}

inline fun <S, I> LazyListScope.sectionsIndexed(
    sections: List<S>,
    sectionItems: (sectionIndex: Int, section: S) -> List<I>,
    headerContent: (sectionIndex: Int, section: S) -> (@Composable LazyItemScope.() -> Unit)? = { _, _ -> null },
    footerContent: (sectionIndex: Int, section: S) -> (@Composable LazyItemScope.() -> Unit)? = { _, _ -> null },
    headerKey: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    headerContentType: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    isStickyHeader: (sectionIndex: Int, section: S) -> Boolean = { _, _ -> false },
    noinline itemKey: ((sectionIndex: Int, itemIndexed: Int, item: I) -> Any)? = null,
    crossinline itemContentType: (sectionIndex: Int, itemIndexed: Int, item: I) -> Any? = { _, _, _ -> null },
    footerKey: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    footerContentType: (sectionIndex: Int, section: S) -> Any? = { _, _ -> null },
    crossinline itemContent: @Composable LazyItemScope.(sectionIndex: Int, itemIndexed: Int, item: I) -> Unit
) {
    sections(sections.count()) { sectionIndex ->
        val section = sections[sectionIndex]
        sectionIndexed(
            items = sectionItems(sectionIndex, section),
            headerContent = headerContent(sectionIndex, section),
            footerContent = footerContent(sectionIndex, section),
            itemContent = { itemIndex, item -> itemContent(sectionIndex, itemIndex, item) },
            headerKey = headerKey(sectionIndex, section),
            headerContentType = headerContentType(sectionIndex, section),
            isStickyHeader = isStickyHeader(sectionIndex, section),
            itemKey = if (itemKey == null) null else { itemIndex, item ->
                itemKey(
                    sectionIndex,
                    itemIndex,
                    item
                )
            },
            itemContentType = { itemIndex, item -> itemContentType(sectionIndex, itemIndex, item) },
            footerKey = footerKey(sectionIndex, section),
            footerContentType = footerContentType(sectionIndex, section)
        )
    }
}