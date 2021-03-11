/*
 *  Author:	Gilles de Waha
 *  Date: 	11 mars 2021
 */
package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;

public class DeckTest {

    //public <C> List<C> of(Deck deck) {


	/*@Test
	void ofWorks() {
		final Random NON_RANDOM = new Random() {
		    @Override
		    public int nextInt(int i) {
		        return i-1;
		    }
		};
		assertEquals(new Deck(), Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM));
	}*/

    @Test
    void sizeWorks() {
        final Random NON_RANDOM = new Random() {
            @Override
            public int nextInt(int i) {
                return i-1;
            }
        };
        assertEquals(3, Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM).size());
    }

    @Test
    void isEmptyWorks() {
        final Random NON_RANDOM = new Random() {
            @Override
            public int nextInt(int i) {
                return i-1;
            }
        };
        assertEquals(false, Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM).isEmpty());
    }

    @Test
    void topCardWorks() {
        List<String> list = new ArrayList<>();
        final Random NON_RANDOM = new Random() {
            @Override
            public int nextInt(int i) {
                return i-1;
            }
        };
        assertEquals(3, Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM).topCard());
        assertThrows(IllegalArgumentException.class, () -> {
            Deck.of(SortedBag.of(list), NON_RANDOM).topCard();
        });
    }

    @Test
    void topCardsWorks() {
        final Random NON_RANDOM = new Random() {
            @Override
            public int nextInt(int i) {
                return i-1;
            }
        };
        assertEquals(SortedBag.of(List.of(2, 3)), Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM).topCards(2));
        assertThrows(IllegalArgumentException.class, () -> {
            Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM).topCards(4);
        });
    }

    @Test
    void withoutTopCardWorks() {
        List<String> list = new ArrayList<>();
        final Random NON_RANDOM = new Random() {
            @Override
            public int nextInt(int i) {
                return i-1;
            }
        };
        //assertEquals(SortedBag.of(List.of(2, 3)), Deck.of(SortedBag.of(List.of(1, 2, 3)), NON_RANDOM).withoutTopCard());
        assertThrows(IllegalArgumentException.class, () -> {
            Deck.of(SortedBag.of(list), NON_RANDOM).withoutTopCard();
        });
    }

    @Test
    void withoutTopCardsWorks() {
        List<String> list = new ArrayList<>();
        final Random NON_RANDOM = new Random() {
            @Override
            public int nextInt(int i) {
                return i-1;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> {
            Deck.of(SortedBag.of(list), NON_RANDOM).withoutTopCard();
        });
    }
}
