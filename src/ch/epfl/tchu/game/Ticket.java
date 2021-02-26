package ch.epfl.tchu.game;


import java.util.List;
import java.util.TreeSet;

public final class Ticket implements Comparable<Ticket> {
    private int p;
    private final List<Trip> trips;
    private final String representation;

    public Ticket(List<Trip> trips){
        if (trips == null){ throw new IllegalArgumentException();}
        else{ this.trips = trips; }
        representation = computeText();
    }

    public Ticket(Station from, Station to, int points){

        this(List.of(new Trip(from, to, points)));
    }


    private String computeText(){
        TreeSet<String> s = new TreeSet<>();
        String n = "";

        for (Trip t : trips){
            n = t.from.name();
            s.add( t.to.name() + " (" + t.points + ")" );
        }

        String text =String.join(", ", s);

        return String.format("%s - {%s}",n ,text);
    }

    public String text(){ return representation; }

    @Override
    public String toString() { return representation; }

    public int points(StationConnectivity connectivity){ return 3;}

    public int compareTo(Ticket that){
        return text().compareTo(that.text());
    }
}

