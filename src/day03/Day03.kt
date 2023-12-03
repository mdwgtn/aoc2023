package day03

import readInput
import test

fun main() {
    val day = "Day03"
    val test01Expected = 4361
    val test02Expected = 467835

    fun part1(input: List<String>): Int = EngineSchematic(input).validPartsSum

    fun part2(input: List<String>): Int = EngineSchematic(input).gearRatioSum

    test(::part1, day, 1, test01Expected)
    test(::part2, day, 2, test02Expected)

    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

data class Position(val x: Int, val y: Int)

data class Part(val partNo: String, val position: Position) {
    fun hasNeighbourOf(otherPosition: Position): Boolean = (
            (otherPosition.x >= (position.x - 1)) &&
                    (otherPosition.x <= (position.x + partNo.length)) &&
                    (otherPosition.y >= (position.y - 1)) &&
                    (otherPosition.y <= (position.y + 1))
            )
}

class EngineSchematic(input: List<String>) {

    private val parts: List<Part> = input
            .mapIndexed { i, it -> EngineSchematicLine(it, i) }
            .flatMap { it.parts }

    private val symbols: List<Position> = input
            .mapIndexed { i, it -> EngineSchematicLine(it, i) }
            .flatMap { it.symbols }

    private val validParts: List<Part> = parts
            .filter { part ->
                symbols.any { symbol -> part.hasNeighbourOf(symbol) }
            }

    private val gears: List<Gear> = input
            .mapIndexed { i, it -> EngineSchematicLine(it, i) }
            .flatMap { it.gears }

    val validPartsSum = validParts.sumOf { it.partNo.toInt() }
    val gearRatioSum = gears.sumOf { it.gearRatio(validParts) }
}

class Gear(private val position: Position) {

    fun gearRatio(parts: List<Part>): Int {
        val neighbouringParts = parts.filter { it.hasNeighbourOf(position) }
        return if (neighbouringParts.size == 2)
            (neighbouringParts.map { it.partNo.toInt() }.reduce { acc, next -> acc * next })
        else 0
    }
}

class EngineSchematicLine(input: String, lineNumber: Int) {

    val parts = Regex("\\d+")
            .findAll(input.trim())
            .map { match -> match.range.first to match.value }
            .map { Part(it.second, Position(it.first, lineNumber)) }
            .toList()

    val symbols = input.trim().withIndex()
            .filter { !it.value.isDigit() && it.value != '.' }
            .map { Position(it.index, lineNumber) }

    val gears = input.trim().withIndex()
            .filter { it.value == '*' }
            .map { Gear(Position(it.index, lineNumber)) }
}