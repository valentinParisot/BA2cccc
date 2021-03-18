package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * StationPartition & Builder
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class StationPartition implements StationConnectivity {

    private final List<Integer> integers;

    //----------------------------------------------------------------------------------------------------

    private StationPartition(List<Integer> integers) {
        this.integers = integers;
    }

    //----------------------------------------------------------------------------------------------------

    public boolean connected(Station station1, Station station2) {

        if (station1.id() > integers.size() || station2.id() > integers.size()) {
            if (station1.id() == station2.id()) {
                return true;
            }
        }

        if (integers.get(station1.id()) == integers.get(station2.id())) {
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------

    public final static class Builder {
        private List<Integer> integersBuilder;

        //------------------------------------------------------------------------------------------------

        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            integersBuilder = new ArrayList<Integer>() {};
            for (int i = 0; i < stationCount; i++) {
                integersBuilder.add(i,i);
            }
        }

        //------------------------------------------------------------------------------------------------

        private int representative(int id) {
            return integersBuilder.get(id);
        }

        //------------------------------------------------------------------------------------------------

        public Builder connect(Station s1, Station s2) {

            int rpz = representative(s2.id());

            for (int i = 0; i< integersBuilder.size(); ++i) {

                if (representative(i) == rpz) {
                    integersBuilder.set(i, representative(s1.id()));
                    System.out.println(i + " pointe vers " + representative(i));
                }
            }
            return this;
        }

        //------------------------------------------------------------------------------------------------

        public StationPartition build() {
            return new StationPartition(integersBuilder);
        }

        //------------------------------------------------------------------------------------------------
    }

    //----------------------------------------------------------------------------------------------------

}
