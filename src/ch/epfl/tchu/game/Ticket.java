package ch.epfl.tchu.game;


import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.TreeSet;

/**
 * Ticket
 * class
 *
 * @author Hugo Jeannin (329220)
 */

public final class Ticket implements Comparable<Ticket> {
    private final List<Trip> trips;
    private final String representation;

    //----------------------------------------------------------------------------------------------------

    /**
     * creates a ticket (list of trips)
     * @throws IllegalArgumentException if the size of the trips list is 0
     *                                  or if all starting stations don't have the same name
     * @param trips
     */

    public Ticket(List<Trip> trips) {

        Preconditions.checkArgument(trips.size() != 0);

        String pattern = trips.get(0).from.name();

        for (Trip t : trips) {

            Preconditions.checkArgument(t.from.name() == pattern);

        }

        this.trips = trips;

        representation = computeText();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * creates a ticket with one trip
     *
     * @param from
     * @param to
     * @param points
     */

    public Ticket(Station from, Station to, int points) {

        this(List.of(new Trip(from, to, points)));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the name of the sations with setting
     * @return the String to print on the ticket
     */

    private String computeText() {
        TreeSet<String> s = new TreeSet<>();
        String n = "";

        for (Trip t : trips) {
            n = t.from.name();
            s.add(t.to.name() + " (" + t.points + ")");
        }

        String text = String.join(", ", s);

        if (s.size() == 1) {
            return String.format("%s - %s", n, text);
        }
        return String.format("%s - {%s}", n, text);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the graphic representation of the ticket
     */

    public String text() {
        return representation;
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return representation;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns the number of points the ticket is worth, knowing that the connectivity given is that of the player owning the ticket
     * @param connectivity connected or not
     * @return the amount of points earned with the ticket
     */

    public int points(StationConnectivity connectivity) {
        int realPoints = -100;
        for (Trip t : trips) {
            if (t.points(connectivity) > realPoints) {
                realPoints = t.points(connectivity);
            }
        }
        return realPoints;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * which compares the post to which it is applied (this) to the one passed as argument (that) in alphabetical order of their textual representation
     *
     * @param that post compare with the current
     * @return returns a strictly negative integer if this is strictly less than that, a strictly positive integer if this is strictly greater than that, and zero if the two are equal
     */

    public int compareTo(Ticket that) {
        return text().compareTo(that.text());
    }

    //----------------------------------------------------------------------------------------------------
}

