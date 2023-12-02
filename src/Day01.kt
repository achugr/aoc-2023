fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { str ->
            val first = str.first { it in '0'..'9' }.digitToInt()
            val last = str.last { it in '0'..'9' }.digitToInt()
            "${first}${last}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val one = "one"
        val two = "two"
        val three = "three"
        val four = "four"
        val five = "five"
        val six = "six"
        val seven = "seven"
        val eight = "eight"
        val nine = "nine"
        val numbers = listOf(one, two, three, four, five, six, seven, eight, nine)
        val forwardRegexp = """(${numbers.joinToString("|")})""".toRegex()
        val backwardRegexp = """(${numbers.joinToString("|") { it.reversed() }})""".toRegex()
        val wordToDigit = mapOf(
            one to "1",
            two to "2",
            three to "3",
            four to "4",
            five to "5",
            six to "6",
            seven to "7",
            eight to "8",
            nine to "9",
            "" to ""
        )
        val reversedWordToDigit = wordToDigit.map { it.key.reversed() to it.value }.toMap()
        val modifiedInput = input
            .map {
                it.replace("oneight", "oneeight")
                    .replace("eightwo", "eighttwo")
                    .replace("nineight", "nineeight")
                    .replace("twone", "twoone")
                    .replace("threeight", "threeeight")
                    .replace("fiveight", "fiveeight")
            }
            .map {
                forwardRegexp.find(it)?.let { match ->
                    val digit = wordToDigit[match.value]!!
                    it.replaceFirst(match.value, digit)
                } ?: it
            }
            .map { it.reversed() }
            .map {
                backwardRegexp.find(it)?.let { match ->
                    val digit = reversedWordToDigit[match.value]!!
                    it.replaceFirst(match.value, digit)
                } ?: it
            }
            .map { it.reversed() }
        return part1(modifiedInput)
    }

    val testInput = readInput("Day01_test")
    val result = part1(testInput)
    check(result == 142)

    val testPart2Input = readInput("Day01_part2_test")
    val resultPart2 = part2(testPart2Input)
    check(resultPart2 == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}