fun main() {
    data class Race(val duration: Long, val record: Long) {
        fun waysToWin(): Long =
            (0..duration)
                .map { speed ->
                    speed * (duration - speed)
                }.count { it > record }.toLong()

        fun waysToWinEfficient(): Long {
            var i = 0
            while (i * (duration - i) < record) i++
            return duration - 2 * i + 1
        }

    }

    fun parseRace(input: List<String>): List<Race> =
        input[0].drop(5).trim().split(" +".toRegex()).zip(input[1].drop(9).trim().split(" +".toRegex())).map {
            Race(it.first.toLong(), it.second.toLong())
        }

    fun parseSingleRace(input: List<String>): Race {
        val duration = input[0].drop(5).trim().split(" +".toRegex()).joinToString("") { it }.toLong()
        val record = input[1].drop(9).trim().split(" +".toRegex()).joinToString("") { it }.toLong()
        return Race(duration, record)
    }

    fun part1(input: List<String>): Long {
        return parseRace(input).map {
            it.waysToWin()
        }.reduce { acc, it -> acc * it }
    }

    fun part2(input: List<String>): Long {
        return parseSingleRace(input).waysToWinEfficient()
    }

    val input = readInput("src/2023/input/Day06")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}