package ch.epfl.tchu.game;

import java.util.List;

/**
 * PlayerId
 * enum
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public enum PlayerId {

    PLAYER_1,
    PLAYER_2;

    public static final List<PlayerId> ALL = List.of(values());
    public static final int COUNT = ALL.size();

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the other player (the one who will play next turn)
     */

    public PlayerId next() {

        return (this.equals(PLAYER_1)) ? PLAYER_2 : PLAYER_1;
    }

    //----------------------------------------------------------------------------------------------------
}
