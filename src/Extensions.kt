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

fun String.whitespaceToComma() : String = replace(Regex("\\s+"), ",")
fun String.leftOfPipe() : String = trim().split('|')[0].trim()
fun String.rightOfPipe() : String = trim().split('|')[1].trim()

fun String.leftOfColon(): String = trim().split(':')[0].trim()
fun String.rightOfColon(): String = trim().split(':')[1].trim()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
        .toString(16)
        .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
