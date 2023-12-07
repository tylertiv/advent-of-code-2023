import com.sun.javafx.collections.MappingChange
import kotlin.math.pow
import kotlin.math.max
import kotlin.math.min
fun main() {
    data class Mapping(val from: Long, val to: Long, val count: Long) {
        fun contains(value: Long) = value in from..<from + count
        fun contains(range: LongRange) = range.intersect(from..<from+count).isNotEmpty()

        //        Mapping from = 1, to = 7, count = 3
//        1, 2, 3 --> 7, 8, 9
        fun map(value: Long) = to + (value - from)
        fun map(range: LongRange): LongRange {
            val delta = to - from
            return max(range.first, from)+delta..<min(range.last, from+count-1)+delta
        }
    }

    data class ResourceMap(val mapTo: String, val mappings: List<Mapping>)

    fun findStartingSeeds(input: String): Set<Long> {
        val arr = input.split("\n\n")[0]
        return """\d+""".toRegex().findAll(arr).map { it.value.toLong() }.toSet()
    }

    fun findStartingSeedPairs(input: String): Map<Long, Long> {
        val seedsText = input.split("\n\n")[0]
        val x = seedsText.split(' ')
        return x.slice(1..<x.size).chunked(2).associate { it[0].toLong() to it[1].toLong() }
    }

    fun parseAlmanac(input: String): Map<String, ResourceMap> {
        val arr = input.split("\n\n")
        return arr.slice(1..<arr.size).map { it.split("\n") }.associate {
            val (fromResource, toResource) = """(?<from>\w+)-to-(?<to>\w+)""".toRegex()
                .find(it[0])?.groups?.toList()?.slice(1..2)!!
                .map { it?.value }
//                println("from: $fromResource to: $toResource")

            val mapping = it.slice(1..<it.size).map {
                val (to, from, count) = it.split(' ').map { it.toLong() }
//                println("$to $from $count")
                Mapping(from, to, count)
            }

            fromResource!! to ResourceMap(toResource!!, mapping)
        }
    }

    fun part1(input: String): Long {
        val startingSeeds = findStartingSeeds(input)
        val almanac = parseAlmanac(input)
        return startingSeeds.map {
            var resource: String? = "seed"
            var id = it
            do {
                var flag = false
                almanac[resource]?.mappings?.forEach { mapping ->
                    if (!flag && mapping.contains(id)) {
//                        println("mapped $id to ${mapping.map(id)}")
                        id = mapping.map(id)
                        flag = true
                    }
                }
//                println("$resource done")
                resource = almanac[resource]?.mapTo
            } while (almanac[resource] != null)
//            println("location: $id\n")
            id
        }.min()
    }

    fun part2(input: String): Long {
        val startingSeeds = findStartingSeedPairs(input)
        val almanac = parseAlmanac(input)
        return startingSeeds.map {
            var resource: String? = "seed"
            val (id, count) = it
            var x = id..<id + count
            do {
                var flag = false
                almanac[resource]?.mappings?.forEach { mapping ->
//                    println("${mapping.from}..${mapping.from + mapping.count - 1}   $x")
                    if (!flag && mapping.contains(x)) {
//                        println("mapped $x to ${mapping.map(x)}")
                        x = mapping.map(x)
                        flag = true
                    }
                }
//                println("$resource done")
                resource = almanac[resource]?.mapTo
            } while (almanac[resource] != null)
//            println("location: $x\n")
            x
        }.minOf { it.first }
    }

    val input = read("src/2023/input/Day05")
    println("Part 1 output — ${part1(input)}")
    println("Part 2 output — ${part2(input)}")
}