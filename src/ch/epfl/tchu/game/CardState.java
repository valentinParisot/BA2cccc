package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Objects.checkIndex;

/**
 * CardState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class CardState extends PublicCardState {

    //----------------------------------------------------------------------------------------------------

    /**
     * factory constructor
     * the 5 cards placed face up are the first 5 of the given pile,
     * the draw pile consists of the remaining cards of the pile, and the discard pile is empty
     *
     * @param deck the deck
     * @return new card state
     * @throws IllegalArgumentException if the size of the deck is less than 5
     */

    public static CardState of(Deck<Card> deck) {

        Preconditions.checkArgument(deck.size() >= Constants.FACE_UP_CARDS_COUNT);

        List<Card> listTopCards = deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList();

        return (new CardState(deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT), new SortedBag.Builder<Card>().build(), listTopCards));
    }

    //----------------------------------------------------------------------------------------------------

    private final Deck<Card> deck;
    private final SortedBag<Card> discard;

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     *
     * @param deck        the deck
     * @param discard     the discard sorted
     * @param faceUpCards list of facUpCards
     */

    private CardState(Deck<Card> deck, SortedBag<Card> discard, List<Card> faceUpCards) {

        super(faceUpCards, deck.size(), discard.size());
        this.deck = deck;
        this.discard = discard;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * set of cards identical to the receiver,
     * except that the face-up index slot card has been replaced by the one at the top of the draw pile,
     * which is removed at the same time
     *
     * @param slot index of the card
     * @return new card state
     * @throws IllegalArgumentException if the deck is empty or the index slot is not < 5 or >= 0
     */

    public CardState withDrawnFaceUpCard(int slot) {

        Preconditions.checkArgument(!deck.isEmpty());

        ArrayList<Card> newFaceUpCard = new ArrayList<>(faceUpCards());
        newFaceUpCard.set(checkIndex(slot, 5), deck.topCard());

        return new CardState(deck.withoutTopCard(), discard, newFaceUpCard);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return top card in the deck
     * @throws IllegalArgumentException if the deck is empty
     */

    public Card topDeckCard() {

        Preconditions.checkArgument(!deck.isEmpty());

        return deck.topCard();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * create the same card state without the top deck card
     *
     * @return new card state
     * @throws IllegalArgumentException is the deck is empty
     */

    public CardState withoutTopDeckCard() {

        Preconditions.checkArgument(!deck.isEmpty());

        return new CardState(deck.withoutTopCard(), this.discard, faceUpCards());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * create a new card state with new deck shuffle and an empty discard
     *
     * @param rng random
     * @return new card state
     * @throws IllegalArgumentException is the deck is empty
     */

    public CardState withDeckRecreatedFromDiscards(Random rng) {

        Preconditions.checkArgument(deck.isEmpty());

        return new CardState(Deck.of(discard, rng), SortedBag.of(), faceUpCards());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * create a new card state with additional cards in the discard
     *
     * @param additionalDiscards new cards for the discard
     * @return new card state
     */

    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards) {

        SortedBag<Card> newDiscard = discard.union(additionalDiscards);

        return new CardState(this.deck, newDiscard, faceUpCards());
    }

    //----------------------------------------------------------------------------------------------------
}
