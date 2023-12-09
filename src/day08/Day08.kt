package day08

import lcmult
import readInput
import testFileName
import toParagraphs

fun main() {
    val day = "Day08"
    val test01Expected = 2
    val test02Expected = 6L

    fun part1(input: List<String>): Int = NavigableNetwork(input).stepsToEnd
    fun part2(input: List<String>): Long = NavigableNetwork(input).ghostCycles.lcm

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput("day08/Day08_2_test")) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class NavigableNetwork(input: List<String>) {
    private var instructionsText = input.toParagraphs()[0][0]
    private var instructions = LRInstructions(instructionsText)
    private var nodePattern = Regex("[0-9a-zA-Z]+")
    private var nodeDescriptions = input.toParagraphs()[1]
        .map {
            nodePattern.findAll(it)
                .map { n -> n.value }
                .toList()
        }
    private var nodes = NavigableNetworkNodes(nodeDescriptions)

    var stepsToEnd = nodes.journeyLength(instructions)

    val ghostCycles = GhostCycles(nodes, instructionsText)

}

class GhostCycles(nodes: NavigableNetworkNodes, instructionsText: String) {
    private val startNodes = nodes.ghostStartNodes
    private var cycles = startNodes.map { GhostCycle(it.value, nodes, instructionsText) }
    var lcm = (cycles.map { it.cycleLength }).lcmult()
}

class GhostCycle(startNode: NavigableNetworkNode, nodes: NavigableNetworkNodes, instructionsText: String) {
    var cycleLength: Int = cycleLength(startNode, nodes, LRInstructions(instructionsText))

    private fun cycleLength(
        startNode: NavigableNetworkNode,
        nodes: NavigableNetworkNodes,
        instructions: LRInstructions
    ): Int {
        var currentNode = startNode
        while (!currentNode.isEnd || instructions.totalInstructionsFollowed == 0) {
            currentNode = nodes.nextNode(currentNode, instructions)
        }
        return instructions.totalInstructionsFollowed
    }
}

class LRInstructions(input: String) {
    private val instructions = input
    private var nextPos: Int = 0
    val nextInstruction: Char
        get() = instructions[nextPos++ % instructions.length]
    val totalInstructionsFollowed: Int
        get() = nextPos
}

class NavigableNetworkNodes(input: List<List<String>>) {
    private var nodes = input
        .map { NavigableNetworkNode(it[0], it[1], it[2]) }
        .associateBy { it.name }

    private var startNode = nodes["AAA"]
    private var endNode = nodes["ZZZ"]
    var ghostStartNodes = nodes.filter { it.value.isStart }
    fun journeyLength(instructions: LRInstructions): Int {
        var currentNode = startNode
        while (currentNode != endNode) {
            currentNode = nextNode(currentNode!!, instructions)
        }
        return instructions.totalInstructionsFollowed
    }

    fun nextNode(currentNode: NavigableNetworkNode, instructions: LRInstructions) =
        nodes[currentNode.nextNodeName(instructions.nextInstruction)]!!
}

data class NavigableNetworkNode(val name: String, val left: String, val right: String) {
    val isStart = name.endsWith('A')
    val isEnd = name.endsWith('Z')
    fun nextNodeName(dir: Char) = if (dir == 'L') left else right
}
