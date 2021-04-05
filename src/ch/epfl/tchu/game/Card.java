package ch.epfl.tchu.game;

import java.util.List;

/**
 * Card
 * enum
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public enum Card {

    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);

    private static final int MAX_SIZE = 8;
    public static final List<Card> ALL = List.of(values());
    public static final int COUNT = ALL.size();
    public final static List<Card> CARS = ALL.subList(0, MAX_SIZE);
    private final Color c;


    //----------------------------------------------------------------------------------------------------

    /**
     * build a card, the color is given
     *
     * @param color given color
     */

    Card(Color color) {
        this.c = color;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param color given color
     * @return type of the card
     */

    //----------------------------------------------------------------------------------------------------
    public static Card of(Color color) {
        return Card.valueOf(color.toString());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return color of the type od the card
     */

    //----------------------------------------------------------------------------------------------------
    public Color color() {
        return c;
    }

    //----------------------------------------------------------------------------------------------------

}
