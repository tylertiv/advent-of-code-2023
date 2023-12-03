fun main() {
    data class Pull(val red: Int, val blue: Int, val green: Int)
    data class Game(val id: Int, val pulls: List<Pull>)

    fun getColorCount(string: String, color: String): Int {
        val matchResult = """(\d+) $color""".toRegex().find(string) ?: return 0
        return matchResult.groups[1]?.value?.toInt() ?: 0
    }

    fun parseGame(gameText: String) = Game(
        "Game (\\d+): ".toRegex().find(gameText)?.groups?.get(1)?.value?.toInt() ?: 0,
        gameText
            .replace("Game (\\d+): ".toRegex(), "")
            .split("; ")
            .map {
                Pull(
                    getColorCount(it, "red"),
                    getColorCount(it, "blue"),
                    getColorCount(it, "green")
                )
            }
    )

    val maxColors = object {
        val red = 12
        val green = 13
        val blue = 14
    }

    fun isValidGame(game: Game): Boolean =
        game.pulls.none { it.red > maxColors.red || it.blue > maxColors.blue || it.green > maxColors.green }

    fun part1(input: List<String>): Int =
        input
            .map { parseGame(it) }  // List<String> -> List<Game>
            .filter { isValidGame(it) }
            .sumOf { game: Game -> game.id }


    fun calculatePower(game: Game): Int {
        val minRedNeeded = game.pulls.maxOf { it.red }
        val minGreenNeeded = game.pulls.maxOf { it.green }
        val minBlueNeeded = game.pulls.maxOf { it.blue }

        return minGreenNeeded * minRedNeeded * minBlueNeeded
    }

    fun part2(input: List<String>): Int =
        input
            .map { parseGame(it) }
            .sumOf { calculatePower(it) }

    val input = readInput("src/2023/input/Day02")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}