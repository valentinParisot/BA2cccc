package ch.epfl.tchu.game;

import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublicPlayerStateTest {

    @Test
    void constructorfailsticketless0(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-1 , 3, List.of());
        });
    }

    @Test
    void constructorfailscardless0(){
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(2 , -2, List.of());
        });
    }

    @Test

    void ticketcountworkswell(){

        var s1 = new PublicPlayerState(0, 3, List.of());
        var s2 = new PublicPlayerState(1, 3,List.of());
        assertEquals(0, s1.ticketCount());
        assertEquals(1, s2.ticketCount());
    }

    @Test

    void cardcountworkswell(){
        var s1 = new PublicPlayerState(0, 3, List.of());
        var s2 = new PublicPlayerState(1, 3,List.of());
        assertEquals(3, s1.cardCount());
        assertEquals(3, s2.cardCount());
    }

    @Test
    void routerreturnsroute(){

        List<Route> r1 = new ArrayList<>();
        var f = new Station(1,"2");
        var r = new Station(3, "3");
        Route s = new Route("d", r , f, 4 , Route.Level.OVERGROUND, Color.BLACK);
        Route s1 = new Route("e", f , r, 3 , Route.Level.OVERGROUND, Color.GREEN);
        r1.add(s);
        r1.add(s1);
        var a = new PublicPlayerState(0, 3, r1);
        assertEquals(r1, a.routes());

    }

    @Test
    void carCountwell(){

        List<Route> r1 = new ArrayList<>();
        var f = new Station(1,"2");
        var r = new Station(3, "3");
        Route a = new Route("d", r , f, 4 , Route.Level.OVERGROUND, Color.BLACK);
        Route b = new Route("e", f , r, 3 , Route.Level.OVERGROUND, Color.GREEN);
        r1.add(a);
        r1.add(b);
        var s1 = new PublicPlayerState(0, 3, r1);
        var s2 = new PublicPlayerState(1, 3, r1);
        var i = s1.carCount();
        var j = s2.carCount();
        assertEquals(33, i);
        assertEquals(33, j);

    }

    @Test
    void claimPoints(){

        List<Route> r1 = new ArrayList<>();
        var f = new Station(1,"2");
        var r = new Station(3, "3");
        Route a = new Route("d", r , f, 4 , Route.Level.OVERGROUND, Color.BLACK);
        Route b = new Route("e", f , r, 3 , Route.Level.OVERGROUND, Color.GREEN);
        r1.add(a);
        r1.add(b);
        var s1 = new PublicPlayerState(0, 3, r1);
        var s2 = new PublicPlayerState(1, 3, r1);
        var p = a.claimPoints() + b.claimPoints();
        assertEquals(p, s1.claimPoints());
        assertEquals(p, s2.claimPoints());


    }





}
