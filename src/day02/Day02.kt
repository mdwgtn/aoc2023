package day02

import println
import readInput
import test

fun main() {

    val day = "Day02"
    val test01Expected = 8
    val test02Expected = 2286

    fun part1(input: List<String>): Int = CubeGames(input).possibleGamesSum(Round("12 red, 14 blue, 13 green"))

    fun part2(input: List<String>): Int = CubeGames(input).power

    test(::part1, day, 1, test01Expected)
    test(::part2, day, 2, test02Expected)

    val input = readInput(day + "/" + day)
    println("Part 1 answer\n-------------")
    part1(input).println()
    println("\nPart 2 answer\n-------------")
    part2(input).println()
}

class CubeGames(input: List<String>) {

    private val games = input.map { CubeGame(it) }
    val power = games.sumOf { it.power }

    private fun possibleGames(claim: Round): List<Int> =
            games.filter { it.isPossible(claim) }
                    .map { it.gameNumber }

    fun possibleGamesSum(claim: Round): Int =
            possibleGames(claim).sum()
}

val colourWords = mapOf("red" to 0, "green" to 1, "blue" to 2)

class CubeGame(input: String) {

    private val inputSplitAtColon = input.split(":")
    val gameNumber = inputSplitAtColon[0].split(' ')[1].toInt()
    private val rounds = inputSplitAtColon[1].split(';').map { Round(it) }
    val power = colourWords.keys
            .map { word -> rounds.maxOf { it.cubesOfColour(word) } }
            .reduce { acc, next -> acc * next }

    fun isPossible(claim: Round): Boolean =
            rounds.all { it.isPossible(claim) }
}

class Round(input: String) {

    val drawings = input.split(',').map { Drawing(it) }

    fun isPossible(claim: Round): Boolean = drawings.all { it.isPossible(claim) }

    fun cubesOfColour(colour: String): Int =
            drawings.filter { it.colour == colourWords[colour] }
                    .map { it.count }.firstOrNull() ?: 0
}

class Drawing(input: String) {
    private val splitInput = input.trim().split(' ')
    val count = splitInput[0].toInt()
    val colour = colourWords[splitInput[1]] ?: -1

    fun isPossible(claim: Round): Boolean =
            claim.drawings
                    .filter { it.colour == colour }
                    .all { it.count >= count }
}