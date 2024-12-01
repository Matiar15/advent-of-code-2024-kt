import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        return Day01(input).countDistance()
    }

    fun part2(input: List<String>): Int {
        return Day01(input).countSimilarityScore()
    }

    val distance = part1(readInput("Day01"))
    distance.println()

    val similarityScore = part2(readInput("Day01"))
    similarityScore.println()
}

class Day01(input: List<String>) {
    private val pairedIdsFromLines: List<Pair<Int, Int>> =
        input.map { line ->
            val filter = line.filter { it.isDigit() }
            filter.take(5).toInt() to filter.takeLast(5).toInt()
        }

    private val firstList = Day01List(pairedIdsFromLines.map { it.first })
    private val secondList = Day01List(pairedIdsFromLines.map { it.second })

    fun countDistance(distance: Int = 0): Int {
        if (firstList.isEmpty && secondList.isEmpty) return distance
        val currentDistance = (firstList.smallest - secondList.smallest).absoluteValue
        firstList.removeSmallest()
        secondList.removeSmallest()
        return countDistance(distance + currentDistance)
    }

    fun countSimilarityScore(similarityScore: Int = 0): Int {
        if (firstList.isEmpty) return similarityScore
        firstList.first.println()
        val currentSimilarityScore = firstList.first * secondList.removeAppearancesOf(firstList.first)
        firstList.remove(firstList.first)

        return countSimilarityScore(similarityScore + currentSimilarityScore)
    }

    class Day01List(input: List<Int>) {
        private val actualList = input.toMutableList()
        val isEmpty: Boolean get() = actualList.isEmpty()
        val smallest: Int get() = actualList.min()
        val first: Int get() = actualList.first()

        private val smallestIndex: Int get() = actualList.indexOf(smallest)
        private fun isNotInList(value: Int) = actualList.indexOf(value) == -1

        fun remove(value: Int) = actualList.remove(value)

        fun removeSmallest() = if (actualList.isEmpty()) 0 else actualList.removeAt(smallestIndex)

        fun removeAppearancesOf(value: Int, appearances: Int = 0): Int {
            return if (actualList.isEmpty() || isNotInList(value)) appearances else {
                actualList.removeAt(actualList.indexOf(value))
                removeAppearancesOf(value, appearances + 1)
            }
        }
    }
}
