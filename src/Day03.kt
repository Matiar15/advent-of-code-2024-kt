fun main() {
    fun part1(input: List<String>): Int {
        return Day03(input).part1()
    }

    fun part2(input: List<String>): Int {
        return Day03(input).part2()
    }

    val part1 = part1(readInput("Day03"))
    part1.println()

    val part2 = part2(readInput("Day03"))
    part2.println()
}


class Day03(private val input: List<String>) {
    private val mulPattern = """mul\(\d{1,3},\d{1,3}\)""".toRegex()
    private val instructionMuiPattern = """do\(\)|don't\(\)|$mulPattern""".toRegex()

    fun part1(): Int {
        return mulPattern.findAllValuesToList(input).sumOf { it.multiplyFactors() }
    }

    fun part2(): Int {
        val multiplicationsWithInstruction = instructionMuiPattern
            .findAllValuesToList(input)
        return multiplicationWithInstruction(multiplicationsWithInstruction)
    }

    private tailrec fun multiplicationWithInstruction(
        instructions: List<String>,
        sum: Int = 0,
        enabled: Boolean = true
    ): Int {
        if (instructions.isEmpty()) return sum
        val current = instructions.first()
        return when {
            current == "do()" -> multiplicationWithInstruction(
                instructions = instructions.toMutableList().apply { remove(current) },
                sum = sum,
                enabled = true
            )

            current == "don't()" -> multiplicationWithInstruction(
                instructions = instructions.toMutableList().apply { remove(current) }, sum = sum, enabled = false
            )

            enabled && current.contains("mul") -> multiplicationWithInstruction(
                instructions = instructions.toMutableList().apply { remove(current) },
                sum = sum + current.multiplyFactors(),
                enabled = true
            )

            else -> {
                multiplicationWithInstruction(
                    instructions = instructions.toMutableList().apply { remove(current) },
                    sum = sum,
                    enabled = enabled
                )
            }
        }
    }

    private fun Regex.findAllValuesToList(stringMatchingRegex: List<String>) =
        this.findAll(stringMatchingRegex.joinToString()).map { it.value }.toList()

    private fun String.multiplyFactors(): Int {
        val first = substringBefore(",").filter { sub -> sub.isDigit() }.toInt()
        val second = substringAfter(",").filter { sub -> sub.isDigit() }.toInt()
        return first * second
    }
}
