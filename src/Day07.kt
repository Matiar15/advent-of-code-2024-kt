fun main() {
    fun part1(input: List<String>): Long {
        return Day07(input).part1()
    }

    fun part2(input: List<String>): Long {
        return Day07(input).part2()
    }

    val xd = part1(readInput("Day07_test"))
    xd.println()

//    val xdd = part2(readInput("Day07"))
//    xdd.println()
}

class Day07(private val input: List<String>) {
    fun part1(): Long =
        parseEquations(input).sumOf { (target, numbers) ->
            if (allPossibleCombinations(numbers, target)) target else 0
        }

    fun part2() = parseEquations(input).sumOf { (target, numbers) ->
        if (allPossibleCombinations(numbers, target, customOp = { a, b -> "$a$b".toLong()})) target else 0
    }


    private fun allPossibleCombinations(
        numbers: List<Int>,
        target: Long,
        currentValue: Long = 0,
        customOp: ((Long, Long) -> Long)? = null
    ): Boolean {
        if (numbers.isEmpty()) return currentValue == target

        val first = numbers.first().toLong()
        val remaining = numbers.drop(1)

        return when {
            currentValue == 0L -> allPossibleCombinations(remaining, target, first, customOp)
            allPossibleCombinations(remaining, target, currentValue + first, customOp) -> true
            allPossibleCombinations(remaining, target, currentValue * first, customOp) -> true
            customOp != null && allPossibleCombinations(
                remaining,
                target,
                customOp(currentValue, first),
                customOp
            ) -> true

            else -> false
        }
    }

    private fun parseEquations(input: List<String>): List<Pair<Long, List<Int>>> =
        input.map { line ->
            val (target, numbers) = line.split(":").map(String::trim)
            target.toLong() to numbers.split(" ").map(String::toInt)
        }
}
