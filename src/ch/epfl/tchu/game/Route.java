package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.*;

public final class Route {

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;


    public enum Level {

        OVERGROUND ,
        UNDERGROUND ;

    }

    public Route(String id, Station station1, Station station2, int length, Level level, Color color){
        if(station1.equals(station2)){
            throw new IllegalArgumentException("Stations are the same");
        }
        if(length > Constants.MAX_ROUTE_LENGTH || length < Constants.MIN_ROUTE_LENGTH){
            throw new IllegalArgumentException("wrong size");

        }
        if(id == null || station1 == null || station2 == null || level == null){
            throw new NullPointerException("Some fields are null");

        }

        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;

    }

    public String id() {
        return id;
    }

    public Station station1() {
        return station1;
    }

    public Station station2() {
        return station2;
    }

    public int length() {
        return length;
    }

    public Level level() {
        return level;
    }

    public Color color() {
        if(this.color == null){

            return null;
        }
        return color;
    }

    public List<Station> stations(){
        List<Station> stations = new ArrayList<Station>();
        stations.add(station1);
        stations.add(station2);

        return stations;
    }

    public Station stationOpposite(Station station){
        if (station.equals(station1)){
            return station2;
        }
        if (station.equals(station2)){
            return station1;
        }
        else {
            throw new IllegalArgumentException("Incompatible Station");
        }

    }

    public List<SortedBag<Card>> possibleClaimCards(){
        SortedBag.Builder<Card> card1 = new SortedBag.Builder<>();
      if(this.color != null) {
          for (int loco = 0; loco < length; loco++) {
              card1.add(SortedBag.of(length - loco, Card.of(this.color), loco, Card.LOCOMOTIVE));

          }
      }
        else{




          }







    }

    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){

    }

    public int claimPoints(){

    }
}
