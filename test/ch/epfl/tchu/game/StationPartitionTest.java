package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import ch.epfl.tchu.game.StationPartition.Builder;

class StationPartitionTest {
    private StationPartition.Builder b = new Builder(6);
    private Station s0 = new Station(0, "station 0");
    private Station s1 = new Station(1, "station 1");
    private Station s3 = new Station(3, "station 3");
    private Station s5 = new Station(5, "station 5");

    @Test
    public void connectedWorks(){
        b.connect(s1,s5);
        b.connect(s3,s5);
        b.connect(s0,s3);
        StationPartition sp = b.build();
        assertEquals(true,sp.connected(s1,s5));
        assertEquals(true,sp.connected(s1,s3));
        assertEquals(true,sp.connected(s1,s0));
    }

}
