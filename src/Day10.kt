import Direction.*
import java.util.PriorityQueue

enum class Direction(val row: Int, val col: Int) {
    UP(-1, 0), RIGHT(0, 1), DOWN(1, 0), LEFT(0, -1);

    fun opposite(): Direction {
        return when (this) {
            RIGHT -> LEFT
            LEFT -> RIGHT
            UP -> DOWN
            DOWN -> UP
        }
    }

    fun turn90(): Pair<Direction, Direction> {
        return when (this) {
            UP, DOWN -> Pair(LEFT, RIGHT)
            RIGHT, LEFT -> Pair(UP, DOWN)
        }
    }
}

enum class ConnectorType(val code: Char, val d1: Direction, val d2: Direction) {
    NS('|', UP, DOWN),
    EW('-', LEFT, RIGHT),
    NE('L', UP, RIGHT),
    NW('J', UP, LEFT),
    SW('7', LEFT, DOWN),
    SE('F', RIGHT, DOWN);

    fun opposite(direction: Direction): Direction {
        return if (d1 == direction) d2 else d1
    }

    companion object {
        fun from(code: Char): ConnectorType? {
            return entries.firstOrNull { it.code == code }
        }
    }
}

fun main() {

    data class Node(
        val row: Int,
        val col: Int,
        val inputDirection: Direction? = null,
        val type: ConnectorType? = null,
        val depth: Int,
        val path: List<Node> = listOf()
    ) : Comparable<Node> {
        override fun compareTo(other: Node): Int {
            return depth.compareTo(other.depth)
        }

        override fun toString(): String {
            return "Node(row=$row, col=$col, inputDirection=$inputDirection, type=$type, depth=$depth)"
        }
    }

    fun transitionAllowed(node: Node, data: List<List<Char>>, direction: Direction): Boolean {
        return when (direction) {
            UP -> node.row > 0
            RIGHT -> node.col < data[0].size - 1
            DOWN -> node.row < data.size - 1
            LEFT -> node.col > 0
        }
    }

    fun next(node: Node, direction: Direction, data: List<List<Char>>): Node {
        val row = node.row + direction.row
        val col = node.col + direction.col
        val path = node.path.toMutableList()
        path.add(node)
        return Node(row, col, direction.opposite(), ConnectorType.from(data[row][col]), node.depth + 1, path = path)
    }

    fun getPath(data: List<List<Char>>, start: Node): List<Node> {
        val queue = PriorityQueue<Node>()
        Direction.entries
            .filter { direction ->
                when (direction) {
                    UP -> start.row > 0
                    RIGHT -> start.col < data[0].size - 1
                    DOWN -> start.row < data.size - 1
                    LEFT -> start.col > 0
                }
            }
            .map { direction -> next(start, direction, data) }
            .filter { it.type != null }
            .filter { it.inputDirection == it.type!!.d1 || it.inputDirection == it.type.d2 }
            .forEach(queue::add)
        while (!queue.isEmpty()) {
            val node = queue.poll()
            if (node.row == start.row && node.col == start.col) {
                return node.path
            }
            if (node.type == null) {
                continue
            }
            val freeDirection = if (node.inputDirection == node.type.d1) node.type.d2 else node.type.d1
            if (transitionAllowed(node, data, freeDirection)) {
                val next = next(node, freeDirection, data)
                queue.add(next)
            }
        }
        return listOf()
    }


    fun getStartNode(data: List<List<Char>>): Node {
        return data.flatMapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, cell ->
                Pair(colIdx, cell)
            }
                .filter { it.second == 'S' }
                .map { Pair(rowIdx, it.first) }
        }.first().let {
            Node(row = it.first, col = it.second, depth = 0)
        }
    }

    fun part1(input: List<String>): Int {
        val data = input.map { it.toCharArray().toList() }
        val start = getStartNode(data)

        val path = getPath(data, start)
        return path.size / 2
    }


    fun part2(input: List<String>): Int {
        val data = input.map { it.toCharArray().toList() }
        val startNode = getStartNode(data)

        val path = getPath(data, startNode)
        val dataMarked = data.map { row -> row.map { Pair(it, false) }.toMutableList() }
        path.forEach { node ->
            dataMarked[node.row][node.col] = Pair(dataMarked[node.row][node.col].first, true)
        }
//        it's a mess
        val dir1 = path[1].inputDirection!!.opposite()
        val dir2 = path.last().type!!.opposite(path.last().inputDirection!!).opposite()
        val startType = ConnectorType.entries.find {
            it.d1 == dir1 && it.d2 == dir2 || it.d1 == dir2 && it.d2 == dir1
        }
        dataMarked[startNode.row][startNode.col] = Pair(startType!!.code, true)
        var counter = 0
        for (i in dataMarked.indices) {
            var isInside = false
            for (j in dataMarked[i].indices) {
                val cell = dataMarked[i][j]
                if (cell.second) {
                    if (cell.first == '|' || cell.first == 'F' || cell.first == '7') {
                        isInside = !isInside
                    }
                }
                if (!cell.second && isInside) {
                    counter++
                }
            }
        }
        return counter
    }

    val testInput = readInput("Day10_test")
    val result = part1(testInput)
    check(result == 4)

    val testInput2 = readInput("Day10_test_2")
    val result2 = part1(testInput2)
    check(result2 == 8)

    check(part2(readInput("Day10_part2_test")) == 4)
    check(part2(readInput("Day10_part2_test_1")) == 4)
    check(part2(readInput("Day10_part2_test_2")) == 8)
    check(part2(readInput("Day10_part2_test_3")) == 10)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
