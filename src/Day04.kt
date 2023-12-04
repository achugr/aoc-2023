import java.util.PriorityQueue

fun main() {

    val spaceRegex = "\\s+".toRegex()

    fun part1(input: List<String>): Int {
        return input.map {
            it.split(":")[1]
        }.sumOf { line ->
            val winning = line.split("|")[0].trim().split(spaceRegex)
                .map { it.trim() }
                .toSet()
            line.split("|")[1].trim().split(spaceRegex)
                .map { it.trim() }
                .filter { winning.contains(it) }
                .map { 2 }
                .fold(1) { acc, i -> acc * i }
                .div(2)
        }
    }

    data class Game(val index: Int, val wins: Int)

    fun part2(input: List<String>): Int {
        val sources = input.map {
            it.split(":")[1]
        }.mapIndexed { idx, line ->
            val winning = line.split("|")[0].trim().split(spaceRegex)
                .map { it.trim() }
                .toSet()
            val wins = line.split("|")[1].trim().split(spaceRegex)
                .map { it.trim() }
                .count { winning.contains(it) }
            Game(idx + 1, wins)
        }
        val gameComparator: Comparator<Game> = compareBy { it.index }
        val queue = PriorityQueue(gameComparator)
        queue.addAll(sources)
        var counter = 0
        while (!queue.isEmpty()) {
            val top = queue.poll()
            counter++
            queue.addAll(sources.subList(top.index, top.index + top.wins))
        }
        return counter
    }

    val testInput = readInput("Day04_test")
    val result = part1(testInput)
    check(result == 13)

    val resultPart2 = part2(testInput)
    check(resultPart2 == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()

}