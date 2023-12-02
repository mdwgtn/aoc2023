package day01

import println
import readInput
import test

fun main() {

    val day = "Day02"
    val test01Expected = 1
    val test02Expected = 2

    fun part1(input: List<String>): Int = 1

    fun part2(input: List<String>): Int = 2

    test(::part1, day, 1, test01Expected)
    test(::part2, day, 2, test02Expected)

    val input = readInput(day)
    println("Part 1 answer\n-------------")
    part1(input).println()
    println("\nPart 2 answer\n-------------")
    part2(input).println()
}
