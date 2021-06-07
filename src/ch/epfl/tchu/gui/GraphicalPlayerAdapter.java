package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

/**
 * GraphicalPlayerAdapter
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class GraphicalPlayerAdapter implements Player {

    //----------------------------------------------------------------------------------------------------

    private final static int CAPACITY = 1;

    private GraphicalPlayer graphicalPlayer;

    public GraphicalPlayer getGraphicalPlayer() {
        return graphicalPlayer;
    }

    private final boolean isChinese;

    private final BlockingQueue<SortedBag<Ticket>> ticketsBlockingQueue = new ArrayBlockingQueue<>(CAPACITY);
    private final BlockingQueue<GraphicalPlayer> graphicalPlayerBlockingQueue = new ArrayBlockingQueue<>(CAPACITY);
    private final BlockingQueue<Route> claimedRoute = new ArrayBlockingQueue<>(CAPACITY);
    private final BlockingQueue<SortedBag<Card>> claimedCards = new ArrayBlockingQueue<>(CAPACITY);
    private final BlockingQueue<SortedBag<Card>> givenCards = new ArrayBlockingQueue<>(CAPACITY);
    private final BlockingQueue<Integer> cardsPlace = new ArrayBlockingQueue<>(CAPACITY);
    private final BlockingQueue<TurnKind> nextTurn = new ArrayBlockingQueue<>(CAPACITY);

    //----------------------------------------------------------------------------------------------------


    public GraphicalPlayerAdapter(boolean isChinese){

        this.isChinese = isChinese;
    }

    /**
     * Builds, on the JavaFX thread, the instance of the graphical player.
     *
     * @param ownId       the ID of the player.
     * @param playerNames the name of each player.
     */

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() ->
            graphicalPlayerBlockingQueue.add(new GraphicalPlayer(ownId, playerNames, isChinese))
        );

        graphicalPlayer = tryCatch(graphicalPlayerBlockingQueue);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication to receive an info.
     *
     * @param info any information the player should receive.
     */

    @Override
    public void receiveInfo(String info) {
        runLater(() ->
                graphicalPlayer.receiveInfo(info));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make update the state.
     *
     * @param newState the new state of the game.
     * @param ownState the new state of the player.
     */

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() ->
                graphicalPlayer.setState(newState, ownState));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Calls, on the JavaFX thread, the chooseTickets method of the graphic player,
     * to ask him to choose his initial tickets.
     *
     * @param tickets the 5 initial tickets distributed to the player.
     */

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() ->

            graphicalPlayer.chooseTickets(tickets, (ticketsBlockingQueue::add))
        );
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Blocks while waiting for the queue also used by setInitialTicketChoice to contain a value,
     * then returns it.
     *
     * @return sorted bag from setInitialTicketChoice.
     */

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {

        return tryCatch(ticketsBlockingQueue);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Calls, on the JavaFX thread, the startTurn method of the graphic player,
     * passing it action managers which place the type of turn chosen, as well as any "arguments" of the action.
     *
     * @return kind of the action that the player want to do.
     */

    @Override
    public TurnKind nextTurn() {
        runLater(() ->
                graphicalPlayer.startTurn(
                        () -> nextTurn.add(TurnKind.DRAW_TICKETS)
                        ,
                        (o) -> {
                            cardsPlace.add(o);
                            nextTurn.add(TurnKind.DRAW_CARDS);
                        }
                        ,
                        (o, p) -> {
                            claimedRoute.add(o);
                            claimedCards.add(p);
                            nextTurn.add(TurnKind.CLAIM_ROUTE);
                        }));

        return tryCatch(nextTurn);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Chain the actions performed by setInitialTicketChoice and chooseInitialTickets.
     *
     * @param tickets sorted bag of ticket that can be choose.
     * @return sorted bag of chosen tickets.
     */

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> tickets) {
        runLater(() ->
                graphicalPlayer.chooseTickets(tickets, ticketsBlockingQueue::add)
        );

        return tryCatch(ticketsBlockingQueue);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Tests if the queue containing the card slots contains a value
     * if this is the case, it means that drawSlot is called for the first time of the turn,
     * and that the manager installed by nextTurn has placed the location of the first card drawn in this queue,
     * which therefore suffices to return; otherwise, it means that drawSlot is called for the second time of the turn,
     * so that the player draws his second card, and we must therefore call, on the JavaFX thread,
     * the drawCard method of the graphic player.
     * <p>
     * Before blocking while waiting for the manager that we pass it places the location of the drawn card in the queue,
     * which is then extracted and returned.
     *
     * @return the location of the drawn card in the queue.
     */

    @Override
    public int drawSlot() {
        if (cardsPlace.isEmpty()) {
            runLater(() ->
                    graphicalPlayer.drawCard(cardsPlace::add)
            );
        }

        return tryCatch(cardsPlace);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Extracts and returns the first element of the queue containing the routes,
     * which will have been placed there by the manager passed to startTurn by nextTurn.
     *
     * @return the first element of the queue containing the routes.
     */

    @Override
    public Route claimedRoute() {

        return tryCatch(claimedRoute);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Is similar to claimedRoute but uses the queue containing multiSets of maps.
     *
     * @return the first element of the containing multiSets of maps containing the routes.
     */

    @Override
    public SortedBag<Card> initialClaimCards() {

        return tryCatch(claimedCards);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Calls, on the JavaFX thread, the method of the same name of the graphics player then blocks while waiting
     * for an element to be placed in the queue containing the multiSets of cards, which it returns
     *
     * @param options the additional cards proposed to the player.
     * @return element placed in the queue containing the multiSets of cards.
     */

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() ->
                graphicalPlayer.chooseAdditionalCards(options, givenCards::add)
        );
        return tryCatch(givenCards);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Simplify thw way to check the try and catch.
     */

    private static <T> T tryCatch(BlockingQueue<T> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

}

