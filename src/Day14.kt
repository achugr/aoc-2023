fun main() {

    fun transpose(grid: List<List<Char>>): List<List<Char>> {
        return (0 until grid[0].size)
            .map { colIndex ->
                grid.mapNotNull { row ->
                    row.getOrElse(colIndex) { null }
                }
            }
    }

    fun part1(input: List<String>): Int {
        return transpose(input.map { it.toCharArray().toList() }).sumOf {
            it.foldIndexed(mutableListOf<Pair<Int, Char>>()) { idx, acc, e ->
                if (e == 'O') {
                    val last = acc.lastOrNull()?.first?.inc() ?: 0
                    acc.add(Pair(last, e))
                } else if (e == '#') {
                    acc.add(Pair(idx, e))
                }
                acc
            }.sumOf { positionAndChar ->
                if (positionAndChar.second == 'O') {
                    (input.size - positionAndChar.first)
                } else {
                    0
                }
            }
        }
    }

    fun move(row: List<Char>): List<Char> {
        val positions = row.foldIndexed(mutableListOf<Pair<Int, Char>>()) { idx, acc, e ->
            if (e == 'O') {
                val last = acc.lastOrNull()?.first?.inc() ?: 0
                acc.add(Pair(last, e))
            } else if (e == '#') {
                acc.add(Pair(idx, e))
            }
            acc
        }.associateBy { pair -> pair.first }
        return row.indices.map { idx ->
            val value = positions[idx]
            value?.second ?: '.'
        }
    }

    fun cycle(input: List<List<Char>>): List<List<Char>> {
        var result = input
        repeat(4) {
            result = transpose(result).map { row -> move(row).reversed() }
        }
        return result
    }

    fun part2(input: List<String>): Int {
        val source = input.map { it.toCharArray().toList() }
        var result = source
        val iterations = 1000000000
        var idx = 0
        val met = mutableMapOf<List<List<Char>>, Int>()
        while (!met.contains(result)) {
            met[result] = idx++
            result = cycle(result)
        }
        val beginIdx = met.get(result)!!
        val periodLength = met.size - beginIdx
        val iterationsToFinish = (iterations - beginIdx) - ((iterations - beginIdx) / periodLength) * periodLength
        repeat(iterationsToFinish) {
            result = cycle(result)
        }

        return transpose(result).sumOf { row ->
            row.mapIndexed { idx, ch ->
                if (ch == 'O') {
                    row.size - idx
                } else {
                    0
                }
            }.sum()
        }
    }

    val testInput = readInput("Day14_test")
    val result = part1(testInput)
    check(result == 136)

    val testPart2 = part2(testInput)
    check(testPart2 == 64)

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
