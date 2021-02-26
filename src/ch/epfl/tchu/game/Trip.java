package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip {

    public final Station from;
    public final Station to;
    public final int points;
    public Trip(Station from, Station to, int points){
        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        if (points<=0){
            throw new IllegalArgumentException();
        }
        else{this.points = points;}
    }

    /**
     *
     * @param from
     * @param to
     * @param points
     * @return
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points){
        List<Trip> path = new ArrayList<>();
        if (from == null || to == null || points <=0) {
            throw new IllegalArgumentException();
        }
        else {
            for (Station f : from) {
                for (Station t : to) {
                    path.add(new Trip(f, t, points));
                }
            }
        }
        return path;
    }

    public Station from(){ return this.from;}

    public Station to(){return this.to;}

    public int points(){return this.points;}

    public int points(StationConnectivity connectivity){
        if (connectivity.connected(from, to)){
            return points();
        }
        int malus = (- points());
        return malus;
    }
}
