package day06

import invertNTimesNPlusOne
import isEven
import readInput
import removeAllSpaces
import rightOfColon
import testFileName
import tokeniseBySpaces
import kotlin.math.ceil
import kotlin.math.sqrt

fun main() {
    val day = "Day06"
    val test01Expected = 288L
    val test02Expected = 71503L

    fun part1(input: List<String>): Long = Races(input).productOfWaysToWin
    fun part2(input: List<String>): Long = Races(input).productOfBigWayToWin

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class Races(input: List<String>) {
    private val cleanedInput = input.map { it.rightOfColon() }
    private val time = cleanedInput[0].tokeniseBySpaces().map { it.toLong() }
    private val distance = cleanedInput[1].tokeniseBySpaces().map { it.toLong() }
    private val races = time.mapIndexed { index, i -> Race(i, distance[index]) }
    private val bigTime = cleanedInput[0].removeAllSpaces().toLong()
    private val bigDistance = cleanedInput[1].removeAllSpaces().toLong()

    val productOfWaysToWin = races
            .map { it.waysToWin }
            .reduce { acc, i -> acc * i }
    val productOfBigWayToWin = Race(bigTime, bigDistance).waysToWin
}

class Race(time: Long, distanceToBeat: Long) {
    private val maxDistance = if (time.isEven()) ((time / 2) * (time / 2)) else ((time / 2) * (time / 2 + 1))
    private val spread = maxDistance - distanceToBeat
    val waysToWin = if (time.isEven())
        (2L * ceil(sqrt(spread.toDouble())).toLong() - 1L)
    else
        (2L * invertNTimesNPlusOne(spread))
}
