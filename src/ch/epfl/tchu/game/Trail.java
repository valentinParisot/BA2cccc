package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public final class Trail {

    private static int length;
    private static List<Route> routeList;
    private static Station station1;
    private static Station station2;


    //----------------------------------------------------------------------------------------------------

    /**
     *creates a trail from a list of roads
     * @param routeList
     */

    private Trail(List<Route> routeList) {
        this.routeList = routeList;
        station1 = routeList.get(0).station1();
        station2 = routeList.get(routeList.size() - 1).station1();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param routes list of roads that belong to the player
     * @return the longest trail possible
     */

    public static Trail longest(List<Route> routes) {
        Trail longest = null;
        List<Trail> cs = null;
        for (Route r : routes) {
            cs.add(new Trail(List.of(r)));
        }

        while (cs.size() != 0) {
            List<Trail> csPrime = null;
            for (Trail t : cs) {

                List<Route> rs = null;

                for (Route r : routes) {
                    if ((!cs.contains(r)) && ((r.stations().contains(r.stationOpposite(t.station2())))
                    || (r.stations().contains(r.stationOpposite(t.station1()))))) {
                        rs.add(r);

                    }
                }
                for (Route r1 : rs) {
                    t.routeList.add(r1);
                    csPrime.add(new Trail(t.routeList));
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
     *
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
     *
     * @return the 1st station of the trail
     */

    public Station station1(){
        if(this.length() == 0){
            return null;
        }
        return routeList.get(0).station1();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the last station of the trail
     */
    public Station station2(){
        if(this.length() == 0){
            return null;
        }
        return routeList.get(routeList.size() - 1).station2();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the stations of the trial (ordered) and its points
     */

    @Override
    public String toString(){

        String n = " ";
        n = "";

        for(Route r : routeList){
            n =(n + (r.station1().name() + " - "));
        }
        n = (n + (station2().name() + "(" + length() + ")"));

        return n;
    }

    //----------------------------------------------------------------------------------------------------
}
