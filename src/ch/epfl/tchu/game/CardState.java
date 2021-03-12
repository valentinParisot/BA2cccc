package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static ch.epfl.tchu.game.Constants.FACE_UP_CARD_SLOTS;
import static java.util.Objects.checkIndex;

/**
 * CardState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class CardState extends PublicCardState {

    private final Deck<Card> deck;
    private final SortedBag<Card> discard;


    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     * @param deck the deck
     * @param discard the discard sorted
     * @param faceUpCards list of facupcards
     *
     */

    private CardState(Deck<Card> deck, SortedBag<Card> discard, List<Card> faceUpCards ){

        super(faceUpCards, deck.size(), discard.size());
        this.deck = deck;
        this.discard = discard;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * factory constructor
     * the 5 cards placed face up are the first 5 of the given pile,
     * the draw pile consists of the remaining cards of the pile, and the discard pile is empty
     * @param deck the deck
     * @throws IllegalArgumentException if the size of the deck is less than 5
     * @return new card state
     */

    public static CardState of(Deck<Card> deck){

        Preconditions.checkArgument(deck.size() >= 5);

        List<Card> listTopCards = deck.topCards(5).toList();

        CardState without5 = new CardState(deck.withoutTopCards(5), null, listTopCards);

        return without5;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * set of cards identical to the receiver,
     * except that the face-up index slot card has been replaced by the one at the top of the draw pile,
     * which is removed at the same time
     * @param slot index of the card
     * @throws IllegalArgumentException if the deck is empty or the index slot is not < 5 or >= 0
     * @return new card state
     */

    public CardState withDrawnFaceUpCard(int slot){

        Preconditions.checkArgument(!deck.isEmpty());

        ArrayList<Card> newfaceupcard = new ArrayList<>(faceUpCards());
        newfaceupcard.set(checkIndex(slot , 5), deck.topCard());

        CardState withDrawnFaceUpCard = new CardState(deck.withoutTopCard(), discard, newfaceupcard);

        return withDrawnFaceUpCard;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @throws IllegalArgumentException if the deck is empty
     * @return top card in the deck
     */

    public Card topDeckCard(){

        Preconditions.checkArgument(!deck.isEmpty());

        return deck.topCard();

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * create the same card state without the top deck card
     * @throws IllegalArgumentException is the deck is empty
     * @return new card state
     */

    public CardState withoutTopDeckCard(){

        Preconditions.checkArgument(!deck.isEmpty());

        CardState  withoutTopDeckCard = new CardState(deck.withoutTopCard(), this.discard, faceUpCards());

        return withoutTopDeckCard ;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * create a new card state with new deck shuffle and an empty discard
     * @param rng random
     * @throws IllegalArgumentException is the deck is empty
     * @return new card state
     */

    public CardState withDeckRecreatedFromDiscards(Random rng){

        Preconditions.checkArgument(!deck.isEmpty());

        CardState  withDeckRecreatedFromDiscards = new CardState( Deck.of(discard, rng),SortedBag.of() , faceUpCards());

        return withDeckRecreatedFromDiscards;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * create a new card state with additional cards in the discard
     * @param additionalDiscards new cards for the discard
     * @return new card state
     */

    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){

        SortedBag<Card> newDiscard = discard.union(additionalDiscards);

        CardState  withMoreDiscardedCards = new CardState( this.deck , newDiscard , faceUpCards());

        return withMoreDiscardedCards;

    }

    //----------------------------------------------------------------------------------------------------

}
