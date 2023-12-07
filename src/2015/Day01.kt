package `2015`

import readInput

fun main() {
    fun part1(input: List<String>): Int = input[0].fold(0) { acc, next ->
        if (next == '(') acc + 1 else acc - 1
    }


    fun part2(input: List<String>): Int {
        var floor = 0
        var position = 1
        val chars = ' ' + input[0]
        while (position < input[0].length-1) {
            floor += if (chars[position] == '(') 1 else -1
            if (floor == -1) break
            position++
        }
        return position
    }

    val input = readInput("./input/Day01")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}
