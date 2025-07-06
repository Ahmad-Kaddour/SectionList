package sectionListUtils

import com.ahmadkaddour.sectionlist.toSectionIndex
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ItemToSectionIndexTest {
    @Test
    fun `item in first section`() {
        val prefixSum = listOf(3, 5, 9)
        assertEquals(0, 0.toSectionIndex(prefixSum))
        assertEquals(0, 1.toSectionIndex(prefixSum))
        assertEquals(0, 2.toSectionIndex(prefixSum))
    }

    @Test
    fun `item in middle section`() {
        val prefixSum = listOf(3, 5, 9)
        assertEquals(1, 3.toSectionIndex(prefixSum))
        assertEquals(1, 4.toSectionIndex(prefixSum))
    }

    @Test
    fun `item in last section`() {
        val prefixSum = listOf(3, 5, 9)
        assertEquals(2, 5.toSectionIndex(prefixSum))
        assertEquals(2, 8.toSectionIndex(prefixSum))
    }

    @Test
    fun `sections with single items`() {
        val prefixSum = listOf(1, 2, 3)
        assertEquals(0, 0.toSectionIndex(prefixSum))
        assertEquals(1, 1.toSectionIndex(prefixSum))
        assertEquals(2, 2.toSectionIndex(prefixSum))
    }

    @Test
    fun `larger number of sections`() {
        val prefixSum = listOf(10, 20, 25, 35, 40)
        assertEquals(0, 0.toSectionIndex(prefixSum))
        assertEquals(0, 9.toSectionIndex(prefixSum))
        assertEquals(1, 10.toSectionIndex(prefixSum))
        assertEquals(2, 24.toSectionIndex(prefixSum))
        assertEquals(4, 39.toSectionIndex(prefixSum))
    }

    @Test
    fun `including empty sections`() {
        val prefixSum = listOf(1, 2, 2, 2, 3)
        assertEquals(0, 0.toSectionIndex(prefixSum))
        assertEquals(1, 1.toSectionIndex(prefixSum))
        assertEquals(4, 2.toSectionIndex(prefixSum))
    }

    @Test
    fun `single empty section`() {
        val prefixSum = listOf(0)
        assertFailsWith<IllegalArgumentException> { 0.toSectionIndex(prefixSum) }
    }

    @Test
    fun `single section list`() {
        val prefixSum = listOf(5)
        assertEquals(0, 0.toSectionIndex(prefixSum))
        assertEquals(0, 4.toSectionIndex(prefixSum))
    }

    @Test
    fun `default to 0 when sectionsSizePrefixSum is empty`() {
        val prefixSum = emptyList<Int>()
        assertEquals(0, 0.toSectionIndex(prefixSum))
    }

    @Test
    fun `throws when itemIndex is negative`() {
        val prefixSum = listOf(5, 10)
        assertFailsWith<IllegalArgumentException> { (-1).toSectionIndex(prefixSum) }
    }

    @Test
    fun `throws when itemIndex is equal to total items`() {
        val prefixSum = listOf(5, 10)
        assertFailsWith<IllegalArgumentException> { 10.toSectionIndex(prefixSum) }
    }

    @Test
    fun `throws when itemIndex is greater than total items`() {
        val prefixSum = listOf(5, 10)
        assertFailsWith<IllegalArgumentException> { 11.toSectionIndex(prefixSum) }
    }
}