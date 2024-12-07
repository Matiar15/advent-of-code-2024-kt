fun main() {
    fun part1(input: List<String>): Int {
        return Day05(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day05(input).part1()
    }

    val part1 = part1(readInput("Day05_test"))
    part1.println()

    val part2 = part2(readInput("Day05"))
    part2.println()
}


class Day05(input: List<String>) {
    private val order: Map<Int, List<Int>> = input
        .filterNot { it.isEmpty() }
        .filter { it.contains("|") }
        .groupBy({ it.substringAfter("|").toInt() }) { it.substringBefore("|").toInt() }


    private val updates: List<List<Int>> = input
        .filterNot { it.isEmpty() }
        .filterNot { it.contains("|") }
        .map { line ->
            line
                .trim()
                .splitToSequence(",")
                .filter(String::isNotBlank)
                .map(String::toInt)
                .toList()
        }


    fun part1(): Int {
        return updates.sumOf {
            val sortedLine = sorted(it)
            if (it != sortedLine) sortedLine.elementAt(sortedLine.middleIndex) else 0
        }
    }

    private fun sorted(
        update: List<Int>,
    ): List<Int> {
        return searchForCorrectOrder(update = update, originalSize = update.size)
    }

    private tailrec fun searchForCorrectOrder(idx: Int = 0, originalSize: Int, collected: List<Int> = emptyList(), update: List<Int>): List<Int> {
        if (collected.size == originalSize) return collected
        val currentOrder = order.filter { it.key in collected }
        val forbiddenValues = currentOrder.values.flatten()
        val correctOrderElem = update.filterNot { it in forbiddenValues }.firstOrNull()
        return if (correctOrderElem != null) {
            searchForCorrectOrder(idx + 1, originalSize, collected + correctOrderElem, update - correctOrderElem)
        } else {
            searchForCorrectOrder(0, originalSize, emptyList(), update + collected)
        }
    }

    private val List<Int>.firstIndex: Int
        get() = indexOf(first())

    private val List<Int>.lastIndex: Int
        get() = size - 1

    private val List<Int>.middleIndex: Int
        get() = (firstIndex + lastIndex) / 2
}
