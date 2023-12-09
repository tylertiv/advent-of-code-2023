val cards = mapOf(
    'A' to 14,
    '2' to 2,
    '3' to 3,
    '4' to 4,
    '5' to 5,
    '6' to 6,
    '7' to 7,
    '8' to 8,
    '9' to 9,
    'T' to 10,
    'J' to 1,
    'Q' to 12,
    'K' to 13
)

data class Hand(val hand: String, val bet: Int, val score: Int = -1) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        hand.zip(other.hand).forEach {
            if (cards[it.first]!! - cards[it.second]!! != 0) return cards[it.first]!! - cards[it.second]!!
        }
        return 0
    }

    companion object {
        fun create(line: String): Hand {
            val (hand, bet) = line.split(" ")

            val score = this.calcScore(
                cards.map {
                    if(it.key != 'J')
                        it.key.toString().toRegex().findAll(hand).count()
                    else
                        -1
                }.filter { it > 0 }.sortedDescending(),
                hand.count { it == 'J' }
            )
//            println("hand $hand, bet $bet, score $score #J: ${hand.count{it=='J'}}")
            return Hand(hand, bet.toInt(), score)
        }
        private fun calcScore(counts: List<Int>, countJs: Int): Int {
            return if (countJs == 5 || counts[0] + countJs == 5) {
                7                       // 5 of a kind = 7
            } else if (counts[0] + countJs == 4) {
                6                       // 4 of a kind = 6
            } else if (counts[0] + countJs == 3) {
                if (counts[1] == 2) 5   // full house = 5
                else 4                  // 3 of a kind = 4
            } else if (counts[0] + countJs == 2) {
                if (counts[1] == 2) 3   // 2 pair = 3
                else 2                  // 1 pair = 2
            } else 1                    // high card = 1
        }
    }

}

fun main() {
    fun part1(input: List<String>) =
        input.asSequence().map {
            Hand.create(it)
        }.sortedWith(
            compareBy({ it.score }, { it })
        ).onEach { println(it) }.mapIndexed { i, hand -> (i + 1) * (hand.bet) }.sum()

    fun part2(input: List<String>) =
        input.asSequence().map {
            Hand.create(it)
        }.sortedWith(
            compareBy({ it.score }, { it })
        ).onEach { println(it) }.mapIndexed { i, hand -> (i + 1) * hand.bet }.sum()


    val input = readInput("src/2023/input/Day07")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}