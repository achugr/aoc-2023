fun main() {

    fun part1(input: List<String>): Int {
        val condition = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )
        val regex = "(\\d+) (red|green|blue)".toRegex()
        return input.map { line ->
            line.split(":")[1]
        }.map { game ->
            game.split(";")
        }.map { round ->
            round.all {
                regex.findAll(it)
                    .all { match ->
                        val color = match.groups.get(2)!!.value
                        val amount = match.groups.get(1)!!.value.toInt()
                        condition[color]!! >= amount
                    }
            }
        }.mapIndexed { idx, fits ->
            if (fits) idx + 1 else 0
        }.sum()
    }

    fun part2(input: List<String>): Int {
        val regex = "(\\d+) (red|green|blue)".toRegex()
        return input.sumOf { line ->
            line.split(":")[1]
                .split(";")
                .flatMap {
                    regex.findAll(it)
                        .map { match ->
                            val color = match.groups.get(2)!!.value
                            val amount = match.groups.get(1)!!.value.toInt()
                            Pair(color, amount)
                        }
                }
                .groupingBy { pair -> pair.first }
                .fold(0) { max, e -> if (e.second > max) e.second else max }
                .entries
                .fold(1) { mul, e -> mul * e.value }
                .toInt()
        }
    }

    val testInput = readInput("Day02_test")
    val result = part1(testInput)
    check(result == 8)

    val testPart2Input = readInput("Day02_test")
    val resultPart2 = part2(testPart2Input)
    check(resultPart2 == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}