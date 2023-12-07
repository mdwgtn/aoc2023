package day07

import maxRunLength
import readInput
import sorted
import testFileName
import tokeniseBySpaces

fun main() {
    val day = "Day07"
    val test01Expected = 6440
    val test02Expected = 5905

    fun part1(input: List<String>): Int = PokerHands(input).totalWinnings;
    fun part2(input: List<String>): Int = PokerHands(input).totalWinningsConsideringJokers

    check(part1(readInput(testFileName(day))) == test01Expected)
    check(part2(readInput(testFileName(day))) == test02Expected)
    val input = readInput("$day/$day")
    println("Part 1 answer: " + part1(input))
    println("Part 2 answer: " + part2(input))
}

class PokerHands(input: List<String>) {
    private val hands: List<PokerHand> = input.map { PokerHand(it) }

    private val rankings = hands.sortedWith(pokerHandComparator)
    val totalWinnings = rankings.mapIndexed { index, pokerHand -> pokerHand.winnings(index+1) }.sum()

    private val rankingsConsideringJokers = hands.sortedWith(jokerHandComparator)
    val totalWinningsConsideringJokers = rankingsConsideringJokers.mapIndexed { index, pokerHand -> pokerHand.winnings(index+1) }.sum()
}

class PokerHand(input: String) {
    private val unnormalisedCards = input.tokeniseBySpaces()[0].trim()
    private val cardValues = unnormalisedCards.toCharArray().map { PokerCard(it) }
    private val bid = input.tokeniseBySpaces()[1].trim().toInt()
    private val cards = unnormalisedCards.sorted()
    private val uniqueCards = cards.toSet()
    private val maxRunLength = cards.maxRunLength()
    private val isNothing = uniqueCards.size == 5
    private val isPair = uniqueCards.size == 4
    private val isTwoPair = uniqueCards.size == 3 && maxRunLength == 2
    private val isThreeOfAKind = uniqueCards.size == 3 && maxRunLength == 3
    private val isFullHouse = uniqueCards.size == 2 && maxRunLength == 3
    private val isFourOfAKind = uniqueCards.size == 2 && maxRunLength == 4
    private val isFiveOfAKind = uniqueCards.size == 1
    private val rating = listOf(isNothing, isPair, isTwoPair, isThreeOfAKind,
            isFullHouse, isFourOfAKind, isFiveOfAKind).indexOfFirst { it }

    fun winnings(ranking: Int): Int = ranking * bid;

    fun compare(that: PokerHand): Int {
        val comparison = rating - that.rating
        return if (comparison != 0) comparison else beatsWhenResolvingATie(that)
    }

    private fun beatsWhenResolvingATie(that: PokerHand): Int {
        for (i in 0 until 5) {
            val comparison = cardValues[i].compare(that.cardValues[i])
            if (comparison != 0) return comparison
        }
        return 0
    }

    private val cardValuesConsideringJokers = unnormalisedCards.toCharArray().map { JokerCard(it) }
    private val cardsWithoutJokers = cards.replace("J", "")
    private val jokerCount = cards.count{ it == 'J' }
    private val uniqueCardsWithoutJokers = cardsWithoutJokers.toSet()
    private val maxRunLengthWithoutJokers = cardsWithoutJokers.maxRunLength()
    private val isNothingConsideringJokers = isNothing && jokerCount == 0
    private val isPairConsideringJokers = isPair && jokerCount == 0 || uniqueCardsWithoutJokers.size == 4 && jokerCount == 1
    private val isTwoPairConsideringJokers = isTwoPair && jokerCount == 0
    private val isThreeOfAKindConsideringJokers = isThreeOfAKind && jokerCount == 0 || isPair && jokerCount == 1 || uniqueCardsWithoutJokers.size == 3 && jokerCount == 2
    private val isFullHouseConsideringJokers = isFullHouse && jokerCount == 0 || isTwoPair && jokerCount == 1
    private val isFourOfAKindConsideringJokers = isFourOfAKind && jokerCount == 0 || isThreeOfAKind && jokerCount == 1 || uniqueCardsWithoutJokers.size == 2 && jokerCount == 2 || uniqueCardsWithoutJokers.size == 2 && jokerCount == 3
    private val isFiveOfAKindConsideringJokers = uniqueCardsWithoutJokers.isEmpty() ||  uniqueCardsWithoutJokers.size == 1
    private val ratingConsideringJokers = listOf(isNothingConsideringJokers, isPairConsideringJokers,
            isTwoPairConsideringJokers, isThreeOfAKindConsideringJokers, isFullHouseConsideringJokers,
            isFourOfAKindConsideringJokers, isFiveOfAKindConsideringJokers).indexOfFirst { it }

    fun compareConsideringJokers(that: PokerHand): Int {
        val comparison = ratingConsideringJokers - that.ratingConsideringJokers
        return if (comparison != 0) comparison else beatsWhenResolvingATieConsideringJokers(that)
    }

    private fun beatsWhenResolvingATieConsideringJokers(that: PokerHand): Int {
        for (i in 0 until 5) {
            val comparison = cardValuesConsideringJokers[i].compare(that.cardValuesConsideringJokers[i])
            if (comparison != 0) return comparison
        }
        return 0
    }
}

val pokerHandComparator: Comparator<PokerHand> = Comparator{ first, second -> first.compare(second) }
val jokerHandComparator: Comparator<PokerHand> = Comparator{ first, second -> first.compareConsideringJokers(second) }

class PokerCard(card: Char) {
    private val cardValue = when (card) {
        '2', '3', '4', '5', '6', '7', '8', '9' -> card.code - '0'.code
        'T' -> 10
        'J' -> 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> -1
    }

    fun compare(that: PokerCard) = cardValue - that.cardValue
}

class JokerCard(card: Char) {
    private val cardValue = when (card) {
        'J' -> 1
        '2', '3', '4', '5', '6', '7', '8', '9' -> card.code - '0'.code
        'T' -> 10
        'Q' -> 11
        'K' -> 12
        'A' -> 13
        else -> -1
    }

    fun compare(that: JokerCard) = cardValue - that.cardValue
}