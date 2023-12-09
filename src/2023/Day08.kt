fun main() {
    data class Node(val value: String, val left: String, val right: String)
    data class InstructionSet(val instructions: String, val numInstructions: Int, val network: Map<String, Node>) {
        var counter: Int = 0
        fun getNextDir(): Char {
            counter %= numInstructions
            return instructions[counter++]
        }

        fun getNext(current: String): String {
            val dir = this.getNextDir()
            return if (dir == 'L')
                network[current]?.left!!
            else
                network[current]?.right!!
        }
    }

    fun parseInstructions(input: List<String>): InstructionSet {
        val instructions = input.first()
        val networkText = input.takeLast(input.size - 2)
        val network = networkText.associate {
            val (value, routes) = it.split(" = ")
            val (left, right) = routes.slice(1..(routes.length - 2)).split(", ")
            value to Node(value, left, right)
        }
        return InstructionSet(instructions, instructions.length, network)
    }

    fun part1(input: List<String>): Int {
        val instructionSet = parseInstructions(input)
        var current = "AAA"
        var counter = 0
        while (current != "ZZZ") {
            current = instructionSet.getNext(current)
            counter++
        }
        return counter
    }

    // thx geeks4geeks
    fun Long.lcm(b: Long): Long {
        fun gcd(a: Long, b: Long): Long {
            if (a == 0.toLong()) return b;
            return gcd(b % a, a);
        }
        return (this / gcd(this, b)) * b;
    }

    fun part2(input: List<String>): Long {
        val instructionSet = parseInstructions(input)
        val current = instructionSet.network.entries.filter { it.key[2] == 'A' }.map { it.key }
        return current.map {
            var c = it
            var counter = 0
            while (c[2] != 'Z') {
                c = instructionSet.getNext(c)
                counter++
            }
            counter
        }.map { it.toLong() }.reduce { acc, next -> acc.lcm(next) }
    }


    val input = readInput("src/2023/input/Day08")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}