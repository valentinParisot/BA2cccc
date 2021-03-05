package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public final class Trail {

    private int length;
    private List<Route> routeList;
    private Station station1;
    private Station station2;


    //----------------------------------------------------------------------------------------------------

    /**
     * creates a trail from a list of roads
     *
     * @param routeList list of roads making a trail
     */

    private Trail(List<Route> routeList, Station station1, Station station2, int length) {
        this.routeList = routeList;
        this.station1 = station1;
        this.station2 = station2;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param routes list of roads that belong to the player
     * @return the longest trail possible
     */

    public static Trail longest(List<Route> routes) {
        Trail longest = null;
        List<Trail> cs = null;
        for (Route r : routes) {
            cs.add(new Trail(List.of(r), r.station1(), r.station2(), r.length()));
        }

        while (cs.size() != 0) {
            List<Trail> csPrime = null;
            for (Trail t : cs) {

                for (Route r : routes) {
                    if ((!cs.contains(r)) && (r.stations().contains(t.station2))) {
                        t.routeList.add(r);
                        t.station2 = r.station2();
                        csPrime.add(new Trail(t.routeList, t.station1, t.station2, t.length()));
                    }
                }

                if ((t.length > longest.length() || longest == null)) {
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
     * @return the 1st station of the trail
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

        String n = " ";
        n = "";

        for (Route r : routeList) {
            n = (n + (r.station1().name() + " - "));
        }
        n = (n + (station2().name() + "(" + length() + ")"));

        return n;
    }

    //----------------------------------------------------------------------------------------------------
}
