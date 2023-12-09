import java.math.BigInteger
import java.security.MessageDigest

fun String.insertBefore(insertMe: Char?, insertMeAt: Int): String =
        substring(0 until insertMeAt) + insertMe + substring(insertMeAt until length)

fun String.firstNumeronym(): String? = numeronyms.keys
        .filter { indexOf(it) >= 0 }
        .minByOrNull { indexOf(it) }

fun String.lastNumeronym(): String? = numeronyms.keys
        .filter { indexOf(it) >= 0 }
        .maxByOrNull { lastIndexOf(it) }

fun String.withFirstNumeronymReplaced(): String =
        firstNumeronym()?.let { firstNumeronym ->
            insertBefore(numeronyms[firstNumeronym], indexOf(firstNumeronym))
        } ?: this

fun String.withLastNumeronymReplaced(): String =
        lastNumeronym()?.let { firstNumeronym ->
            insertBefore(numeronyms[firstNumeronym], lastIndexOf(firstNumeronym))
        } ?: this


fun String.firstDigit(): Int = this[indexOfFirst { it.isDigit() }].digitToInt()
fun String.lastDigit(): Int = this[indexOfLast { it.isDigit() }].digitToInt()

fun String.whitespaceToComma(): String = replace(Regex("\\s+"), ",")
fun String.leftOfPipe(): String = trim().split('|')[0].trim()
fun String.rightOfPipe(): String = trim().split('|')[1].trim()

fun String.leftOfColon(): String = trim().split(':')[0].trim()
fun String.rightOfColon(): String = trim().split(':')[1].trim()

fun String.tokeniseBySpaces(): List<String> = split("\\s+".toRegex())
fun String.removeAllSpaces(): String = replace("\\s".toRegex(), "")

fun String.sorted(): String = toCharArray().sorted().joinToString("")

fun String.maxRunLength(): Int {
    val result = mutableListOf<String>()

    if (isNotEmpty()) {
        var currentGroup = this[0].toString()

        for (i in 1 until length) {
            if (this[i] == this[i - 1]) {
                currentGroup += this[i]
            } else {
                result.add(currentGroup)
                currentGroup = this[i].toString()
            }
        }

        result.add(currentGroup)
    }

    return result.maxOfOrNull { it.length } ?: 0
}

fun List<String>.toParagraphs(): List<List<String>> =
        fold(mutableListOf(mutableListOf<String>())) { acc, line ->
            if (line.isBlank()) {
                acc.add(mutableListOf())
            } else {
                acc.last().add(line)
            }
            acc
        }.filter { it.isNotEmpty() }

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

fun Long.isEven() = this % 2L == 0L

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun List<Int>.lcmult(): Long = this.map{ it.toLong() }.lcm()
fun List<Long>.lcm(): Long = reduce {it, element -> it.lcm(element)}

fun Long.lcm(that: Long) = this * that / this.gcd(that)

fun Long.gcd(that: Long): Long {
    var x = this
    var y = that
    while (y != 0L) {
        val temp = y
        y = x % y
        x = temp
    }
    return x
}
