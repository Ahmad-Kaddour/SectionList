package sectionListUtils

import com.ahmadkaddour.sectionlist.toItemIndex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith


class SectionToItemIndexTest {
    @Test
    fun `section index to item index`() {
        val prefixSum = listOf(3, 5, 9)
        assertEquals(0, 0.toItemIndex(prefixSum))
        assertEquals(3, 1.toItemIndex(prefixSum))
        assertEquals(5, 2.toItemIndex(prefixSum))
    }

    @Test
    fun `single item sections`() {
        val prefixSum = listOf(1, 2, 3)
        assertEquals(0, 0.toItemIndex(prefixSum))
        assertEquals(1, 1.toItemIndex(prefixSum))
        assertEquals(2, 2.toItemIndex(prefixSum))
    }

    @Test
    fun `single element in prefixSum list`() {
        val prefixSum = listOf(5)
        assertEquals(0, 0.toItemIndex(prefixSum))
    }

    @Test
    fun `throws when sectionIndex is negative`() {
        val prefixSum = listOf(5, 10)
        assertFailsWith<IllegalArgumentException> { (-1).toItemIndex(prefixSum) }
    }

    @Test
    fun `throws when sectionsSizePrefixSum is empty`() {
        val prefixSum = emptyList<Int>()
        assertFailsWith<IllegalArgumentException> { 0.toItemIndex(prefixSum) }
    }

    @Test
    fun `throws when sectionIndex is greater than sectionsSizePrefixSum lastIndex`() {
        val prefixSum = listOf(5, 10)
        assertFailsWith<IllegalArgumentException> { 2.toItemIndex(prefixSum) }
    }
}