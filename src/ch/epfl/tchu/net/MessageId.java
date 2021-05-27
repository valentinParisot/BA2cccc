package ch.epfl.tchu.net;

import java.util.List;

/**
 * MessageId
 * enum
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public enum MessageId {

    //----------------------------------------------------------------------------------------------------


    INIT_PLAYERS,
    RECEIVE_INFO,
    UPDATE_STATE,
    SET_INITIAL_TICKETS,
    CHOOSE_INITIAL_TICKETS,
    NEXT_TURN,
    CHOOSE_TICKETS,
    DRAW_SLOT,
    ROUTE,
    CARDS,
    CHOOSE_ADDITIONAL_CARDS;


    //----------------------------------------------------------------------------------------------------

    /**
     * List of message Id
     */

    public static final List<MessageId> ALL = List.of(values());

    //----------------------------------------------------------------------------------------------------

    /**
     * The size of all message
     */

    public static final int COUNT = ALL.size();

    //----------------------------------------------------------------------------------------------------
}
