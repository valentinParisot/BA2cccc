package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerStateTest {

    private static final List<Card> ALL_CARDS = List.of(Card.values());
    private static final int FACE_UP_CARDS_COUNT = 5;

    @Test
    void initialexceptionno4(){
        SortedBag.Builder<Card> b = new SortedBag.Builder<>();
        SortedBag<Card> t = b.build();
        assertThrows(IllegalArgumentException.class, () -> {
             PlayerState.initial(t);
        });

    }

    @Test

    void withAddedCard(){

    }


}
