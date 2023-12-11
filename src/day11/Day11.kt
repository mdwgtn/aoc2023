package day11

import readInput
import testFileName
import kotlin.math.max
import kotlin.math.min

fun main() {
    val day = "Day11"
    val test01Expected = 374L
    val test02Expected = 82000210L

    fun part1(input: List<String>): Long = ExpandedGalaxy(input, 2).distanceSum

    fun part2(input: List<String>): Long = ExpandedGalaxy(input, 1000000).distanceSum

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class ExpandedGalaxy(input: List<String>, expansionFactor: Int) {
    private val galaxy = input

    private fun galaxyMapColumnIsEmpty(col: Int): Boolean = galaxy.all { it[col] == '.' }
    private fun galaxyMapRowIsEmpty(row: Int): Boolean = galaxy[row].all { it == '.' }

    private fun emptyGalaxyMapColumns(): List<Int> =
        (0 until galaxy[0].length).mapIndexedNotNull { index, _ -> if (galaxyMapColumnIsEmpty(index)) index else null }

    private fun emptyGalaxyMapRows(): List<Int> =
        galaxy.indices.mapIndexedNotNull { index, _ -> if (galaxyMapRowIsEmpty(index)) index else null }

    private val galaxyIndices = galaxy.flatMapIndexed { rowIndex, row ->
        row.mapIndexedNotNull { colIndex, char ->
            if (char == '#') Pair(rowIndex, colIndex) else null
        }.map { Location(it.first, it.second) }
    }

    val distanceSum =
        PairwiseDistanceSum(galaxyIndices, expansionFactor, emptyGalaxyMapRows(), emptyGalaxyMapColumns()).distanceSum

}

class PairwiseDistanceSum(
    locations: List<Location>,
    expansionFactor: Int,
    emptyGalaxyMapRows: List<Int>,
    emptyGalaxyMapColumns: List<Int>
) {
    val distanceSum = distanceSum(locations, expansionFactor, emptyGalaxyMapRows, emptyGalaxyMapColumns)

    private fun distanceSum(
        locations: List<Location>,
        expansionFactor: Int,
        emptyGalaxyMapRows: List<Int>,
        emptyGalaxyMapColumns: List<Int>
    ): Long {
        var sum = 0L
        for (lhs in 0 until locations.size - 1) {
            for (rhs in lhs + 1 until locations.size) {
                sum += locations[lhs].distanceFrom(
                    locations[rhs],
                    expansionFactor,
                    emptyGalaxyMapRows,
                    emptyGalaxyMapColumns
                )
            }
        }
        return sum
    }
}

class Location(val row: Int, val col: Int) {
    fun distanceFrom(that: Location, expansionFactor: Int, emptyRows: List<Int>, emptyColumns: List<Int>): Int {
        val leftColumn: Int = min(col, that.col)
        val rightColumn: Int = max(col, that.col)
        val topRow: Int = min(row, that.row)
        val bottomRow: Int = max(row, that.row)
        val expandedColumnCount = emptyColumns.intersect(leftColumn..rightColumn).size
        val expandedRowCount = emptyRows.intersect(topRow..bottomRow).size

        return (rightColumn - leftColumn + (expansionFactor - 1) * expandedColumnCount) +
                (bottomRow - topRow + (expansionFactor - 1) * expandedRowCount)
    }
}
