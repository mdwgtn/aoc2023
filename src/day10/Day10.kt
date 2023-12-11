package day10

import padGridWith
import readInput
import testFileName
import kotlin.math.ceil

fun main() {
    val day = "Day10"
    val test01Expected = 8
    val test02Expected = 2

    fun part1(input: List<String>): Int =
        PipeTiles(input).farthestPointInCycle

    fun part2(input: List<String>): Int = test02Expected

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class PipeTiles(input: List<String>) {
    private val paddedInput = input.padGridWith(".")
    private val rowLength = paddedInput[0].length
    private val grid =
        paddedInput.joinToString("").mapIndexed { location, it -> PipeTile(it.toString(), location, rowLength) }

    private val startTile: PipeTile = grid.first { it.isStart }
    private val startTileGoesNorth: Boolean = grid[startTile.locationOfNeighbourToThe(Direction.NORTH)].goesSouth
    private val startTileGoesSouth: Boolean = grid[startTile.locationOfNeighbourToThe(Direction.SOUTH)].goesNorth
    private val startTileGoesEast: Boolean = grid[startTile.locationOfNeighbourToThe(Direction.EAST)].goesWest

    private fun cycleLength(): Int {
        var travelDirection = startTileTravelDirection()
        var currentTile = grid[startTile.locationOfNeighbourToThe(travelDirection)]
        var journeyLength = 0
        while (!currentTile.visited) {
            journeyLength++
            currentTile.visit()
            travelDirection = currentTile.otherExitDirection(travelDirection)
            currentTile = grid[currentTile.locationOfNeighbourToThe(travelDirection)]
        }
        return journeyLength
    }

    private fun startTileTravelDirection(): Direction =
        if (startTileGoesNorth) Direction.NORTH
        else if (startTileGoesSouth) Direction.SOUTH
        else if (startTileGoesEast) Direction.EAST
        else Direction.WEST

    val farthestPointInCycle = ceil(cycleLength().toDouble() / 2).toInt()
}

enum class Direction {
    NORTH, SOUTH, EAST, WEST;

    fun opposite() = when (this) {
        NORTH -> SOUTH
        SOUTH -> NORTH
        EAST -> WEST
        WEST -> EAST
    }
}

data class PipeTile(val glyph: String, val location: Int, val rowLength: Int) {
    val goesNorth = glyph in listOf("|", "J", "L")
    val goesSouth = glyph in listOf("|", "7", "F")
    val goesWest = glyph in listOf("-", "J", "7")
    val isStart = glyph == "S"

    private val exits = when (glyph) {
        "|" -> listOf(Direction.NORTH, Direction.SOUTH)
        "J" -> listOf(Direction.NORTH, Direction.WEST)
        "L" -> listOf(Direction.NORTH, Direction.EAST)
        "7" -> listOf(Direction.WEST, Direction.SOUTH)
        "F" -> listOf(Direction.EAST, Direction.SOUTH)
        "-" -> listOf(Direction.EAST, Direction.WEST)
        else -> listOf()
    }

    fun visit() { visited = true }

    fun otherExitDirection(fromTravelDirection: Direction): Direction =
        exits.first { it != fromTravelDirection.opposite() }

    fun locationOfNeighbourToThe(direction: Direction): Int =
        when (direction) {
            Direction.NORTH -> location - rowLength
            Direction.SOUTH -> location + rowLength
            Direction.EAST -> location + 1
            Direction.WEST -> location - 1
        }

    var visited = isStart
}
