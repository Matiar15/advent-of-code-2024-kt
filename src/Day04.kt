fun main() {
    fun part1(input: List<String>): Int {
        return Day04(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day04(input).part2()
    }

    val xd = part1(readInput("Day04"))
    xd.println()
}

class Day04(private val input: List<String>) {
    private val splitInput = input.map { it.split("") }.map { line -> line.filter { it.isNotBlank() } }
    private val word = "XMAS"
    private val wordBackwards = "SAMX"
    private fun String.findWordOccurrences(): Int = Regex("""(?=($word|$wordBackwards))""").findAll(this).count()

    fun part1(): Int {
        val combinations: List<String> = splitVertically() + splitHorizontally() + splitDiagonally()
        return combinations.sumOf { it.findWordOccurrences() }
    }

    fun part2(): Int = 0

    private fun splitVertically(): List<String> = input

    private fun splitHorizontally(): List<String> = transpose(splitInput)

    private fun splitDiagonally(): List<String> {
        val fdiag = diagonalGroups(splitInput) { x, y -> x + y }
        val bdiag = diagonalGroups(splitInput) { x, y -> x - y }

        return fdiag + bdiag
    }
}

inline fun <reified T> transpose(xs: List<List<T>>): List<String> {
    val cols = xs[0].size
    val rows = xs.size
    return Array(cols) { j ->
        Array(rows) { i ->
            xs[i][j]
        }.joinToString("")
    }.toList()
}

inline fun diagonalGroups(data: List<List<String>>, pattern: (Int, Int) -> Int): List<String> {
    val grouping = mutableMapOf<Int, MutableList<String>>()

    for (y in data.indices) {
        for (x in data[y].indices) {
            val key = pattern(x, y)
            grouping.computeIfAbsent(key) { mutableListOf() }.add(data[y][x])
        }
    }

    return grouping.keys.sorted().map { grouping[it]?.joinToString("") ?: "" }.filter { it.isNotEmpty() }
}
