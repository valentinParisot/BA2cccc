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

    private final int length;
    private final List<Route> routeList;
    private final Station station1;
    private final Station station2;


    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     *
     * @param routeList list of roads that belong to the player
     * @param station1 first station of the trail
     * @param station2 last station of the trail
     * @param length length of the trail (sum of the length of each road)
     */

    private Trail(List<Route> routeList, Station station1, Station station2, int length) {
        this.routeList = routeList;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param routes list of roads that belong to the player
     * @return the longest trail possible (even if there may have several)
     */

    public static Trail longest(List<Route> routes) {
        Trail longest = null;
        List<Trail> cs = null;

        for (Route r : routes) {
            cs.add(new Trail(List.of(r), r.station1(), r.station2(), r.length()));
            cs.add(new Trail(List.of(r), r.station2(), r.station1(), r.length()));
        }

        while (cs.size() != 0) {
            List<Trail> csPrime = null;
            for (Trail t : cs) {

                List<Route> rs = t.routeList;

                for (Route r : routes) {
                    if ((!cs.contains(r)) && (r.stations().contains(t.station2))) {
                        rs.add(r);
                        csPrime.add(new Trail(rs, t.station1, r.stationOpposite(t.station2), t.length()+r.length()));
                    }
                }

                if ((t.length() > longest.length() || longest == null)) {
                    longest = t;
                }
                cs = csPrime;
            }
        }
        return longest;
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
        if (this.length() == 0) {
            return null;
        }
        return station1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the last station of the trail
     */
    public Station station2() {
        if (this.length() == 0) {
            return null;
        }
        return station2;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the stations of the trial (ordered) and its length
     */

    @Override
    public String toString() {

        if (this == null){
            return "Nothing to see there...";
        }

        String n = (station1.name() + station2.name());

        n = (n + "(" + length() + ")");

        return n;
    }

    //----------------------------------------------------------------------------------------------------
}
