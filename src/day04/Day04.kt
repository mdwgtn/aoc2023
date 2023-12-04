package day04

import leftOfColon
import leftOfPipe
import readInput
import rightOfColon
import rightOfPipe
import test
import whitespaceToComma

fun main() {
    val day = "Day04"
    val test01Expected = 13
    val test02Expected = 30

    fun part1(input: List<String>): Int = input.sumOf { GameCard(it).points }

    fun part2(input: List<String>): Int = GameCardHand(input).totalCards

    test(::part1, day, 1, test01Expected)
    test(::part2, day, 2, test02Expected)

    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}


class GameCardHand(input: List<String>) {

    private val cardCount = IntArray(input.size) { 1 }
    private val cards = input.map { GameCard(it) }.sortedBy { it.gameNumber }
    val totalCards = totalCards()

    private fun totalCards(): Int {
        var sum = 0
        for (i in cardCount.indices) {
            for (j in i + 1..i + cards[i].matchCount) {
                cardCount[j] += cardCount[i]
            }
            sum += cardCount[i]
        }
        return sum
    }
}

class GameCard(input: String) {

    val gameNumber = input.leftOfPipe().leftOfColon().split(' ')[1]
    private val winningNumbers = input.leftOfPipe().rightOfColon().whitespaceToComma().split(',').map { it.trim() }.toSet()
    private val actualNumbers = input.rightOfPipe().whitespaceToComma().split(',').map { it.trim() }.toSet()
    private val matchingNumbers = winningNumbers.filter { actualNumbers.contains(it) }.toSet()
    val points = if (matchingNumbers.isEmpty()) 0 else (1 shl (matchingNumbers.size - 1))
    val matchCount = matchingNumbers.size
}