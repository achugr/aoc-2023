enum class SetType(val score: Int) {
    FIVE(7), FOUR(6), FULL_HOUSE(5), THREE(4), TWO_PAIR(3), ONE_PAIR(2), HIGH_CARD(1);
}

enum class Card(val score: Int) {
    A(12), K(11), Q(10), J(9), T(8), _9(7), _8(6), _7(5), _6(4), _5(3), _4(2), _3(1), _2(0), JOKER(-1);

    companion object {
        fun fromSymbol(char: Char): Card {
            return when (char) {
                'A' -> A
                'K' -> K
                'Q' -> Q
                'J' -> J
                'T' -> T
                '9' -> _9
                '8' -> _8
                '7' -> _7
                '6' -> _6
                '5' -> _5
                '4' -> _4
                '3' -> _3
                '2' -> _2
                else -> throw IllegalArgumentException("Invalid card symbol")
            }
        }

        fun fromSymbolPart2(char: Char): Card {
            if (char == 'J') {
                return JOKER
            }
            return fromSymbol(char)
        }

        fun comparator(): Comparator<Card> {
            return Comparator.comparingInt(Card::score)
        }
    }
}

fun main() {

    data class CardSet(val type: SetType, val cards: List<Card>, val bid: Int) : Comparable<CardSet> {

        override fun compareTo(other: CardSet): Int {
            return Comparator.comparingInt<CardSet> { set -> set.type.score }
                .thenComparator { a, b -> compareCards(a, b) }
                .compare(this, other)
        }

        private fun compareCards(a: CardSet, b: CardSet) = a.cards.zip(b.cards)
            .firstOrNull { cardPair -> Card.comparator().compare(cardPair.first, cardPair.second) != 0 }
            ?.let { pair ->
                Card.comparator().compare(pair.first, pair.second)
            } ?: 0
    }

    fun part1(input: List<String>): Int {
        val result = input.map { it.split("\\s+".toRegex()) }.map { line -> Pair(line[0], line[1]) }.map { pair ->
            val letters = pair.first.groupingBy { it }.eachCount()
            var type: SetType? = null
            val counts = letters.values.sorted().reversed().toList()
            if (counts == listOf(5)) {
                type = SetType.FIVE
            } else if (counts == listOf(4, 1)) {
                type = SetType.FOUR
            } else if (counts == listOf(3, 2)) {
                type = SetType.FULL_HOUSE
            } else if (counts == listOf(3, 1, 1)) {
                type = SetType.THREE
            } else if (counts == listOf(2, 2, 1)) {
                type = SetType.TWO_PAIR
            } else if (counts == listOf(2, 1, 1, 1)) {
                type = SetType.ONE_PAIR
            } else if (counts == listOf(1, 1, 1, 1, 1)) {
                type = SetType.HIGH_CARD
            }
            CardSet(
                type!!, pair.first.toCharArray().map { Card.fromSymbol(it) }.toList(), pair.second.toInt()
            )
        }.sorted()
        return result.mapIndexed { idx, cardSet -> (idx + 1) * cardSet.bid }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map { it.split("\\s+".toRegex()) }.map { line -> Pair(line[0], line[1]) }.map { pair ->
            val letters = pair.first.groupingBy { it }.eachCount()
            val counts = letters.values.sorted().reversed().toList()
            val jokers = pair.first.count { it == 'J' }
            val type = when (counts) {
                listOf(5) -> SetType.FIVE
                listOf(4, 1) -> if (jokers == 1 || jokers == 4) SetType.FIVE else SetType.FOUR
                listOf(3, 2) -> if (jokers == 2 || jokers == 3) SetType.FIVE else SetType.FULL_HOUSE
                listOf(3, 1, 1) -> if (jokers == 1 || jokers == 3) SetType.FOUR else SetType.THREE
                listOf(2, 2, 1) -> when (jokers) {
                    1 -> SetType.FULL_HOUSE
                    2 -> SetType.FOUR
                    else -> SetType.TWO_PAIR
                }

                listOf(2, 1, 1, 1) -> if (jokers == 2 || jokers == 1) SetType.THREE else SetType.ONE_PAIR
                listOf(1, 1, 1, 1, 1) -> if (jokers == 1) SetType.ONE_PAIR else SetType.HIGH_CARD
                else -> throw IllegalArgumentException("Unknown combination")
            }
            CardSet(
                type, pair.first.toCharArray().map { Card.fromSymbolPart2(it) }.toList(), pair.second.toInt()
            )
        }
            .sorted()
            .mapIndexed { idx, cardSet -> (idx + 1) * cardSet.bid }
            .sum()
    }

    val testInput = readInput("Day07_test")
    val result = part1(testInput)
    check(result == 6440)

    val resultPart2 = part2(testInput)
    check(resultPart2 == 5905)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()

}