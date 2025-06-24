package sectionListUtils

import com.ahmadkaddour.sectionlist.toPrefixSum
import kotlin.test.Test
import kotlin.test.assertEquals

class ListToPrefixSumTest {
    @Test
    fun `test positive integers`() {
        val input = listOf(1, 2, 3, 4, 5)
        val expected = listOf(1, 3, 6, 10, 15)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test negative integers`() {
        val input = listOf(-1, -2, -3, -4, -5)
        val expected = listOf(-1, -3, -6, -10, -15)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test mixed positive and negative integers`() {
        val input = listOf(10, -2, 5, -8, 3)
        val expected = listOf(10, 8, 13, 5, 8)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test list with zeros`() {
        val input = listOf(1, 0, 2, 0, 3)
        val expected = listOf(1, 1, 3, 3, 6)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test list with all zeros`() {
        val input = listOf(0, 0, 0, 0)
        val expected = listOf(0, 0, 0, 0)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test empty list`() {
        val input = emptyList<Int>()
        val expected = emptyList<Int>()
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test single positive element`() {
        val input = listOf(100)
        val expected = listOf(100)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test single negative element`() {
        val input = listOf(-50)
        val expected = listOf(-50)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test single zero element`() {
        val input = listOf(0)
        val expected = listOf(0)
        assertEquals(expected, input.toPrefixSum())
    }

    @Test
    fun `test input list not modified`() {
        val originalList = listOf(5, 10, 15)
        originalList.toPrefixSum()
        assertEquals(listOf(5, 10, 15), originalList, "Original list should not be modified.")
    }
}