package com.ahmadkaddour.sectionlist

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach

/**
 * Represents the state of a list with distinct sections.
 * It allows observing the current visible section and scrolling to specific sections.
 *
 * @property sectionsSize A list where each element is the number of items in the corresponding section.
 * @property lazyListState The underlying [LazyListState] used to control the scrolling behavior.
 */
@Stable
class SectionListState(
    val sectionsSize: List<Int>,
    val lazyListState: LazyListState
) {
    private val sectionSizesPrefixSum = sectionsSize.toPrefixSum()

    private var selectedSectionProgrammatically by mutableStateOf<Int?>(null)

    // Flow from drag interactions. We only care about the event, not its value.
    @OptIn(ExperimentalCoroutinesApi::class)
    private val userDragInteractionFlow: Flow<Boolean> =
        snapshotFlow { lazyListState.interactionSource }
            .flatMapLatest { it.interactions }
            .mapLatest { interaction -> interaction is DragInteraction.Start && selectedSectionProgrammatically != null }
            .distinctUntilChanged()
            .onEach { reset ->
                if (reset) {
                    resetProgrammaticSectionSelection()
                }
            }

    /**
     * A flow emitting the index of the current prominent section.
     **/
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentSectionIndex: Flow<Int> by lazy {
        merge(
            snapshotFlow { lazyListState.layoutInfo },
            snapshotFlow { selectedSectionProgrammatically },
            userDragInteractionFlow.map { }
        ).mapLatest {
            determineCurrentSectionIndex()
        }.distinctUntilChanged()
    }

    private fun determineCurrentSectionIndex(): Int {
        selectedSectionProgrammatically?.let { return it }

        if (!lazyListState.canScrollBackward) return firstVisibleSectionIndex
        if (!lazyListState.canScrollForward) return lastVisibleSectionIndex

        val fullyVisibleItemIdx = firstFullyVisibleItemIndex()
        return if (fullyVisibleItemIdx != -1) {
            fullyVisibleItemIdx.toSectionIndex(sectionSizesPrefixSum)
        } else {
            firstVisibleSectionIndex
        }
    }

    /**
     * Resets the programmatically selected section. Typically called when user interaction
     * like dragging overrides a programmatic scroll.
     */
    private fun resetProgrammaticSectionSelection() {
        selectedSectionProgrammatically = null
    }

    /**
     * The index of the first visible section.
     */
    val firstVisibleSectionIndex: Int
        get() = lazyListState.firstVisibleItemIndex.toSectionIndex(sectionSizesPrefixSum)

    /**
     * The index of the last visible section. Returns 0 if the list is empty or no items are visible.
     */
    val lastVisibleSectionIndex: Int
        get() = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.toSectionIndex(
            sectionSizesPrefixSum
        ) ?: 0


    /**
     * Finds the index of the first item that is fully visible within the viewport.
     * Returns -1 if no item is fully visible.
     */
    internal fun firstFullyVisibleItemIndex(): Int {
        val layoutInfo = lazyListState.layoutInfo
        val viewportHeight = layoutInfo.viewportSize.height // Or .width for horizontal lists
        return layoutInfo.visibleItemsInfo
            .firstOrNull { item ->
                val itemStartOffset = item.offset
                val itemEndOffset = item.offset + item.size
                // Ensure item is fully within the viewport boundaries
                itemStartOffset >= 0 && itemEndOffset <= viewportHeight
            }?.index ?: -1
    }


    /**
     * Scrolls to the specified [sectionIndex] with an optional [scrollOffset].
     * @param sectionIndex The index of the section to scroll to.
     * @param scrollOffset The offset that the section should end up after the scroll.
     */
    suspend fun scrollToSection(sectionIndex: Int, scrollOffset: Int = 0) {
        val targetSection = sectionIndex.coerceIn(0, sectionSizesPrefixSum.lastIndex)
        selectedSectionProgrammatically = targetSection
        lazyListState.scrollToItem(
            targetSection.toItemIndex(sectionSizesPrefixSum),
            scrollOffset
        )
    }

    /**
     * Animates a scroll to the specified [sectionIndex] with an optional [scrollOffset].
     * @param sectionIndex The index of the section to scroll to.
     * @param scrollOffset The offset within the section to scroll to.
     */
    suspend fun animateScrollToSection(sectionIndex: Int, scrollOffset: Int = 0) {
        val targetSection = sectionIndex.coerceIn(0, sectionSizesPrefixSum.lastIndex)
        selectedSectionProgrammatically = targetSection
        lazyListState.animateScrollToItem(
            targetSection.toItemIndex(sectionSizesPrefixSum),
            scrollOffset
        )
    }

    companion object {
        /**
         * The default [Saver] implementation for [SectionListState].
         */
        val Saver: Saver<SectionListState, *> = listSaver(
            save = { state ->
                listOf(
                    with(LazyListState.Saver) { save(state.lazyListState) },
                    state.sectionsSize
                )
            },
            restore = @Suppress("UNCHECKED_CAST") { savedState ->
                SectionListState(
                    lazyListState = (LazyListState.Saver as Saver<LazyListState, Any>).restore(value = savedState[0] as Any)!!,
                    sectionsSize = savedState[1] as List<Int>
                )
            }
        )
    }
}

/**
 * Creates and remembers a [SectionListState] for controlling and observing a sectioned list.
 * This state handles mapping between item indices and section indices.
 *
 * @param sectionsSize A list where each element represents the number of items in the corresponding section.
 * @param lazyListState The state of the lazy list.
 * @return A remembered [SectionListState].
 */
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