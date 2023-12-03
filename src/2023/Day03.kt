fun main() {

    data class Part(val number: Int, val range: IntRange, val lineNum: Int)

    data class Gear(val row: Int, val col: Int, val adjacentParts: MutableList<Part> = mutableListOf()) {
        fun addAdjacentPart(part: Part) {
            this.adjacentParts.add(part)
        }
    }

    data class Blueprint(
        val blueprintText: List<String>,
        val parts: List<Part>,
        val potentialGears: MutableList<Gear> = mutableListOf()
    ) {
        fun addPotentialGear(gear: Gear) = this.potentialGears.add(gear)
    }

    fun Blueprint.performGearCheck(part: Part) {
        println("part ${part.number}")
        val potentialGearsTop =
            """\*""".toRegex().findAll(blueprintText.getOrNull(part.lineNum - 1)?.substring(part.range) ?: "")
        potentialGearsTop.forEach { star ->
            val gearRow = part.lineNum - 1
            val gearCol = part.range.first + star.range.first
            val findGear = this.potentialGears.find { it.row == gearRow && it.col == gearCol }
            if (findGear != null) {
                findGear.addAdjacentPart(part)
            } else {
                this.potentialGears.add(Gear(gearRow, gearCol, mutableListOf(part)))
            }
        }

        val potentialGearsBottom =
            """\*""".toRegex().findAll(blueprintText.getOrNull(part.lineNum + 1)?.substring(part.range) ?: "")
        potentialGearsBottom.forEach { star ->
            val gearRow = part.lineNum + 1
            val gearCol = part.range.first + star.range.first
            val findGear = this.potentialGears.find { it.row == gearRow && it.col == gearCol }
            if (findGear != null) {
                findGear.addAdjacentPart(part)
            } else {
                this.potentialGears.add(Gear(gearRow, gearCol, mutableListOf(part)))
            }
        }

        // gears left
        blueprintText.subList(
            if (part.lineNum - 1 >= 0) part.lineNum - 1 else 0,
            if (part.lineNum + 1 < blueprintText.size) part.lineNum + 2 else blueprintText.size
        )
            .mapNotNull { it.getOrNull(part.range.first - 1) }
            .forEachIndexed { index, c ->
                if (c == '*') {
                    var gearRow = part.lineNum + index - 1
                    val gearCol = part.range.first - 1
                    if (part.lineNum == 0) gearRow++
                    println("gear left: col$gearCol row$gearRow")
                    val findGear = this.potentialGears.find { it.row == gearRow && it.col == gearCol }
                    if (findGear != null) {
                        findGear.addAdjacentPart(part)
                    } else {
                        this.potentialGears.add(Gear(gearRow, gearCol, mutableListOf(part)))
                    }
                }
            }
        // gears right
        blueprintText.subList(
            if (part.lineNum - 1 >= 0) part.lineNum - 1 else 0,
            if (part.lineNum + 1 < blueprintText.size) part.lineNum + 2 else blueprintText.size
        )
            .mapNotNull { it.getOrNull(part.range.last + 1) }
            .forEachIndexed { index, c ->
                if (c == '*') {
                    var gearRow = part.lineNum + index - 1
                    if (part.lineNum == 0) gearRow++
                    val gearCol = part.range.last + 1
                    println("gear right: col$gearCol row$gearRow")
                    val findGear = this.potentialGears.find { it.row == gearRow && it.col == gearCol }
                    if (findGear != null) {
                        findGear.addAdjacentPart(part)
                    } else {
                        this.potentialGears.add(Gear(gearRow, gearCol, mutableListOf(part)))
                    }
                }
            }
    }

    fun Blueprint.performPartCheck(part: Part): Boolean {
        val topSymbol =
            blueprintText.getOrNull(part.lineNum - 1)?.substring(part.range)?.any { !(it == '.' || it.isDigit()) }
                ?: false
        val bottomSymbol =
            blueprintText.getOrNull(part.lineNum + 1)?.substring(part.range)?.any { !(it == '.' || it.isDigit()) }
                ?: false
        val leftSymbol =
            blueprintText.subList(
                if (part.lineNum - 1 >= 0) part.lineNum - 1 else 0,
                if (part.lineNum + 1 < blueprintText.size - 1) part.lineNum + 2 else blueprintText.size - 1
            ).mapNotNull { it.getOrNull(part.range.first - 1) }.any { !(it == '.' || it.isDigit()) }
        val rightSymbol =
            blueprintText.subList(
                if (part.lineNum - 1 >= 0) part.lineNum - 1 else 0,
                if (part.lineNum + 1 < blueprintText.size - 1) part.lineNum + 2 else blueprintText.size - 1
            ).mapNotNull { it.getOrNull(part.range.last + 1) }.any { !(it == '.' || it.isDigit()) }

        return topSymbol || bottomSymbol || leftSymbol || rightSymbol
    }

    fun Blueprint.checkValidPart(part: Part): Boolean {
        return performPartCheck(part)
    }

    fun parseBlueprint(blueprintText: List<String>): Blueprint =
        Blueprint(
            blueprintText,
            blueprintText.flatMapIndexed { lineNum, lineText ->
                val numbers = """\d+""".toRegex().findAll(lineText)
                numbers
                    .map {
                        Part(it.value.toInt(), it.range, lineNum)
                    }
            })

    fun part1(input: List<String>): Int {
        val blueprint: Blueprint = parseBlueprint(input)
        return blueprint.parts.filter { blueprint.checkValidPart(it) }.sumOf { it.number }
    }

    fun part2(input: List<String>): Int {
        val blueprint: Blueprint = parseBlueprint(input)
        blueprint.parts.map { blueprint.performGearCheck(it)}
        return blueprint.potentialGears
            .filter { it.adjacentParts.size == 2 }.sumOf {
                it.adjacentParts.fold(1) { acc, part -> acc * part.number }.toInt()
            }
    }

    val input = readInput("src/2023/input/Day03")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}