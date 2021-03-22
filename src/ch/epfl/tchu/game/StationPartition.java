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

    private final int[] integers;

    //----------------------------------------------------------------------------------------------------

    private StationPartition(int[] integers) {
        this.integers = integers;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param station1
     * @param station2
     * @return if 2 stations are connected or not
     */
    public boolean connected(Station station1, Station station2) {

        if (station1.id() > integers.length || station2.id() > integers.length) {
            if (station1.id() == station2.id()) {
                return true;
            }
            return false;
        }

        if (integers[station1.id()] == integers[station2.id()]) {
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------------

    public final static class Builder {
        private int[] integersBuilder;

        //------------------------------------------------------------------------------------------------

        public Builder(int stationCount) {
            Preconditions.checkArgument(stationCount >= 0);
            integersBuilder = new int[stationCount];
            for (int i = 0; i < stationCount; i++) {
                integersBuilder[i] = i;
            }
        }

        //------------------------------------------------------------------------------------------------

        /**
         * @param id the id of a station (its place in the list)
         * @return the representative of a station
         */
        private int representative(int id) {
            int i = id;
            while (i != integersBuilder[i]) {
                i = integersBuilder[i];
            }
            return i;
        }

        //------------------------------------------------------------------------------------------------

        /**
         * @param s1 first staion
         * @param s2 second station
         * @return a Builder with the 2 station connected
         */
        public Builder connect(Station s1, Station s2) {

            integersBuilder[representative(s2.id())] = representative(s1.id());
            return this;
        }

        //------------------------------------------------------------------------------------------------

        /**
         * @return a partition with exactly one representative for each group of stations
         */
        public StationPartition build() {

            for (int i = 0; i < integersBuilder.length; ++i) {

                integersBuilder[i] = representative(i);
            }

            return new StationPartition(integersBuilder.clone());
        }

        //------------------------------------------------------------------------------------------------
    }

    //----------------------------------------------------------------------------------------------------

}
