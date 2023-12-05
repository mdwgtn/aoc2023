package day05

import readInput
import rightOfColon
import testFileName
import toParagraphs

fun main() {
    val day = "Day05"
    val test01Expected = 35L
    val test02Expected = 46L


    fun part1(input: List<String>): Long = Almanac(input).leastSeedLocation
    fun part2(input: List<String>): Long = Almanac(input).leastSeedLocationInRanges()

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)

    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}


class Almanac(input: List<String>) {

    private val paragraphs = input.toParagraphs()
    private val seeds = paragraphs[0][0].rightOfColon().split(' ').map { it.trim().toLong() }
    private val steps = paragraphs.drop(1)
    private val almanacMappings = steps.map { AlmanacMapping(it.drop(1)) }
    private val seedLocations = seeds.map { locationOfSeed(it) }
    private val seedRanges = seeds.indices.step(2).map { SeedRange(seeds[it], seeds[it + 1]) }
    private val reversedAlmanacMappings = almanacMappings.asReversed()
    private fun seedForLocation(location: Long): Long =
            reversedAlmanacMappings.fold(location) { y, mapping -> mapping.reverseMappingOf(y) }
    private fun locationOfSeed(seed: Long): Long =
            almanacMappings.fold(seed) { location, mapping -> mapping.mappingOf(location) }

    val leastSeedLocation = seedLocations.min()

    fun leastSeedLocationInRanges(): Long {
        var location = 0L
        while (true) {
            if (seedRanges.any { it.contains(seedForLocation(location)) }) {
                return location
            }
            location++
        }
    }
}

class AlmanacMapping(mappingInput: List<String>) {

    private val almanacRanges = mappingInput.map { AlmanacRange(it) }

    fun mappingOf(x: Long): Long = almanacRanges.firstOrNull { it.mappingOf(x) != null }?.mappingOf(x) ?: x
    fun reverseMappingOf(y: Long): Long = almanacRanges.firstOrNull { it.reverseMappingOf(y) != null }?.reverseMappingOf(y) ?: y
}

class SeedRange(private val start: Long, private val length: Long) {
    fun contains(x: Long): Boolean = x >= start && x < start + length
}

class AlmanacRange(input: String) {
    private val splitInput = input.split(' ').map { it.trim() }
    private val domainFirst = splitInput[1].toLong()
    private val rangeFirst = splitInput[0].toLong()
    private val rangeSize = splitInput[2].toLong()
    private val domainLast = domainFirst + rangeSize - 1
    private val rangeLast = rangeFirst + rangeSize - 1

    fun mappingOf(x: Long): Long? = if ((domainFirst..domainLast).contains(x)) (rangeFirst + x - domainFirst) else null
    fun reverseMappingOf(y: Long): Long? = if ((rangeFirst..rangeLast).contains(y)) (domainFirst + y - rangeFirst) else null
}
