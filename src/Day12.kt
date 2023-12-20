import kotlin.math.min

fun main() {

    val cache = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun solveRec(sequence: String, encodings: List<Int>): Long {
        cache[Pair(sequence, encodings)]?.let { return it }
        if (encodings.isEmpty()) {
            return if (sequence.contains("#")) 0 else 1
        }
        if (sequence.isEmpty()) {
            return if (encodings.isEmpty()) 1 else 0
        }
        val encoding = encodings.first()
        if (sequence.length < encoding) {
            return 0
        }
        var result = 0L
        if (sequence.first() == '?' || sequence.first() == '.') {
            result += solveRec(sequence.substring(1), encodings)
        }
        if (sequence.first() == '?' || sequence.first() == '#') {
            if (sequence.indexOf('.') !in (0 until encoding)
                && (sequence.length == encoding || sequence[encoding] != '#')
            ) {
                val subsequence = sequence.substring(min(encoding + 1, sequence.length))
                result += solveRec(subsequence, encodings.drop(1))
            }
        }
        cache[Pair(sequence, encodings)] = result
        return result
    }

    fun part1(input: List<String>): Int {
        return input.map { it.split("\\s".toRegex()) }
            .sumOf {
                cache.clear()
                solveRec(it.first(), it.last().split(",").map { it.toInt() }.toList())
            }.toInt()
    }

    fun part2(input: List<String>): Long {
        return input.map { it.split("\\s".toRegex()) }
            .map { Pair(it.first(), it.last().split(",").map { it.toInt() }.toList()) }
            .map { pair ->
                Pair(
                    (0 until 5).joinToString(separator = "?") { pair.first },
                    generateSequence { pair.second }.take(5).flatten().toList()
                )
            }
            .sumOf { pair ->
                cache.clear()
                solveRec(pair.first, pair.second)
            }
    }

    val testInput = readInput("Day12_test")
    val result = part1(testInput)
    check(result == 21)

    val testPart2 = part2(testInput)
    check(testPart2 == 525152L)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
