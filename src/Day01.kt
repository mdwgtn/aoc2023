

fun main() {

    val day = "Day01"
    val test_01 = day + "_1_test"
    val test_02 = day + "_2_test"

    val test_01_expected = 1
    val test_02_expected = 2

    fun part1(input: List<String>): Int = test_01_expected

    fun part2(input: List<String>): Int = test_02_expected

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput(test_01)
    val testInput2 = readInput(test_02)
    check(part1(testInput1) == test_01_expected)
    check(part2(testInput2) == test_02_expected)

    val input = readInput(day)
    part1(input).println()
    part2(input).println()
}

