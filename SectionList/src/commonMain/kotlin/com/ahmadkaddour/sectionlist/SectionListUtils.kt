package com.ahmadkaddour.sectionlist


internal fun List<Int>.toPrefixSum(): List<Int> {
    var sum = 0
    return List(this.size) { i -> sum += this[i]; sum }
}

/**
 * Converts an item index in the flattened list to its corresponding section index.
 * This function uses a binary search algorithm to efficiently find the section index.
 *
 * @param sectionsSizePrefixSum A list representing the prefix sum of the sizes of all sections.
 * @return The index of the section to which the item belongs.
 * @throws IllegalArgumentException if `itemIndex` is negative, `sectionsSizePrefixSum` is empty,
 *                                  or `itemIndex` is out of bounds (greater than or equal to the total number of items).
 */
internal fun Int.toSectionIndex(sectionsSizePrefixSum: List<Int>): Int {
    require(this >= 0) {
        "Item index cannot be negative: $this"
    }
    require(sectionsSizePrefixSum.isNotEmpty()) {
        "sectionsSizePrefixSum cannot be empty."
    }
    require(this < sectionsSizePrefixSum.last()) {
        "Item index ($this) is out of bounds for the total number of items (${sectionsSizePrefixSum.last()})."
    }

    var low = 0
    var high = sectionsSizePrefixSum.lastIndex

    while (low <= high) {
        val mid = (low + high).ushr(1)
        val midVal = sectionsSizePrefixSum[mid] - 1

        if (midVal < this)
            low = mid + 1
        else
            high = mid - 1
    }
    return low
}


/**
 * Converts a section index to its corresponding item index in the flattened list.
 *
 * @param sectionsSizePrefixSum A list representing the prefix sum of the sizes of all sections.
 * @return The item index in the flattened list that corresponds to the start of the section.
 * @throws IllegalArgumentException if `sectionIndex` is negative, `sectionsSizePrefixSum` is empty,
 *                                  or `sectionIndex` is out of bounds (greater than the number of sections).
 **/
internal fun Int.toItemIndex(sectionsSizePrefixSum: List<Int>): Int {
    require(this >= 0) { "Section index cannot be negative: $this" }
    require(sectionsSizePrefixSum.isNotEmpty()) { "sectionsSizePrefixSum cannot be empty." }
    require(this <= sectionsSizePrefixSum.lastIndex) {
        "Section index ($this) is out of bounds. Max valid section index is ${sectionsSizePrefixSum.lastIndex}."
    }

    return if (this == 0) 0 else sectionsSizePrefixSum[this - 1]
}
