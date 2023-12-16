fun main() {

    fun findReflection(input: List<List<Char>>): Int {
        val inputList = input.map { row -> row.joinToString(separator = "") }
        return (0 until inputList.size - 1).map { idx ->
            val isMirror = (idx downTo 0).zip(idx + 1 until inputList.size)
                    .none { inputList[it.first] != inputList[it.second] }
            Pair(idx + 1, isMirror)
        }.firstOrNull { it.second }?.first ?: 0
    }

    fun transpose(grid: List<List<Char>>): List<List<Char>> {
        return (0 until grid[0].size)
                .map { colIndex ->
                    grid.mapNotNull { row ->
                        row.getOrElse(colIndex) { null }
                    }
                }
    }

    fun part1(input: String): Int {
        return input.split("\n\n".toRegex())
                .sumOf { pattern ->
                    val grid = pattern.split("\n".toRegex())
                            .map { row -> row.toCharArray().toList() }
                    val horizontalReflection = findReflection(grid)
                    val verticalReflection = findReflection(transpose(grid))
                    horizontalReflection * 100 + verticalReflection
                }
    }

    fun diff(line1: String, line2: String): Int {
        return line1.toCharArray().zip(line2.toCharArray()).count { it.first != it.second }
    }

    fun findReflectionPart2(input: List<List<Char>>): Int {
        val inputList = input.map { row -> row.joinToString(separator = "") }
        return (0 until inputList.size - 1).map { idx ->
            val diff = (idx downTo 0).zip(idx + 1 until inputList.size)
                    .sumOf { diff(inputList[it.first], inputList[it.second]) }
            Pair(idx + 1, diff == 1)
        }.firstOrNull { it.second }?.first ?: 0
    }

    fun part2(input: String): Int {
        return input.split("\n\n".toRegex())
                .sumOf { pattern ->
                    val grid = pattern.split("\n".toRegex())
                            .map { row -> row.toCharArray().toList() }
                    val horizontalReflection = findReflectionPart2(grid)
                    val verticalReflection = findReflectionPart2(transpose(grid))
                    horizontalReflection * 100 + verticalReflection
                }
    }

    val testInput = readInputText("Day13_test")
    val result = part1(testInput)
    check(result == 405)

    val testInput2 = readInputText("Day13_test_2")
    val result2 = part1(testInput2)
    check(result2 == 709)

    val testPart2 = part2(testInput)
    check(testPart2 == 400)

    val input = readInputText("Day13")
    part1(input).println()
    part2(input).println()
}
