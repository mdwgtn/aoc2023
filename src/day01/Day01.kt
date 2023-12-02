package day01

import firstDigit
import lastDigit
import println
import readInput
import test
import withFirstNumeronymReplaced
import withLastNumeronymReplaced

fun main() {

    fun part1(input: List<String>): Int = input
            .map { CalibrationValue(it).part1Value() }
            .reduce { acc, next -> acc + next }

    fun part2(input: List<String>): Int = input
            .map { CalibrationValue(it).part2Value() }
            .reduce { acc, next -> acc + next }

    val day = "Day01"
    val test01Expected = 142
    val test02Expected = 281

    test(::part1, day, 1, test01Expected)
    test(::part2, day, 2, test02Expected)

    val input = readInput(day + "/" + day)
    println("Part 1 answer\n-------------")
    part1(input).println()
    println("\nPart 2 answer\n-------------")
    part2(input).println()
}

class CalibrationValue(input: String) {

    private var normalized: String

    init {
        normalized = input
                .withFirstNumeronymReplaced()
                .withLastNumeronymReplaced()
    }

    fun part1Value(): Int = normalized.firstDigit() * 10 + normalized.lastDigit();
    fun part2Value(): Int = part1Value()
}