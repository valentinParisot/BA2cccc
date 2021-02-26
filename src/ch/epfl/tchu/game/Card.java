package ch.epfl.tchu.game;

import java.util.List;

/**
 *
 * @author Valentin Parisot (326658)
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

    public int v;

    public static final List<Card> ALL =  List.of(values());
    public static final int COUNT = ALL.size();
    public final static List<Card> CARS =  ALL.subList(0,COUNT);
    private final Color c;

    /**
     * build a card, the color is given
     * @param b
     */
    private Card(Color b) {
        this.c = b;
    }

    /**
     *
     * @param color
     * @return type of the card
     */
    public static Card of (Color color){
        return Card.valueOf(color.toString());
    }

    /**
     *
     * @return color of the type od the card
     */
    public Color color(){
    return c;
    }

}
