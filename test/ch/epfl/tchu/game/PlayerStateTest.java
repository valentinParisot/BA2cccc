package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerStateTest {

    @Test
    void initialexceptionno4(){
        SortedBag.Builder<Card> b = new SortedBag.Builder<>();
        SortedBag<Card> t = b.build();
        assertThrows(IllegalArgumentException.class, () -> {
             PlayerState.initial(t);
        });

    }

    @Test
    void ticketsreturnstickets(){

        SortedBag.Builder<Ticket> b = new SortedBag.Builder<>();
        var t1 = new Ticket(List.of());
        b.add(t1);
        SortedBag<Ticket> t = b.build();
        SortedBag.Builder<Card> r = new SortedBag.Builder<>();
        SortedBag<Card> t2 = r.build();
        List<Route> r1 = new ArrayList<>();
        var f = new Station(1,"2");
        var i = new Station(3, "3");
        Route s = new Route("d", i , f, 4 , Route.Level.OVERGROUND, Color.BLACK);
        Route s1 = new Route("e", f , i, 3 , Route.Level.OVERGROUND, Color.GREEN);
        r1.add(s);
        r1.add(s1);
        var a = new PlayerState(t,t2, r1);
        a.initial(t2);
        ArrayList<Route> routes = new ArrayList<>();
        assertEquals(routes, a.routes());
    }
}
