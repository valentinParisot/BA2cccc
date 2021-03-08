package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Deck
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Deck<C extends Comparable<C>> {

    private final int size;
    private final List<C> cards;

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     *
     * @param size size of the deck
     * @param cards List of cards in the deck
     */

    private Deck(int size , List<C> cards){

        this.cards = cards;
        this.size = size;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * take a sorted bag of cards and return a deck with the same cards but the deck is now shuffle
     *
     * @param cards cards that will be shuffled
     * @param rng parameter random for shuffle
     * @param <C> type
     * @return deck shuffled
     */

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){

       List<C> cards2 =  cards.toList();

       Collections.shuffle(cards2 , rng);

       Deck<C> deckschuffle = new Deck<C>(cards.size(), cards2);

       return deckschuffle;


    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns size
     *
     * @return size of deck
     */

    public int size(){

        return this.size;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * true if there is nothing in the deck
     *
     * @return true if its empty
     */

    public boolean isEmpty(){

        return (cards.size() == 0);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * the card on the top is return, here the top is the last place in the list
     *
     * @throws IllegalArgumentException if the deck is empty
     * @return the card on the top of the deck
     */

    public C topCard(){

        Preconditions.checkArgument(!this.isEmpty());

        return cards.get(cards.size() - 1);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * take the current list and remove the card on the top
     *
     * @throws IllegalArgumentException if the deck is empty
     * @return the deck without the TopCard
     */

    public Deck<C> withoutTopCard(){

        Preconditions.checkArgument(!this.isEmpty());

        Deck<C> deckwithoutTopCard = new Deck<>(this.size() - 1 , cards.subList(0, cards.size() - 1));

        return deckwithoutTopCard;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns a multiset containing the count cards at the top of the pile
     *
     * @param count index of the last element
     *
     * @throws IllegalArgumentException if count is superior than the current size or inferior than 0
     * @return returns a multiset containing the count cards at the top of the pile
     */

    public SortedBag<C> topCards(int count){

        Preconditions.checkArgument(count >= 0 && count <= this.size());

        List<C> cards2 = cards.subList(this.size() - count , this.size());

        SortedBag.Builder<C> builder = new SortedBag.Builder<>();

        for (C c : cards2){

            builder.add(c);

        }

        SortedBag<C> topCards = builder.build();

        return topCards;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns a heap identical to the receiver but without the top card count
     *
     * @param count
     *
     * @throws IllegalArgumentException if count is superior than the current size or inferior than 0
     * @return a heap identical to the receiver but without the top card count
     */

    public Deck<C> withoutTopCards(int count){

        Preconditions.checkArgument(count >= 0 && count <= this.size());

        Deck<C> deckwithoutTopCards = new Deck<>(this.size() - count , cards.subList(0, cards.size() - count));

        return deckwithoutTopCards;

    }

    //----------------------------------------------------------------------------------------------------

}