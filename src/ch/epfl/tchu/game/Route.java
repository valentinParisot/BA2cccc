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


    //----------------------------------------------------------------------------------------------------

    public enum Level {

        OVERGROUND,
        UNDERGROUND;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor of route
     * @param id
     *          id of route
     * @param station1
     *          first station
     * @param station2
     *          second station
     * @param length
     *          length of the route
     * @param level
     *          underground or overground
     * @param color
     *          color
     *
     * @throws IllegalArgumentException
     *          if station 1 equals to station 2
     *          if the length is not in the constant bounds
     * @throws NullPointerException
     *          if id or station1,2 or level is/are null
     */

    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        if (station1.equals(station2)) {
            throw new IllegalArgumentException("Stations are the same");
        }
        if (length > Constants.MAX_ROUTE_LENGTH || length < Constants.MIN_ROUTE_LENGTH) {
            throw new IllegalArgumentException("wrong size");

        }
        if (id == null || station1 == null || station2 == null || level == null) {
            throw new NullPointerException("Some fields are null");

        }

        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the id of the route
     */

    public String id() {
        return id;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the first station
     */

    public Station station1() {
        return station1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the second station
     */

    public Station station2() {
        return station2;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the length of the route
     */

    public int length() {
        return length;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the level, overground or underground
     */

    public Level level() {
        return level;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return the color of the route
     */

    public Color color() {
        if (this.color == null) {

            return null;
        }
        return color;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * creat a list with the first and the second station
     * @return list with station 1,2
     */

    public List<Station> stations() {
        List<Station> stations = new ArrayList<Station>();
        stations.add(station1);
        stations.add(station2);

        return stations;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the next station
     * @param station starting station or ending station
     * @return the opposite station of the param
     */

    public Station stationOpposite(Station station) {
        if (station.equals(station1)) {
            return station2;
        }
        if (station.equals(station2)) {
            return station1;
        } else {
            throw new IllegalArgumentException("Incompatible Station");
        }

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the list with the whole sets of cards who can be play
     * the list is sorted
     * @return the list with the whole sets of cards who can be play
     */

    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> cards1 = new ArrayList<>();
        if (level.equals(Level.UNDERGROUND)) {
            for (int i = 0; i < length; i++) {
                if (color != null) {
                    cards1.add(SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE));

                } else {
                    for (Card card : Card.CARS) {
                        cards1.add(SortedBag.of(length - i, card, i, Card.LOCOMOTIVE));
                    }
                }

            }
        } else if (level.equals(Level.OVERGROUND)) {
            if (color != null) {
                cards1.add(SortedBag.of(length, Card.of(color)));

            } else {
                for (Card card : Card.CARS) {
                    cards1.add(SortedBag.of(length, card));
                }
            }
        }
        return cards1;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the number of cards additional required to get the route
     * @param claimCards cards posed by the player
     * @param drawnCards 3 cards drown on the top of the deck
     *
     * @throws IllegalArgumentException if the current route is not an undergound
     *                                  or if drowncards doesn't have exactly 3 cards
     * @return the number of cards additional required to get the route
     */

    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {

        if (level.equals(Level.OVERGROUND) || drawnCards.size() != 3) {
            throw new IllegalArgumentException("Error level ");
        }

        Card card = claimCards.get(0);
        Color color = card.color();

        int count = 0;
        for (int i = 0; i < 3; ++i) {
            if ((drawnCards.get(i)).equals(card)) {
                count++;

            } else if ((drawnCards.get(i)).equals(Card.LOCOMOTIVE)) {
                count++;

            }
        }

        return count;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the number of points that a players obtains when you the player get the route
     * compute with the length
     * length       points
     *  1             1
     *  2             2
     *  3             4
     *  4             7
     *  5             10
     *  6             15
     * @return points
     */

    public int claimPoints() {

        int points = 0;
        switch (length) {
            case 1:
                points = 1;
                break;

            case 2:
                points = 2;
                break;

            case 3:
                points = 4;
                break;

            case 4:
                points = 7;
                break;

            case 5:
                points = 10;
                break;

            case 6:
                points = 15;
                break;

            default:
                points = 0;
                break;

        }
        return points;
    }

    //----------------------------------------------------------------------------------------------------
}
