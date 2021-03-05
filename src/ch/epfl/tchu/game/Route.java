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

        OVERGROUND,
        UNDERGROUND;

    }

    //----------------------------------------------------------------------------------------------------

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

    public String id() {
        return id;
    }

    //----------------------------------------------------------------------------------------------------

    public Station station1() {
        return station1;
    }

    //----------------------------------------------------------------------------------------------------

    public Station station2() {
        return station2;
    }

    //----------------------------------------------------------------------------------------------------

    public int length() {
        return length;
    }

    //----------------------------------------------------------------------------------------------------

    public Level level() {
        return level;
    }

    //----------------------------------------------------------------------------------------------------

    public Color color() {
        if (this.color == null) {

            return null;
        }
        return color;
    }

    //----------------------------------------------------------------------------------------------------

    public List<Station> stations() {
        List<Station> stations = new ArrayList<Station>();
        stations.add(station1);
        stations.add(station2);

        return stations;
    }

    //----------------------------------------------------------------------------------------------------

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
     *
     * @param claimCards
     * @param drawnCards
     * @return
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
