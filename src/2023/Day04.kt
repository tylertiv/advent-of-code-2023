import kotlin.math.pow

fun main() {
    data class Scratcher(
        val cardId: Int, val winningNumbers: Set<Int>, val yourNumbers: Set<Int>, var copies: Int = 1
    ) {
        fun points() = (2.0).pow(countWins() - 1).toInt()
        fun countWins() = this.yourNumbers.intersect(this.winningNumbers).size
    }

    fun String.withoutPrefix(prefix: Regex): String {
        return this.substring((prefix.find(this)?.range?.last?.plus(1)) ?: 0)
    }

    fun parseScratcher(gameText: String): Scratcher {
        val id = """Card\s+(\d+):""".toRegex().find(gameText)?.groups?.get(1)?.value?.toInt() ?: -1
        val numberList = gameText.withoutPrefix("""Card\s+(\d+):""".toRegex()).split('|').map { it.trim() }
        val winningSet = numberList[0].split("""\s+""".toRegex()).map { it.toInt() }.toSet()
        val yourSet = numberList[1].split("""\s+""".toRegex()).map { it.toInt() }.toSet()

        return Scratcher(id, winningSet, yourSet)
    }

    fun part1(input: List<String>): Int = input.map { parseScratcher(it) }.sumOf { it.points() }

    fun part2(input: List<String>): Int {
        val scratchers = input.map {
            parseScratcher(it)
        }
        return scratchers.mapIndexed { i, current ->
                if (current.countWins() > 0) for (j in 1..current.countWins()) {
                    val count = scratchers.getOrNull(i + j)?.copies ?: 0
                    scratchers.getOrNull(i + j)?.copies = count + current.copies
                }
                current
            }.sumOf { it.copies }
    }

    val input = readInput("src/2023/input/Day04")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}