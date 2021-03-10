package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckTest {
double m;


public static final Random
    non_random = new Random() {
    @Override
    public int nextInt (int i){
        return i-1;
    }
};



    @Test
    void topcardre() {
        Deck d;
        d = Deck.of(SortedBag.of(List.of(1,2,3)), non_random );
        System.out.print(d.topCard());

    }
}
