package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ch.epfl.tchu.game.PlayerId.*;

class PlayerIdTest {
    @Test
    void nextWorks(){
        assertEquals(PLAYER_1, PLAYER_2.next());
        assertEquals(PLAYER_2, PLAYER_1.next());
    }
}
