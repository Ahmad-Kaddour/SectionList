package com.ahmadkaddour.sectionlist


internal fun List<Int>.toPrefixSum(): List<Int> {
    var sum = 0
    return List(this.size) { i -> sum += this[i]; sum }
}

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

internal fun Int.toItemIndex(sectionsSizePrefixSum: List<Int>): Int {
    return if (this <= 0) 0 else sectionsSizePrefixSum[this - 1] + this
}
