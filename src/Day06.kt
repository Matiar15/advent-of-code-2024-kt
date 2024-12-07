typealias Row = Int
typealias Column = Int

fun main() {
    fun part1(input: List<String>): Int {
        return Day06(input).countDistinctPositions()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val positions = part1(readInput("Day06"))
    positions.println()
}

class Day06(private val input: List<String>) {
    fun countDistinctPositions(): Int {
        return FieldMap(input).part1()
    }

}

class FieldMap(private val lines: List<String>) {
    private val map: List<List<Field>> = buildList {
        addAll(lines.map { line -> line.map { char -> Field.mapFromChar(char) } })
    }
    private val rowMinIdx: Int
        get() = 0

    private val rowMaxIdx: Int
        get() = map.size - 1

    private val columnMinIdx: Int
        get() = 0

    private val columnMaxIdx: Int
        get() = map[0].size - 1

    fun part1(): Int = walk().count()

    private tailrec fun walk(
        nextPosition: Position? = null,
        currentDirection: Direction = Direction.NORTH,
        walkedFields: Set<Position> = emptySet()
    ): Set<Position> {
        val current = nextPosition ?: startingPosition()
        if (walkedOutOfMap(current)) return walkedFields + current

        val field = map[current.row][current.column]

        val next = current.findNextPosition(currentDirection)
        val nextField = map[next.row][next.column]

        val direction = if (nextField.cantMove()) currentDirection.change() else currentDirection
        val walked = if (field.cantMove()) walkedFields else walkedFields + current

        return walk(current.findNextPosition(direction), direction, walked)
    }

    private fun startingPosition(): Position {
        fun List<List<Field>>.findCurrentRow() = first { row -> row.any { it == FieldMap.Field.Guard } }
        fun List<Field>.findCurrentColumn() = indexOf(first { it == FieldMap.Field.Guard })

        val currentRow = map.findCurrentRow()
        val rowIdx = map.indexOf(currentRow)
        val columnIdx = currentRow.findCurrentColumn()

        return Position(rowIdx, columnIdx)
    }

    private fun walkedOutOfMap(position: Position): Boolean {
        val (row, column) = position
        return row !in rowMinIdx - 1..<rowMaxIdx || column !in columnMinIdx - 1..<columnMaxIdx
    }

    enum class Field {
        Obstacle,
        Path,
        Guard;

        fun cantMove() = this !in moveableFields()

        companion object {
            fun moveableFields() = listOf(Path, Guard)
            fun mapFromChar(char: Char): Field = when (char) {
                '#' -> Obstacle
                '.' -> Path
                '^' -> Guard
                else -> throw IllegalArgumentException("Invalid character '$char'")
            }
        }
    }
}

enum class Direction {
    NORTH, SOUTH, EAST, WEST;

    fun change(): Direction = when (this) {
        NORTH -> EAST
        EAST -> SOUTH
        SOUTH -> WEST
        WEST -> NORTH
    }
}

data class Position(val row: Int, val column: Int) {
    fun findNextPosition(direction: Direction): Position = when (direction) {
        Direction.NORTH -> Position(row - 1, column)
        Direction.SOUTH -> Position(row + 1, column)
        Direction.WEST -> Position(row, column - 1)
        Direction.EAST -> Position(row, column + 1)
    }
}
