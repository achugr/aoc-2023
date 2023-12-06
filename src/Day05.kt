fun main() {

    data class Transition(val from: Long, val to: Long, val range: Long) {
        fun isApplicable(number: Long): Boolean = number in (from until from + range)
        fun transition(number: Long): Long {
            return (number - from) + to
        }
    }

    data class Transitions(val transitions: List<Transition>) {
        fun transition(number: Long): Long {
            return transitions.firstOrNull { it.isApplicable(number) }
                ?.transition(number)
                ?: number
        }
    }

    fun part1(input: List<String>): Long {
        val seeds = input.get(0).split(":")[1].trim().split(" ").map { it.toLong() }
        val steps = mutableListOf<Transitions>()
        var transitions = mutableListOf<Transition>()
        input.drop(1)
            .filter { it.isNotBlank() }
            .forEach {
                if (it.endsWith("map:")) {
                    if (transitions.isNotEmpty()) {
                        steps.add(Transitions(transitions))
                    }
                    transitions = mutableListOf()
                } else {
                    val source = it.split(" ")[1].toLong()
                    val target = it.split(" ")[0].toLong()
                    val range = it.split(" ")[2].toLong()
                    transitions.add(Transition(source, target, range))
                }
            }
        steps.add(Transitions(transitions))
        val result = seeds
            .map { seed ->
                val location = steps.fold(seed) { acc, transitions ->
                    transitions.transition(acc)
                }
                location
            }
            .sorted()
            .toList()
        return result.min()
    }

    fun part2(input: List<String>): Long {
        val steps = mutableListOf<Transitions>()
        var transitions = mutableListOf<Transition>()
        input.drop(1)
            .filter { it.isNotBlank() }
            .forEach {
                if (it.endsWith("map:")) {
                    if (transitions.isNotEmpty()) {
                        steps.add(Transitions(transitions))
                    }
                    transitions = mutableListOf()
                } else {
                    val source = it.split(" ")[1].toLong()
                    val target = it.split(" ")[0].toLong()
                    val range = it.split(" ")[2].toLong()
                    transitions.add(Transition(source, target, range))
                }
            }
        steps.add(Transitions(transitions))

//        to figure out how long to wait for it
        val total = input.get(0).split(":")[1].trim().split(" ").map { it.toLong() }
            .windowed(size = 2, step = 2)
            .map { it[1] }
            .fold(0L) { acc, el -> acc + el }

        return input.get(0).split(":")[1].trim().split(" ").map { it.toLong() }
            .windowed(size = 2, step = 2)
            .map { Pair(it[0], it[1]) }
//            make it lazy so that it does not exit by memory
            .asSequence()
            .flatMap { pair -> (pair.first until pair.first + pair.second) }
            .mapIndexed { idx, seed ->
                val next = steps.fold(seed) { acc, transitions ->
                    transitions.transition(acc)
                }
                if (idx % 1_000_000 == 0) {
                    println("processed $idx of $total")
                }
                next
            }
            .min()
    }

    val testInput = readInput("Day05_test")
    val result = part1(testInput)
    check(result == 35L)

    val resultPart2 = part2(testInput)
    check(resultPart2 == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()

}