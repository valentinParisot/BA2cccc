package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Random;

public final class CardState extends PublicCardState {

    private final List<Card> deck;
    private final List<Card> discard;


    //----------------------------------------------------------------------------------------------------

    private CardState(){

    }

    //----------------------------------------------------------------------------------------------------

    public static CardState of(Deck<Card> deck){

        Preconditions.checkArgument(deck.size() >= 5);



    }

    //----------------------------------------------------------------------------------------------------

    public CardState withDrawnFaceUpCard(int slot){

        Preconditions.checkArgument(!deck.isEmpty());

    }

    //----------------------------------------------------------------------------------------------------

    public Card topDeckCard(){


    }

    //----------------------------------------------------------------------------------------------------

    public CardState withoutTopDeckCard(){


    }

    //----------------------------------------------------------------------------------------------------

    public CardState withDeckRecreatedFromDiscards(Random rng){


    }

    //----------------------------------------------------------------------------------------------------

    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){


    }

    //----------------------------------------------------------------------------------------------------

}
