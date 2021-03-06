package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Trail
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Trail {

    //----------------------------------------------------------------------------------------------------

    /**
     * returns the longest path of the network made up of the given routes; if there are more than one of maximum
     * length, the returned one is not specified; if the list of given routes is empty, returns a zero-length path,
     * whose stations are both equal to null
     *
     * @param routes list of roads that belong to the player
     * @return the longest trail possible (even if there may have several)
     */

    public static Trail longest(List<Route> routes) {

        Trail longest = new Trail(List.of(), null, null, 0);
        List<Trail> helper = new ArrayList<>();

        for (Route r : routes) {
            helper.add(new Trail(List.of(r), r.station1(), r.station2(), r.length()));

            if (r.length() > longest.length) {
                longest = new Trail(List.of(r), r.station1(), r.station2(), r.length());
            }
            helper.add(new Trail(List.of(r), r.station2(), r.station1(), r.length()));
        }

        while (helper.size() != 0) {
            List<Trail> helperPrime = new ArrayList<>();
            for (Trail t : helper) {


                for (Route r : routes) {
                    if ((!t.routeList.contains(r)) && (r.stations().contains(t.station2))) {

                        List<Route> rs = new ArrayList<>(t.routeList);
                        rs.add(r);
                        helperPrime.add(new Trail(rs, t.station1, r.stationOpposite(t.station2), t.length + r.length()));

                        if (t.length + r.length() > longest.length) {
                            longest = new Trail(rs, t.station1, r.stationOpposite(t.station2), t.length + r.length());
                        }
                    }
                }

            }
            helper = helperPrime;
        }
        return longest;
    }

    //----------------------------------------------------------------------------------------------------

    private final int length;
    private final List<Route> routeList;
    private final Station station1;
    private final Station station2;

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     *
     * @param routeList list of roads that belong to the player
     * @param station1  first station of the trail
     * @param station2  last station of the trail
     * @param length    length of the trail (sum of the length of each road)
     */

    private Trail(List<Route> routeList, Station station1, Station station2, int length) {

        this.routeList = routeList;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the length of the trail
     */

    public int length() {

        int size = 0;
        for (Route r : routeList) {
            size = (size + r.length());
        }

        return size;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the first station of the trail
     */

    public Station station1() {
        return (this.length() == 0) ? null : station1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the last station of the trail
     */
    public Station station2() {
        return (this.length() == 0) ? null : station2;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the stations of the trial (ordered) and its length
     */

    @Override
    public String toString() {

        String n = (station1.name() + station2.name());
        n = (n + "(" + length() + ")");

        return (this == null) ? "Nothing to see there..." : n;
    }

    //----------------------------------------------------------------------------------------------------
}
