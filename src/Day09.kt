fun main() {

    fun recursivePart1(row: List<Int>): Int {
        if (row.all { it == 0 }) {
            return 0
        }
        return row.last() + recursivePart1(row.zipWithNext().map { pair -> pair.second - pair.first })
    }

    fun part1(input: List<String>): Int {
        return input.map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }.sumOf { seq ->
            recursivePart1(seq)
        }
    }

    fun part2(input: List<String>): Int {
        return input.map { line ->
            line.split("\\s+".toRegex()).map { it.toInt() }
        }
            .sumOf { seq ->
                recursivePart1(seq.reversed())
            }
    }

    val testInput = readInput("Day09_test")
    val result = part1(testInput)
    check(result == 114)

    val resultPart2 = part2(testInput)
    println(resultPart2)
    check(resultPart2 == 2)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}