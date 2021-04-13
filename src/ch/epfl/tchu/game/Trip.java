package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Trip
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Trip {

    //----------------------------------------------------------------------------------------------------

    /**
     * creates a list of trips (with starting and ending stations)
     *
     * @param from   from
     * @param to     to
     * @param points points
     * @return a list of trips
     */

    public static List<Trip> all(List<Station> from, List<Station> to, int points) {

        List<Trip> path = new ArrayList<>();

        if (from == null || to == null || points <= 0) {

            throw new IllegalArgumentException();
        } else {

            for (Station f : from) {

                for (Station t : to) {

                    path.add(new Trip(f, t, points));
                }
            }
        }

        return path;
    }

    //----------------------------------------------------------------------------------------------------

    public final Station from;
    public final Station to;
    public final int points;

    //----------------------------------------------------------------------------------------------------

    /**
     * @param from   starting station
     * @param to     ending station
     * @param points the amount of points for this trip
     */

    public Trip(Station from, Station to, int points) {

        Preconditions.checkArgument(points > 0);

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return starting station
     */

    public Station from() {
        return this.from;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return ending station
     */

    public Station to() {
        return this.to;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the amount of points of the trip
     */

    public int points() {
        return this.points;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * compute with the connectivity given
     *
     * @param connectivity connectivity
     * @return the amount of points if the stations are connected or not
     */

    public int points(StationConnectivity connectivity) {

        if (connectivity.connected(from, to)) {
            return points();
        }

        return (-points());
    }

    //----------------------------------------------------------------------------------------------------
}
