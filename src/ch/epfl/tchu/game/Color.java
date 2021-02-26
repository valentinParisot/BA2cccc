package ch.epfl.tchu.game;

import java.util.List;

/**
 *
 * @author valentin Parisot (326658)
 */
public enum Color{


    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;
    public static final List<Color> ALL = List.of(values());
    public static final int COUNT = ALL.size();
   

}
