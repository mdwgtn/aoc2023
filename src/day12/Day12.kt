package day12

import readInput
import test

fun main() {
    val day = "Day12"
    val test01Expected = 21
    val test02Expected = 2

    fun part1(input: List<String>): Int {
        println(SpringSets(input).total); return SpringSets(input).total
    }

    fun part2(input: List<String>): Int = 2

    test(::part1, day, 1, test01Expected)
    test(::part2, day, 2, test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class SpringSets(input: List<String>) {
    private val springSets = input.map { SpringSet(it) }
    val total = springSets.sumOf { it.count }
}

class SpringSet(input: String) {
    private val schematics = SpringSchematic(input.split(" ")[0].trim())
    private val constraints = SpringConstraints(input.split(" ")[1].trim())

    private val candidateInts = schematics.consistentWithPattern

    val count = constraints.filter(candidateInts).size
}

class SpringConstraints(input: String) {
    private val ch = input.split(" ")[0].trim()
    private val constraints = ch.split(",").map{ it.toInt() }.toList().reversed()

    fun filter(candidates: List<Int>): List<Int> = candidates.filter { allowed(it) }

    private fun allowed(candidate: Int): Boolean {
        var checkMe = candidate
        for (constraint in constraints) {
            checkMe = isValid(constraint, checkMe)
            if (checkMe == -1) return false
            if (checkMe % 2 != 0) return false
        }
        if (checkMe != 0) return false
        return true
    }

    companion object {
        fun isValid(filterSize: Int, candidate: Int): Int {

            var current = candidate
            val filter = (1 shl filterSize) - 1

            for (i in 0 until Integer.SIZE - filterSize) {
                if ((current and filter) == filter) {
                    return (current shr filterSize)
                }
                var oneSeen = false
                for (j in 0 until filterSize) {
                    val bit = 1 shr j
                    if ((bit and current) > 0) {
                        oneSeen = true
                    }
                    if (((bit and current) == 0) && oneSeen) {
                        return -1
                    }
                }
                current = (current shr 1)
            }
            return -1
        }
    }
}

class SpringSchematic(schematic: String) {
    private val oneBits = schematic.reversed().mapIndexedNotNull { index, char -> if (char == '#') index else null }
    private val zeroBits = schematic.reversed().mapIndexedNotNull { index, char -> if (char == '.') index else null }
    private val bitLength = schematic.length
    private val maxPossibleValue = (1 shl bitLength) -1

    private var ones = oneBits.fold(0) { acc, it -> acc or (1 shl it) }
    private var zeros = zeroBits.fold(0) { acc, it -> acc or (1 shl it) }

    val consistentWithPattern = consistentWithPattern()

    private fun consistentWithPattern(): List<Int> =
        mutableListOf<Int>().apply {
            for (i in (0 until maxPossibleValue)) {
                if (isConsistentWith(i)) { add(i) }
            }
        }

    private fun isConsistentWith(candidate: Int): Boolean {
        if (candidate > maxPossibleValue)  return false
        if (candidate and ones != ones) return false
        if ((candidate xor Int.MAX_VALUE) and zeros != zeros)  return false
        return true
    }
}