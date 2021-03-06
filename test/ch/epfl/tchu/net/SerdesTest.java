package ch.epfl.tchu.net;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map;

import static ch.epfl.tchu.game.Card.*;
import static ch.epfl.tchu.game.PlayerId.*;
import static ch.epfl.tchu.net.Serdes.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SerdesTest {

    @Test

            void serdesok() {

        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        PublicGameState gs =
                new PublicGameState(40, cs, PLAYER_2, ps, null);

        assertEquals("40:6,7,2,0,6;30;31:1:10;11;0,1:20;21;:"
            , PUBLIC_GAME_STATE_SERDE.serialize(gs));
    }
}
