package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * Route
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Route {

    //----------------------------------------------------------------------------------------------------

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    //----------------------------------------------------------------------------------------------------

    /**
     * enum contains two level of route
     * OVERGROUND
     * UNDERGROUND
     */

    public enum Level {

        OVERGROUND,
        UNDERGROUND
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor of route
     *
     * @param id       id of route
     * @param station1 first station
     * @param station2 second station
     * @param length   length of the route
     * @param level    underground or overground
     * @param color    color
     * @throws IllegalArgumentException if station 1 equals to station 2
     *                                  if the length is not in the constant bounds
     * @throws NullPointerException     if id or station1,2 or level is/are null
     */

    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {

        Preconditions.checkArgument(!(station1.equals(station2)));
        Preconditions.checkArgument((length <= Constants.MAX_ROUTE_LENGTH && length >= Constants.MIN_ROUTE_LENGTH));

        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = Objects.requireNonNull(level);
        this.color = color;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the id of the route
     */

    public String id() {
        return id;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the first station
     */

    public Station station1() {
        return station1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the second station
     */

    public Station station2() {
        return station2;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the length of the route
     */

    public int length() {
        return length;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the level, overground or underground
     */

    public Level level() {
        return level;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the color of the route
     */

    public Color color() { return color; }

    //----------------------------------------------------------------------------------------------------

    /**
     * creat a list with the first and the second station
     *
     * @return list with station 1,2
     */

    public List<Station> stations() {

        List<Station> stations = new ArrayList<>();
        stations.add(station1);
        stations.add(station2);

        return stations;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * return the opposit station of the station in the input
     *
     * @param station starting station or ending station
     * @return the opposite station of the param
     * @throws IllegalArgumentException if the given station is neither the first nor the second
     */

    public Station stationOpposite(Station station) {

        Preconditions.checkArgument(stations().contains(station));

        if (station.equals(station1)) {
            return station2;
        } else
            return station1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * - return the list with the whole sets of cards who can be play
     * - the list is sorted by increasing number of Locomotive cards then by color
     *
     * @return the list with the whole sets of cards who can be play
     */

    public List<SortedBag<Card>> possibleClaimCards() {

        List<SortedBag<Card>> cards1 = new ArrayList<>();

        if (level.equals(Level.UNDERGROUND)) {

            for (int i = 0; i <= length; i++) {

                if (color != null) {

                    cards1.add(SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE));
                } else {

                    if (i != length) {

                        for (Card card : Card.CARS) {

                            cards1.add(SortedBag.of(length - i, card, i, Card.LOCOMOTIVE));
                        }
                    } else {

                        cards1.add(SortedBag.of(i, Card.LOCOMOTIVE));
                    }
                }
            }
        }

        if (level.equals(Level.OVERGROUND)) {

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
     *
     * @param claimCards cards posed by the player
     * @param drawnCards 3 cards drown on the top of the deck
     * @return the number of cards additional required to get the route
     * @throws IllegalArgumentException if the current route is not an undergound
     *                                  or if drowncards doesn't have exactly 3 cards
     */

    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards) {

        Preconditions.checkArgument((level.equals(Level.UNDERGROUND))
                                    && (drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS));

        Card card = claimCards.get(0);

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
     * 1             1
     * 2             2
     * 3             4
     * 4             7
     * 5             10
     * 6             15
     *
     * @return points
     */

    public int claimPoints() { return Constants.ROUTE_CLAIM_POINTS.get(length); }

    //----------------------------------------------------------------------------------------------------
}
