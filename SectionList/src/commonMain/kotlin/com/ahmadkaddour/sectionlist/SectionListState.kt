package com.ahmadkaddour.sectionlist

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge


@Stable
class SectionListState(
    val sectionsSize: List<Int>,
    val lazyListState: LazyListState
) {
    private val sectionSizesPrefixSum = sectionsSize.toPrefixSum()

    private var selectedSection by mutableIntStateOf(-1)

    val currentSection: Flow<Int>
        get() = merge(
            snapshotFlow { lazyListState.layoutInfo },
            snapshotFlow { selectedSection },
            lazyListState.interactionSource.interactions
        ).map { value ->
            if (value is DragInteraction) {
                resetSelectedSection()
            }
            when {
                selectedSection != -1 -> selectedSection
                !lazyListState.canScrollBackward -> firstVisibleSectionIndex
                !lazyListState.canScrollForward -> lastVisibleSectionIndex
                else -> {
                    val itemIndex = firstFullyVisibleItemIndex()
                    if (itemIndex == -1) firstVisibleSectionIndex
                    else itemIndex.toSectionIndex(sectionSizesPrefixSum)
                }
            }
        }

    private fun resetSelectedSection() {
        selectedSection = -1
    }

    val firstVisibleSectionIndex: Int
        get() = lazyListState.firstVisibleItemIndex.toSectionIndex(sectionSizesPrefixSum)

    val lastVisibleSectionIndex: Int
        get() = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.toSectionIndex(
            sectionSizesPrefixSum
        ) ?: 0


    internal fun firstFullyVisibleItemIndex(): Int {
        return lazyListState.layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                val itemStartOffset = item.offset
                val itemEndOffset = item.offset + item.size
                val viewPortHeight = lazyListState.layoutInfo.viewportSize.height
                itemStartOffset >= 0 && itemEndOffset <= viewPortHeight
            }?.index ?: -1
    }

    suspend fun scrollToSection(sectionIndex: Int, scrollOffset: Int = 0) {
        selectedSection =
            sectionIndex.coerceIn(minimumValue = 0, maximumValue = sectionsSize.lastIndex)
        lazyListState.scrollToItem(
            selectedSection.toItemIndex(sectionSizesPrefixSum),
            scrollOffset
        )
    }

    suspend fun animateScrollToSection(sectionIndex: Int, scrollOffset: Int = 0) {
        selectedSection =
            sectionIndex.coerceIn(minimumValue = 0, maximumValue = sectionsSize.lastIndex)
        lazyListState.animateScrollToItem(
            selectedSection.toItemIndex(sectionSizesPrefixSum),
            scrollOffset
        )
    }

    companion object {
        /**
         * The default [Saver] implementation for [SectionListState].
         */
        val Saver: Saver<SectionListState, *> = listSaver(
            save = {
                listOf(
                    with(LazyListState.Saver) { save(it.lazyListState) },
                    it.sectionsSize
                )
            },
            restore = @Suppress("UNCHECKED_CAST") {
                SectionListState(
                    lazyListState = (LazyListState.Saver as Saver<LazyListState, Any>).restore(value = it[0]!!)!!,
                    sectionsSize = it[1] as List<Int>
                )
            }
        )
    }
}

@Composable
fun rememberSectionListState(
    sectionsSize: List<Int>,
    lazyListState: LazyListState = rememberLazyListState()
): SectionListState {
    return rememberSaveable(
        sectionsSize,
        lazyListState,
        saver = SectionListState.Saver
    ) {
        SectionListState(
            sectionsSize = sectionsSize,
            lazyListState = lazyListState
        )
    }
}