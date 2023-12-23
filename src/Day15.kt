import java.util.LinkedList
import kotlin.math.max

fun main() {

    fun hash(str: String): Int {
        return str.toCharArray().toList()
            .fold(0) { acc, e ->
                ((acc + e.code) * 17) % 256
            }
    }

    fun part1(input: String): Int {
        return input.split(",").sumOf { hash(it) }
    }

    data class Lens(val label: String, val focalLength: Int)

    fun part2(input: String): Int {
        val map = mutableMapOf<Int, LinkedList<Lens>>()
        input.split(",").forEach { entry ->
            val parts = entry.split(("[-=]".toRegex()))
            val label = parts[0]
            val hash = hash(label)
            val code = entry[max(entry.indexOf('-'), entry.indexOf('='))]
            if (map[hash] == null) {
                map[hash] = LinkedList()
            }
            val lenses = map[hash]!!
            if (code == '=') {
                val focalLength = parts[1].toInt()
                val lens = Lens(label, focalLength)
                val exists = lenses.any { it.label == label }
                if (exists) {
                    val toReplace = lenses.indexOfFirst { it.label == label }
                    lenses[toReplace] = lens
                } else {
                    lenses.add(lens)
                }
            } else if (code == '-') {
                lenses.removeIf { it.label == label }
            }
        }
        return map.entries.sumOf { entry ->
            entry.value.mapIndexed { idx, lens ->
                (entry.key + 1) * (idx + 1) * lens.focalLength
            }.sum()
        }
    }

    println(hash("qp"))

    val testInput = readInputText("Day15_test")
    val result = part1(testInput)
    check(result == 1320)
//
    val testPart2 = part2(testInput)
    check(testPart2 == 145)
//
    val input = readInputText("Day15")
    part1(input).println()
    part2(input).println()
}
