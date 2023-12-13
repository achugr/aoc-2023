import kotlin.math.abs

fun main() {
    data class Coordinate(val x: Int, val y: Int) {
        fun manhattanDist(coordinate: Coordinate): Int {
            return abs(x - coordinate.x) + abs(y - coordinate.y)
        }
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray().toMutableList() }.toMutableList()
        val xOffsets = grid
                .fold(Triple(0, 0, mutableMapOf<Int, Int>())) { acc, row ->
                    val extraOffset = if (row.all { it == '.' }) 2 else 1
                    val offset = acc.second + extraOffset
                    acc.third[acc.first] = offset
                    Triple(acc.first + 1, offset, acc.third)
                }.third.toMap()
        val yOffsets = grid.indices.map { i -> grid.indices.map { j -> grid[j][i] } }
                .fold(Triple(0, 0, mutableMapOf<Int, Int>())) { acc, row ->
                    val extraOffset = if (row.all { it == '.' }) 2 else 1
                    val offset = acc.second + extraOffset
                    acc.third[acc.first] = offset
                    Triple(acc.first + 1, offset, acc.third)
                }.third.toMap()
        val coordinates = input.flatMapIndexed { rowId, row ->
            row
                    .mapIndexed { colId, col ->
                        Pair(Coordinate(rowId, colId), col)
                    }
                    .filter { it.second == '#' }
                    .map { it.first }
                    .map { Coordinate(xOffsets[it.x]!!, yOffsets[it.y]!!) }
        }
        return coordinates.indices.flatMap { i ->
            (i + 1 until coordinates.size)
                    .map { j -> coordinates[i].manhattanDist(coordinates[j]) }
        }.sum()
    }

    fun part2(input: List<String>): Long {
        val grid = input.map { it.toCharArray().toMutableList() }.toMutableList()
        val xOffsets = grid
                .fold(Triple(0, 0, mutableMapOf<Int, Int>())) { acc, row ->
                    val extraOffset = if (row.all { it == '.' }) 1000000 else 1
                    val offset = acc.second + extraOffset
                    acc.third[acc.first] = offset
                    Triple(acc.first + 1, offset, acc.third)
                }.third.toMap()
        val yOffsets = grid.indices.map { i -> grid.indices.map { j -> grid[j][i] } }
                .fold(Triple(0, 0, mutableMapOf<Int, Int>())) { acc, row ->
                    val extraOffset = if (row.all { it == '.' }) 1000000 else 1
                    val offset = acc.second + extraOffset
                    acc.third[acc.first] = offset
                    Triple(acc.first + 1, offset, acc.third)
                }.third.toMap()
        val coordinates = input.flatMapIndexed { rowId, row ->
            row
                    .mapIndexed { colId, col ->
                        Pair(Coordinate(rowId, colId), col)
                    }
                    .filter { it.second == '#' }
                    .map { it.first }
                    .map { Coordinate(xOffsets[it.x]!!, yOffsets[it.y]!!) }
        }
        return coordinates.indices.flatMap { i ->
            (i + 1 until coordinates.size)
                    .map { j -> coordinates[i].manhattanDist(coordinates[j]).toLong() }
        }.sum()
    }

    val testInput = readInput("Day11_test")
    val result = part1(testInput)
    check(result == 374)

    val result2 = part2(testInput)
//    check(result2 == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
