import Direction.*
import java.util.LinkedList

fun main() {

    data class Node(
        val row: Int,
        val col: Int,
        val direction: Direction,
        val value: Char
    )

    fun solve(
        queue: LinkedList<Node>,
        grid: List<List<Char>>
    ): Int {
        val visited = mutableSetOf<Node>()
        while (!queue.isEmpty()) {
            val node = queue.poll()
            if (!visited.add(node)) {
                continue
            }
            queue.addAll(
                when {
                    node.value == '.'
                            || node.value == '-' && (node.direction == RIGHT || node.direction == LEFT)
                            || node.value == '|' && (node.direction == UP || node.direction == DOWN)
                    -> {
                        val row = node.row + node.direction.row
                        val col = node.col + node.direction.col
                        if (row in grid.indices && col in grid.indices) {
                            val child = Node(row, col, node.direction, grid[row][col])
                            listOf(child)
                        } else {
                            emptyList()
                        }
                    }

                    node.value == '/' && node.direction == RIGHT
                            || node.value == '\\' && node.direction == LEFT -> {
                        val row = node.row + UP.row
                        if (row in grid.indices) {
                            val child = Node(row, node.col, UP, grid[row][node.col])
                            listOf(child)
                        } else {
                            emptyList()
                        }
                    }

                    node.value == '/' && node.direction == DOWN
                            || node.value == '\\' && node.direction == UP -> {
                        val col = node.col + LEFT.col
                        if (col in grid.indices) {
                            val child = Node(node.row, col, LEFT, grid[node.row][col])
                            listOf(child)
                        } else {
                            emptyList()
                        }
                    }

                    node.value == '/' && node.direction == UP
                            || node.value == '\\' && node.direction == DOWN -> {
                        val col = node.col + RIGHT.col
                        if (col in grid.indices) {
                            val child = Node(node.row, col, RIGHT, grid[node.row][col])
                            listOf(child)
                        } else {
                            emptyList()
                        }
                    }

                    node.value == '/' && node.direction == LEFT
                            || node.value == '\\' && node.direction == RIGHT -> {
                        val row = node.row + DOWN.row
                        if (row in grid.indices) {
                            val child = Node(row, node.col, DOWN, grid[row][node.col])
                            listOf(child)
                        } else {
                            emptyList()
                        }
                    }

                    node.value == '-' && (node.direction == UP || node.direction == DOWN) -> {
                        val col1 = node.col + 1
                        val col2 = node.col - 1
                        val children = mutableListOf<Node>()
                        if (col1 in grid.indices) {
                            val child1 = Node(node.row, col1, RIGHT, grid[node.row][col1])
                            children.add(child1)
                        }
                        if (col2 in grid.indices) {
                            val child2 = Node(node.row, col2, LEFT, grid[node.row][col2])
                            children.add(child2)
                        }
                        children
                    }

                    node.value == '|' && (node.direction == LEFT || node.direction == RIGHT) -> {
                        val row1 = node.row + 1
                        val row2 = node.row - 1
                        val children = mutableListOf<Node>()
                        if (row1 in grid.indices) {
                            val child1 = Node(row1, node.col, DOWN, grid[row1][node.col])
                            children.add(child1)
                        }
                        if (row2 in grid.indices) {
                            val child2 = Node(row2, node.col, UP, grid[row2][node.col])
                            children.add(child2)
                        }
                        children
                    }

                    else -> throw IllegalArgumentException("Unknown node type")
                }
            )
        }
        return visited.map { Pair(it.row, it.col) }.toSet().size
    }

    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray().toList() }
        val queue = LinkedList<Node>()
        queue.add(Node(0, 0, RIGHT, grid[0][0]))
        return solve(queue, grid)
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray().toList() }
        return grid.indices.flatMap {
            listOf(
                Node(0, it, DOWN, grid[0][it]),
                Node(grid.size - 1, it, UP, grid.last()[it]),
                Node(it, 0, RIGHT, grid[it][0]),
                Node(it, grid.size - 1, LEFT, grid[it][grid.size - 1]),
            )
        }.map {
            val queue = LinkedList<Node>()
            queue.add(it)
            solve(queue, grid)
        }.max()
    }

    val testInput = readInput("Day16_test")
    val result = part1(testInput)
    check(result == 46)

    val testPart2 = part2(testInput)
    check(testPart2 == 51)

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
