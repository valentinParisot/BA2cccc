package ch.epfl.tchu.game;

import java.util.List;

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

    public static final List<Card> ALL =  List.of(values());
    public static final int COUNT = ALL.size();
    public final static List<Card> CARS =  ALL.subList(0,COUNT);
    private final Color c;

    private Card(Color b) {
        this.c = b;
    }


    public Card of (Color color){
        return Card.valueOf(color.toString());
    }
    public Color color(){
    return c;
    }

}
