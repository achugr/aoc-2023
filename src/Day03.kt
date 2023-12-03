fun main() {
    val lookups =
        listOf(Pair(1, 1), Pair(1, 0), Pair(1, -1), Pair(0, -1), Pair(-1, -1), Pair(-1, 0), Pair(-1, 1), Pair(0, 1))

    data class Cell(val row: Int, val col: Int, val value: Char) {
        fun isDigit(): Boolean {
            return value in '0'..'9'
        }

        fun isSymbol(): Boolean {
            return !isDigit() && value != '.'
        }

        fun isAsterisk(): Boolean {
            return value == '*'
        }
    }

    fun getNumber(cell: Cell, row: List<Cell>): Pair<Int, Int> {
        var idx = cell.col
        while (idx - 1 >= 0 && row[idx - 1].isDigit()) {
            idx--
        }
        val startIdx = idx
        val chars = mutableListOf<Char>()
        while (idx < row.size && row[idx].isDigit()) {
            chars.add(row[idx++].value)
        }
        val number = String(chars.toCharArray()).toInt()
        return Pair(startIdx, number)
    }

    data class ReachableNumber(val row: Int, val startPos: Int, val number: Int)

    fun part1(input: List<String>): Int {
        val inputMatrix = input.mapIndexed { rowIdx, row ->
            row.toCharArray().mapIndexed { colIdx, cell -> Cell(rowIdx, colIdx, cell) }
        }
        return inputMatrix.flatMap { row ->
            row
                .filter { cell -> cell.isDigit() }
                .filter { cell ->
                    lookups
                        .filter { lookup ->
                            (cell.row + lookup.first) in inputMatrix.indices
                                    && (cell.col + lookup.second) in inputMatrix[0].indices
                        }
                        .any { lookup -> inputMatrix[cell.row + lookup.first][cell.col + lookup.second].isSymbol() }
                }
                .map { cell ->
                    val (startIdx, number) = getNumber(cell, row)
                    ReachableNumber(cell.row, startIdx, number)
                }
        }.toSet()
            .sumOf { it.number }
    }

    data class AsteriskReachableNumber(val row: Int, val startPos: Int, val number: Int, val asterisk: Cell)

    fun part2(input: List<String>): Int {
        val inputMatrix = input.mapIndexed { rowIdx, row ->
            row.toCharArray().mapIndexed { colIdx, cell -> Cell(rowIdx, colIdx, cell) }
        }
        return inputMatrix.asSequence().flatMap { row ->
            row
                .filter { cell -> cell.isDigit() }
                .flatMap { cell ->
                    lookups
                        .filter { lookup ->
                            (cell.row + lookup.first) in inputMatrix.indices
                                    && (cell.col + lookup.second) in inputMatrix[0].indices
                        }
                        .filter { lookup -> inputMatrix[cell.row + lookup.first][cell.col + lookup.second].isAsterisk() }
                        .map { lookup ->
                            val (startIdx, number) = getNumber(cell, row)
                            AsteriskReachableNumber(
                                cell.row,
                                startIdx,
                                number,
                                inputMatrix[cell.row + lookup.first][cell.col + lookup.second]
                            )
                        }
                }
        }.toSet().groupBy({ it.asterisk }, { it.number })
            .filter { it.value.size == 2 }
            .map { it.value.fold(1) { acc, i -> acc * i } }
            .sum()

    }

    val testInput = readInput("Day03_test")
    val result = part1(testInput)
    check(result == 4361)

    val resultPart2 = part2(testInput)
    println(resultPart2)
    check(resultPart2 == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()

}