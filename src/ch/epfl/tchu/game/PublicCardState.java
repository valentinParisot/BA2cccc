package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

import static java.util.Objects.checkIndex;

/**
 * PublicCardState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class PublicCardState {

    private final int decksize;
    private final int discardsize;
    private final List<Card> faceUpCards;

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     *
     * @param faceUpCards  face up cards are those given
     * @param deckSize     size of the deck
     * @param discardsSize size of the discard
     * @throws IllegalArgumentException if the number of faceup cards is not 5 or if the size of the deck or the discard is less than 0
     */

    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize) {

        Preconditions.checkArgument(faceUpCards.size() == Constants.FACE_UP_CARDS_COUNT && deckSize >= 0 && discardsSize >= 0);

        this.faceUpCards = List.copyOf(faceUpCards);

        this.discardsize = discardsSize;

        this.decksize = deckSize;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * which returns the total number of cards that are not in the players hand
     *
     * @return number of cards that are not in the players hand
     */

    public int totalSize() {

        return (Constants.FACE_UP_CARDS_COUNT + decksize + discardsize);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the list of the 5 cards that are face up
     *
     * @return a list of cards the size is 5
     */

    public List<Card> faceUpCards() {

        return faceUpCards;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the card face up at the given index
     *
     * @param slot index of the card
     * @return faceUpcards
     * @throws IndexOutOfBoundsException if slot is less than 0 or bigger than 5
     */

    public Card faceUpCard(int slot) {

        return faceUpCards.get(checkIndex(slot, Constants.FACE_UP_CARDS_COUNT));


    }

    //----------------------------------------------------------------------------------------------------

    /**
     * size deck
     *
     * @return the size of the deck
     */

    public int deckSize() {

        return decksize;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * true if the deck is empty
     *
     * @return true if the deck is empty
     */

    public boolean isDeckEmpty() {

        return (decksize == 0);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * discard size
     *
     * @return discard size
     */

    public int discardsSize() {

        return discardsize;
    }

    //----------------------------------------------------------------------------------------------------


}
