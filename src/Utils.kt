import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/input/$name.txt").readLines()

fun testFileName(day: String): String =
        day.lowercase() + "/" + day + "_1_test"

fun test(callMe: (lines: List<String>) -> Int, day: String, testNo: Int, expected: Int) =
        check(callMe(readInput(testFileName(day))) == expected)


val numeronyms = mapOf(
        "one" to '1',
        "two" to '2',
        "three" to '3',
        "four" to '4',
        "five" to '5',
        "six" to '6',
        "seven" to '7',
        "eight" to '8',
        "nine" to '9'
)

fun invertNTimesNPlusOne(takeMeBackToN: Long): Long =
        ceil((-1 + sqrt(1 + 4 * takeMeBackToN.toDouble())) / 2L).toLong()