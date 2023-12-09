fun main() {
    fun parsePatterns(input: List<String>): List<List<Long>> =
        input.map { pattern -> pattern.split(" ").map { it.toLong() } }

    fun List<Long>.predictNext(): Long {
        if (this.all { it == 0.toLong() }) {
            return 0

        }
        return this.last() + this.zipWithNext { first, second -> second - first }.predictNext()
    }

    fun List<Long>.predictPrevious(): Long {
        if (this.all { it == 0.toLong() }) {
            return 0

        }
        return this.first() - this.zipWithNext { first, second -> second - first }.predictPrevious()
    }

    fun part1(input: List<String>) = parsePatterns(input).also { println(it) }.sumOf { it.predictNext() }

    fun part2(input: List<String>) = parsePatterns(input).also { println(it) }.sumOf { it.predictPrevious() }


    val input = readInput("src/2023/input/Day09")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}