package ch.epfl.tchu.game;

import java.util.List;

/**
 * Color
 * enum
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public enum Color{


    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;


    public static final List<Color> ALL = List.of(values());
    public static final int COUNT = ALL.size();
   

}
