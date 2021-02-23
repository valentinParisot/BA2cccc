package ch.epfl.tchu.game;

import java.util.List;

public enum Card {

    BLACK(1),
    VIOLET(2),
    BLUE(3),
    GREEN(4),
    YELLOW(5),
    ORANGE(6),
    RED(7),
    WHITE(8),
    LOCOMOTIVE(null);

    public static final List<Card> ALL = List.of(values());
    public static final int COUNT = ALL.size();
    public final static List<Card> CARS =  ALL.subList(0,COUNT);



    public Card of (Color color){
        return Card.valueOf(color.toString());
    }
    public Color color(){

    return



    }

}
