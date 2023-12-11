package day11

import readInput
import testFileName
import kotlin.math.abs

fun main() {
    val day = "Day11"
    val test01Expected = 374
    val test02Expected = 2

    fun part1(input: List<String>): Int = ExpandedGalaxy(input).distanceSum

    fun part2(input: List<String>): Int = test02Expected

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class ExpandedGalaxy(input: List<String>) {
    private val unexpandedGalaxy = input
    val expandedGalaxy = expandGalaxy()

    private fun galaxyMapColumnIsEmpty(col: Int): Boolean = unexpandedGalaxy.all { it[col] == '.'}
    private fun galaxyMapRowIsEmpty(row: Int): Boolean = unexpandedGalaxy[row].all{ it == '.' }
    private fun widenGalaxyMapColumn(galaxy: List<String>, col:Int): List<String> = galaxy.map { it.slice(0 until col) + "." + it.slice(col until it.length) }
    private fun widenGalaxyMapRow(galaxy: List<String>, row: Int): List<String> = galaxy.slice(0 until row) + ".".repeat(galaxy[0].length) + galaxy.slice(row until galaxy.size)

    private fun emptyGalaxyMapColumns(): List<Int> =  (0 until unexpandedGalaxy[0].length).mapIndexedNotNull{ index, _ -> if (galaxyMapColumnIsEmpty(index)) index else null}
    private fun emptyGalaxyMapRows(): List<Int> = unexpandedGalaxy.indices.mapIndexedNotNull { index, _ -> if (galaxyMapRowIsEmpty(index)) index else null }
    private fun expandGalaxy(): List<String> {
        val emptyColumns = emptyGalaxyMapColumns()
        val emptyRows = emptyGalaxyMapRows()
        var result = unexpandedGalaxy
        for ((doneSoFar, row) in emptyRows.withIndex()) {
            result = widenGalaxyMapRow(result, row + doneSoFar)
        }
        for((doneSoFar, col) in emptyColumns.withIndex()) {
            result = widenGalaxyMapColumn(result, col + doneSoFar)
        }
        println(result)
        return result
    }

    val galaxyIndices = expandedGalaxy.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, char ->
            if (char == '#') Pair(rowIndex, colIndex) else null
        }.map { Location(it.first, it.second)}
    }

    val distanceSum = PairwiseDistanceSum(galaxyIndices).distanceSum

}

class PairwiseDistanceSum(val locations: List<Location>) {
    val distanceSum = distanceSum()

    private fun distanceSum(): Int {
        var sum = 0
        for (lhs in 0 until locations.size - 1) {
            for (rhs in lhs + 1 until locations.size) {
                sum += locations[lhs].distanceFrom(locations[rhs])
            }
        }
        return sum
    }
}


class Location(val row: Int, val col: Int) {
    fun distanceFrom(that: Location): Int  = abs(row - that.row) + abs(col - that.col)
    override fun toString(): String {
        return "Location(row=$row, col=$col)"
    }

}
