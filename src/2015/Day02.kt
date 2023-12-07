package `2015`

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf {
            val (length, width, height) = it.split('x').map { it.toInt() }.sorted()
            (2 * length * width) + (2 * length * height) + (2 * width * height) + length * width
        }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf {
            val (length, width, height) = it.split('x').map { it.toInt() }.sorted()
            2*length + 2*width+ length*width*height
        }
    }

    val input = readInput("src/2020/input/Day02")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}
