fun main() {
    val regex = "(\\w\\w\\w) = \\((\\w\\w\\w), (\\w\\w\\w)\\)".toRegex()

    fun part1(input: List<String>): Int {
        val commands = input.get(0).toCharArray()
        val routes = input.drop(2)
            .map { line ->
                val groups = regex.matchEntire(line)!!.groups
                Pair(groups[1]!!.value, Pair(groups[2]!!.value, groups[3]!!.value))
            }
            .associateBy({ it.first }, { it.second })
        val commandIdxIterator = generateSequence(0) { commandIdx ->
            (commandIdx + 1) % commands.size
        }.iterator()
        return generateSequence(Pair("AAA", 0)) { prev ->
            val idx = commandIdxIterator.next()
            if (commands[idx] == 'L') {
                Pair(routes[prev.first]!!.first, prev.second + 1)
            } else {
                Pair(routes[prev.first]!!.second, prev.second + 1)
            }
        }
            .takeWhile { "ZZZ" != it.first }
            .last().second + 1
    }

    fun lcm(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun lcm(numbers: List<Long>) = numbers
        .reduce { acc, num ->
            lcm(acc, num)
        }

    fun part2(input: List<String>): Long {
        val commands = input.get(0).toCharArray()
        val routes = input.drop(2)
            .map { line ->
                val groups = regex.matchEntire(line)!!.groups
                Pair(groups[1]!!.value, Pair(groups[2]!!.value, groups[3]!!.value))
            }
            .associateBy({ it.first }, { it.second })
        val commandIdxSeq = generateSequence(0) { commandIdx ->
            (commandIdx + 1) % commands.size
        }
        val initials = routes.keys.filter { it.endsWith("A") }
        return lcm(initials.map {
            val iterator = commandIdxSeq.iterator()
            generateSequence(Pair(it, 0)) { prev ->
                val idx = iterator.next()
                val choice = routes[prev.first]!!
                val route = if (commands[idx] == 'L') choice.first else choice.second
                Pair(route, prev.second + 1)
            }
                .takeWhile { !it.first.endsWith("Z") }
                .last().second + 1L
        })
    }

    val testInput = readInput("Day08_test")
    val result = part1(testInput)
    check(result == 6)

    val resultPart2 = part2(testInput)
    println(resultPart2)
    check(resultPart2 == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()

}