package ch.epfl.tchu.game;


import java.util.List;
import java.util.TreeSet;

/**
 * @author Hugo Jeannin (329220)
 */
    public final class Ticket implements Comparable<Ticket> {
    private final List<Trip> trips;
    private final String representation;

    /** creates a ticket (list of trips)
     * @param trips
     */
    public Ticket(List<Trip> trips){
        if (trips.size() == 0){ throw new IllegalArgumentException();}
        String pattern = trips.get(0).from.name();
        for(Trip t : trips){
            if(t.from.name() != pattern){
                throw new IllegalArgumentException();
            }
        }

        this.trips = trips;
        representation = computeText();
    }

    /**
     * creates a ticket with one trip
     * @param from
     * @param to
     * @param points
     */
    public Ticket(Station from, Station to, int points){

        this(List.of(new Trip(from, to, points)));
    }

    /**
     *
     * @return the String to print on the ticket
     */
    private String computeText(){
        TreeSet<String> s = new TreeSet<>();
        String n = "";

        for (Trip t : trips){
            n = t.from.name();
            s.add( t.to.name() + " (" + t.points + ")" );
        }

        String text =String.join(", ", s);

        if(s.size() == 1){
            return String.format("%s - %s",n ,text);
        }
        return String.format("%s - {%s}",n ,text);
    }

    /**
     *
     * @return the graphic representation of the ticket
     */
    public String text(){ return representation; }

    @Override
    public String toString() { return representation; }

    /**
     *
     * @param connectivity connected or not
     * @return the amount of points earned with the ticket
     */
    public int points(StationConnectivity connectivity){
        int realPoints = -100;
        for (Trip t : trips){
            if(t.points(connectivity) > realPoints){
                realPoints = t.points(connectivity);
            }
        }
        return realPoints;
    }

    public int compareTo(Ticket that){
        return text().compareTo(that.text());
    }
}

