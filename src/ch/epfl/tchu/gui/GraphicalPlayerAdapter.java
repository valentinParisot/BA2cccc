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

    //put ou add ????
    //----------------------------------------------------------------------------------------------------

    private GraphicalPlayer graphicalPlayer;
    private final BlockingQueue<SortedBag<Ticket>> sortedBagArrayBlockingQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<GraphicalPlayer> graphicalPlayerArrayBlockingQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> claimedRoute = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> claimedCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> givenCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Integer> onDrawCardIntegerIndex = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<TurnKind> nextTurn = new ArrayBlockingQueue<>(1);

    //----------------------------------------------------------------------------------------------------

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        try {
            runLater(() ->
                    graphicalPlayerArrayBlockingQueue.add(new GraphicalPlayer(ownId, playerNames))
            );

            this.graphicalPlayer = graphicalPlayerArrayBlockingQueue.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }

    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> graphicalPlayer.setState(newState, ownState));
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        runLater(() ->
                graphicalPlayer.chooseTickets(tickets, (sortedBagArrayBlockingQueue::add))
        );
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Ticket> chooseInitialTickets() { // peut passer que par une queun ?

        BlockingQueue<SortedBag<Ticket>> sortedBagArrayBlockingQueue9 = new ArrayBlockingQueue<>(1);

        try {
            runLater(() ->
                    {
                        try {
                            sortedBagArrayBlockingQueue9.add(sortedBagArrayBlockingQueue.take());
                        } catch (InterruptedException e) {
                            throw new Error(e);
                        }
                    }
            );

            return sortedBagArrayBlockingQueue9.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    //----------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> ts) {

        try {
            runLater(() ->
                    graphicalPlayer.chooseTickets(ts, (sortedBagArrayBlockingQueue::put))
            );

            return sortedBagArrayBlockingQueue.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }

    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public int drawSlot() { return 0;
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public Route claimedRoute() {

        try {
            return claimedRoute.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }

    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Card> initialClaimCards() {

        try {
            return claimedCards.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {

        try {
            runLater(() ->
                    graphicalPlayer.chooseAdditionalCards(options, (givenCards::put))
            );

            return givenCards.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *  @Override
     *     public TurnKind nextTurn() {//comment gerer les diff handler et le type de retour avec if ?
     *
     *         try {
     *
     *             runLater(() ->
     *
     *                     graphicalPlayer.startTurn(, (onDrawCardIntegerIndex::add), )
     *                     graphicalPlayer.startTurn(nextTurn ,
     *                             o ->{
     *                         nextTurn
     *                         (onDrawCardIntegerIndex.add());
     *                             } ,
     *
     *                             nextTurn::add )
     *
     *             );
     *
     *             return nextTurn.take();
     *
     *         } catch (InterruptedException e) {
     *             throw new Error(e);
     *         }
     *     }
     */
}

