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
 */
internal fun Int.toSectionIndex(sectionsSizePrefixSum: List<Int>): Int {
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
 */
internal fun Int.toItemIndex(sectionsSizePrefixSum: List<Int>): Int {
    return if (this <= 0) 0 else sectionsSizePrefixSum[this - 1] - 1
}
