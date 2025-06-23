package com.ahmadkaddour.sectionlist

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable


/**
 * Adds a section to the list with a count of items.
 * A section consists of an optional header, a list of items, and an optional footer.
 *
 * @param itemsCount The number of items in the section.
 * @param headerContent The content of the header. If null, no header will be displayed.
 * @param footerContent The content of the footer. If null, no footer will be displayed.
 * @param headerKey Optional key for the header item. Useful for performance optimizations.
 * @param headerContentType Optional content type for the header item. Useful for performance optimizations.
 * @param isStickyHeader If true, the header will stick to the top of the list while scrolling.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional key for the footer item. Useful for performance optimizations.
 * @param footerContentType Optional content type for the footer item. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * ### Example
 * ```
 * LazyColumn {
 *     section(
 *         itemsCount = 3,
 *         headerContent = { Text("Header") },
 *         footerContent = { Text("Footer") },
 *         itemContent = { index ->
 *             Text("Item #$index")
 *         }
 *     )
 * }
 * ```
 **/
fun LazyListScope.section(
    itemsCount: Int,
    headerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    footerContent: (@Composable LazyItemScope.() -> Unit)? = null,
    headerKey: Any? = null,
    headerContentType: Any? = null,
    isStickyHeader: Boolean = false,
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

/**
 * Adds a section to the list with the given [items].
 * A section consists of an optional header, a list of items, and an optional footer.
 *
 * @param items The list of items in the section.
 * @param headerContent The content of the header. If null, no header will be displayed.
 * @param footerContent The content of the footer. If null, no footer will be displayed.
 * @param headerKey Optional key for the header item. Useful for performance optimizations.
 * @param headerContentType Optional content type for the header item. Useful for performance optimizations.
 * @param isStickyHeader If true, the header will stick to the top of the list while scrolling.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional key for the footer item. Useful for performance optimizations.
 * @param footerContentType Optional content type for the footer item. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * ### Example
 * ```
 * LazyColumn {
 *     section(
 *         items = listOf("A", "B", "C"),
 *         headerContent = { Text("List Header") },
 *         footerContent = { Text("End of List") },
 *         itemContent = { item ->
 *             Text(text = item)
 *         }
 *     )
 * }
 * ```
 */
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

/**
 * Adds a section to the list with the given [items] where the content of an item is aware of its index.
 * A section consists of an optional header, a list of items, and an optional footer.
 *
 * @param items The list of items in the section.
 * @param headerContent The content of the header. If null, no header will be displayed.
 * @param footerContent The content of the footer. If null, no footer will be displayed.
 * @param headerKey Optional key for the header item. Useful for performance optimizations.
 * @param headerContentType Optional content type for the header item. Useful for performance optimizations.
 * @param isStickyHeader If true, the header will stick to the top of the list while scrolling.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional key for the footer item. Useful for performance optimizations.
 * @param footerContentType Optional content type for the footer item. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * ### Example
 * ```
 * LazyColumn {
 *     sectionIndexed(
 *         items = listOf("A", "B", "C"),
 *         headerContent = { Text("List Header") },
 *         footerContent = { Text("End of List") },
 *         itemContent = { index, item ->
 *             Text(text = "Item at index ${index}: $item")
 *         }
 *     )
 * }
 * ```
 */
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


/**
 * Adds a [count] of sections to a list.
 *
 * This is a utility function that helps you define repeated sections in a lazy list,
 * where each section can contain its own header, footer, and items.
 *
 * @param count The total number of sections to add.
 * @param sectionContent A lambda that defines the content of each section.
 * The lambda provides the section index and the [LazyListScope] to build content for that section.
 *
 * ### Example:
 * ```
 * LazyColumn {
 *     sections(count = 3) { section ->
 *         section(
 *             itemsCount = 10,
 *             headerContent = { Text("Header $section") },
 *             itemContent = { item -> Text("Item $item in Section $section") },
 *             footerContent = { HorizontalDivider() }
 *         )
 *     }
 * }
 * ```
 */
inline fun LazyListScope.sections(
    count: Int,
    sectionContent: LazyListScope.(sectionIndex: Int) -> Unit
) {
    for (section in 0 until count) {
        sectionContent(section)
    }
}

/**
 * Adds multiple sections to a list where each section consists of an optional header, a list of items, and an optional footer.
 *
 * @param sections A list of item lists, where each inner list represents a section items.
 * @param headerContent Optional composable lambda for rendering a header for each section.
 * @param footerContent Optional composable lambda for rendering a footer for each section.
 * @param headerKey Optional function to provide a unique key for each header. Useful for performance optimizations.
 * @param headerContentType Optional function to provide a content type for each header. Useful for performance optimizations.
 * @param isStickyHeader Whether the header of a given section should stick to the top during scroll.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional function to provide a unique key for each footer. Useful for performance optimizations.
 * @param footerContentType Optional function to provide a content type for each footer. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * **Note:** Providing [headerContent] or [footerContent] lambdas will result in a header or footer being added
 * for every section, regardless of whether the returned composable is empty. If you're calculating the total
 * item count per section, be sure to account for these.
 *
 * ### Example:
 * ```
 * LazyColumn {
 *     sections(
 *         sections = listOf(listOf("A1", "A2"), listOf("B1", "B2", "B3")),
 *         headerContent = { section -> Text("Header $section") },
 *         footerContent = { section -> Text("Footer $section") },
 *         itemContent = { item -> Text("Item: $item") }
 *     )
 * }
 * ```
 */
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

/**
 * Adds multiple sections to a lazy list, where you can provide custom item factory for each section.
 * A section consists of an optional header, a list of items, and an optional footer.
 *
 * @param sections The list of section models.
 * @param sectionItems A function that maps each section to its list of items.
 * @param headerContent Optional composable lambda for rendering a header for each section.
 * @param footerContent Optional composable lambda for rendering a footer for each section.
 * @param headerKey Optional function to provide a unique key for each header. Useful for performance optimizations.
 * @param headerContentType Optional function to provide a content type for each header. Useful for performance optimizations.
 * @param isStickyHeader Whether the header of a given section should stick to the top during scroll.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional function to provide a unique key for each footer. Useful for performance optimizations.
 * @param footerContentType Optional function to provide a content type for each footer. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * **Note:** Providing [headerContent] or [footerContent] lambdas will result in a header or footer being added
 * for every section, regardless of whether the returned composable is empty. If you're calculating the total
 * item count per section, be sure to account for these.
 *
 * ### Example usage:
 * ```
 * LazyColumn {
 *     sections(
 *         sections = listOf("Section A", "Section B"),
 *         sectionItems = { section -> getItemsForSection(section) },
 *         headerContent = { section -> Text("Header for $section") },
 *         itemContent = { item -> Text(item.name) }
 *     )
 * }
 * ```
 */
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

/**
 * Adds multiple sections to a list where each section consists of an optional header, a list of items, and an optional footer.
 * This function provides access to both the section index and the item index within each section.
 *
 * @param sections A list of item lists, where each inner list represents a section items.
 * @param headerContent Optional composable lambda for rendering a header for each section.
 * @param footerContent Optional composable lambda for rendering a footer for each section.
 * @param headerKey Optional function to provide a unique key for each header. Useful for performance optimizations.
 * @param headerContentType Optional function to provide a content type for each header. Useful for performance optimizations.
 * @param isStickyHeader Whether the header of a given section should stick to the top during scroll.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional function to provide a unique key for each footer. Useful for performance optimizations.
 * @param footerContentType Optional function to provide a content type for each footer. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * **Note:** Providing [headerContent] or [footerContent] lambdas will result in a header or footer being added
 * for every section, regardless of whether the returned composable is empty. If you're calculating the total
 * item count per section, be sure to account for these.
 *
 * ### Example:
 * ```
 * LazyColumn {
 *     sectionsIndexed(
 *         sections = listOf(listOf("Apple", "Avocado"), listOf("Banana", "Blueberry")),
 *         headerContent = { sectionIndex -> Text("Header $sectionIndex") },
 *         itemKey = { sectionIndex, itemIndex, item -> "$sectionIndex-$itemIndex-$item" },
 *         itemContent = { sectionIndex, itemIndex, item -> Text("[$sectionIndex:$itemIndex] $item") }
 *     )
 * }
 * ```
 */
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

/**
 * Adds multiple sections to a lazy list, where you can provide custom item factory for each section.
 * A section consists of an optional header, a list of items, and an optional footer.
 * This function provides access to both the section index and the item index within each section.
 *
 * @param sections The list of section models.
 * @param sectionItems A function that maps each section to its list of items.
 * @param headerContent Optional composable lambda for rendering a header for each section.
 * @param footerContent Optional composable lambda for rendering a footer for each section.
 * @param headerKey Optional function to provide a unique key for each header. Useful for performance optimizations.
 * @param headerContentType Optional function to provide a content type for each header. Useful for performance optimizations.
 * @param isStickyHeader Whether the header of a given section should stick to the top during scroll.
 * @param itemKey Optional function to provide a unique key for each item. Useful for performance optimizations.
 * @param itemContentType Optional function to provide a content type for each item. Useful for performance optimizations.
 * @param footerKey Optional function to provide a unique key for each footer. Useful for performance optimizations.
 * @param footerContentType Optional function to provide a content type for each footer. Useful for performance optimizations.
 * @param itemContent Composable content for each item in the section.
 *
 * **Note:** Providing [headerContent] or [footerContent] lambdas will result in a header or footer being added
 * for every section, regardless of whether the returned composable is empty. If you're calculating the total
 * item count per section, be sure to account for these.
 *
 * ### Example usage:
 * ```
 * LazyColumn {
 *     sectionsIndexed(
 *         sections = listOf(Section("Fruits"), Section("Vegetables")),
 *         sectionItems = { sectionIndex, section -> section.items },
 *         headerContent = { sectionIndex, section -> Text("Header($sectionIndex): ${section.title}") },
 *         itemContent = { sectionIndex, itemIndex, item ->
 *             Text("[$sectionIndex:$itemIndex] $item")
 *         }
 *     )
 * }
 * ```
 */
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