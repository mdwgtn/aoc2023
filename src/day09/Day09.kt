package day09

import readInput
import testFileName
import tokeniseBySpaces

fun main() {
    val day = "Day09"
    val test01Expected = 114
    val test02Expected = 2

    fun part1(input: List<String>): Int = Histories(input).predictionProduct
    fun part2(input: List<String>): Int = Histories(input).postdictionProduct

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class Histories(input: List<String>) {
    private val historyPredictions = input.map { History(it) }
    val predictionProduct = historyPredictions.toList().sumOf { it.prediction }
    val postdictionProduct = historyPredictions.toList().sumOf { it.postdiction }
}

class History(val input: List<Int>) {
    constructor(input: String): this(input.tokeniseBySpaces().map { it.toInt() })
    private val differences = input.zipWithNext{ a, b -> b - a }
    private val allZero = differences.all { it == 0 }
    private val child: History? = if (allZero) null else History(differences)
    val prediction: Int = input.last() + (child?.prediction ?: 0)
    val postdiction: Int = input.first() - (child?.postdiction ?: 0)
}
