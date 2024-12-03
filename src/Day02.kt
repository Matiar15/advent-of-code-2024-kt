import kotlin.math.abs
import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        return Day02(input).countSafeReports()
    }

    fun part2(input: List<String>): Int {
        return Day02(input).countSafeReportsWithProblemDampener()
    }

    val part1 = part1(readInput("Day02"))
    part1.println()

    val part2 = part2(readInput("Day02"))
    part2.println()
}


class Day02(input: List<String>) {
    private val lineNumbers = input.map { it.split(" ") }.map { it.map(String::toInt) }
    fun countSafeReports(): Int {
        val safeReports = lineNumbers.fold(0) { acc, list ->
            list.forEachIndexed { index, currElem ->
                if (index == 0) return@forEachIndexed
                val previousElem = list[index - 1]
                if ((currElem - previousElem).absoluteValue > 3) return@fold acc
                if ((currElem - previousElem).absoluteValue == 0) return@fold acc
                if (index == 1) return@forEachIndexed
                val mostPrevious = list[index - 2]
                if (mostPrevious < previousElem && previousElem > currElem) return@fold acc
                if (mostPrevious > previousElem && previousElem < currElem) return@fold acc
            }
            acc + 1
        }
        return safeReports
    }

    fun countSafeReportsWithProblemDampener(): Int {
        return lineNumbers.count { line ->
            line.indices.any { isSafe(line.toMutableList().apply { removeAt(it) }) }
        }
    }

    private fun isSafe(lineNumbers: List<Int>): Boolean {
        val pairs = lineNumbers.zipWithNext()
        return pairs.all { it.isCorrectDifference() } && (pairs.all { (a, b) -> a > b } || pairs.all { (a, b) -> b > a })
    }


    private fun Pair<Int, Int>.isCorrectDifference() = abs(second - first) in (1..3)
}
