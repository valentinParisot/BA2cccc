package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * ActionHandlers
 * interface
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public interface ActionHandlers {

    //----------------------------------------------------------------------------------------------------

    @FunctionalInterface
    interface DrawTicketsHandler {

        /**
         * Useful when the player wants to draw tickets
         */
        void onDrawTickets();

    }

    //----------------------------------------------------------------------------------------------------

    @FunctionalInterface
    interface DrawCardHandler {

        /**
         * when the player wishes to draw a card from the given location
         *
         * @param index given location ( between 0 and 4, or -1)
         */
        void onDrawCard(int index);

    }

    //----------------------------------------------------------------------------------------------------

    @FunctionalInterface
    interface ClaimRouteHandler {

        /**
         * is called when the player wishes to seize the given route by means of the given (initial) cards,
         *
         * @param route   desired route
         * @param initial given cards
         */
        void onClaimRoute(Route route, SortedBag<Card> initial);

    }

    //----------------------------------------------------------------------------------------------------

    @FunctionalInterface
    interface ChooseTicketsHandler {

        /**
         * is called when the player has chosen to keep the tickets given following a ticket draw,
         *
         * @param chooseTickets tickets given
         */
        void onChooseTickets(SortedBag<Ticket> chooseTickets);

    }

    //----------------------------------------------------------------------------------------------------

    @FunctionalInterface
    interface ChooseCardsHandler {

        /**
         * is called when the player has chosen to use the given cards as initial
         * or additional cards when taking possession of a route; if they are additional cards,
         * then the multiset may be empty, which means that the player gives up taking the tunnel
         *
         * @param givenCards given cards
         */
        void onChooseCards(SortedBag<Card> givenCards);

    }

    //----------------------------------------------------------------------------------------------------
}
