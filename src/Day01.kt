import kotlin.reflect.typeOf

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        "${line.first { it.isDigit() }}${line.last { it.isDigit() }}".toInt()
    }

    /* First pass — Bug in here somewhere...
    (0..9).map { digit ->
        line.indexOf(digit.toString()[0])
    }.filter {
        it >= 0
    }.let {
        listOf(it.min(), it.max())
            .map { line[it].toString() }
            .let { "${it[0]}${it[1]}" }
    }.toInt()
     */

    fun part2(input: List<String>): Int {
        /*
            Can't replace written out number with just the digit due to the edge case of two written out numbers up
            against each other ( oneight should be "18" not 1ght ). Hacky workaround instead leaves first/last digit
            of number for this case.
        */
        val replacements = mapOf(
            "one" to "o1e",
            "two" to "t2o",
            "three" to "t3e",
            "four" to "f4r",
            "five" to "f5e",
            "six" to "s6x",
            "seven" to "s7n",
            "eight" to "e8t",
            "nine" to "n9e",
        )

        return part1(input.map { line ->
            line.uppercase()
            replacements.keys.fold(line) { last, numToReplace ->
                last.replace(numToReplace, replacements[numToReplace]!!)
            }
        })
    }

    val input = readInput("input/Day01")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}
