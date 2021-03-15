package ch.epfl.tchu.game;

import java.util.List;

/**
 * PlayerId
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public enum PlayerId {
    PLAYER_1,
    PLAYER_2;

    public static final List<PlayerId> ALL =  List.of(values());
    public static final int COUNT = ALL.size();

    //----------------------------------------------------------------------------------------------------

    public PlayerId next(){
        if (this.equals(PLAYER_1)){return PLAYER_2;}
        else {return PLAYER_1;}
    }

    //----------------------------------------------------------------------------------------------------

}
