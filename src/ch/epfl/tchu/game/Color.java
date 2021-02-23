package ch.epfl.tchu.game;

import java.util.List;

public enum Color{

    BLACK, VIOLET, BLUE, GREEN, YELLOW, ORANGE, RED, WHITE;
    public static final List<Color> ALL = List.of(values());
    public static final int COUNT = ALL.size();
    public int r ;

}
