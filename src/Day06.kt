import kotlin.math.*

fun main() {

    data class Equation(val a: Long, val b: Long, val c: Long) {
        fun roots(): Pair<Double, Double> {
            val d = sqrt(b.toDouble() * b - 4 * a * c)
            val r0 = (-b + d) / 2 * a
            val r1 = (-b - d) / 2 * a
            return Pair(r0, r1)
        }
    }

    /**
     * dist = (time-pullTime) * (pullTime)
     * -1 * pullTime ^ 2 + 7 * pullTime - 9 = 0
     * A * pullTime ^ 2 + B * pullTime + C = 0
     */
    fun part1(input: List<String>): Int {
        val times = input.get(0).substringAfter("Time:").trim().split("\\s+".toRegex()).map { it.toLong() }
        val record = input.get(1).substringAfter("Distance:").trim().split("\\s+".toRegex()).map { it.toLong() }
        return times.zip(record)
            .map { (time, recordToBeat) -> Equation(-1, time, -recordToBeat).roots() }
            .map { roots ->
                val left = if (ceil(roots.first) == roots.first) roots.first + 1 else ceil(roots.first)
                val right = if (floor(roots.second) == roots.second) roots.second - 1 else floor(roots.second)
                val result = abs(left - right) + 1
                result.toInt()
            }
            .fold(1) { acc, el -> acc * el }
    }

    fun part2(input: List<String>): Int {
        val time = input.get(0).substringAfter("Time:").replace("\\s+".toRegex(), "").toLong()
        val record = input.get(1).substringAfter("Distance:").replace("\\s+".toRegex(), "").toLong()
        val roots = Equation(-1, time, -record).roots()
        val left = if (ceil(roots.first) == roots.first) roots.first + 1 else ceil(roots.first)
        val right = if (floor(roots.second) == roots.second) roots.second - 1 else floor(roots.second)
        return abs(left - right).toInt() + 1
    }


    val testInput = readInput("Day06_test")
    val result = part1(testInput)
    check(result == 288)

    val resultPart2 = part2(testInput)
    check(resultPart2 == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()

}