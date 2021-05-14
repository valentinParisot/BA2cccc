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
    private final BlockingQueue<SortedBag<Ticket>> ticketsBlockingQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<GraphicalPlayer> graphicalPlayerBlockingQueue = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> claimedRoute = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> claimedCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> givenCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Integer> cardsPlace = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<TurnKind> nextTurn = new ArrayBlockingQueue<>(1);

    //----------------------------------------------------------------------------------------------------


    public GraphicalPlayerAdapter() {

    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        //definir ds lambda ou ici ?
        try {
            runLater(() ->
                    graphicalPlayerBlockingQueue.add(new GraphicalPlayer(ownId, playerNames))
            );

            this.graphicalPlayer = graphicalPlayerBlockingQueue.take();

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

                graphicalPlayer.chooseTickets(tickets, (ticketsBlockingQueue::add))
        );
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Ticket> chooseInitialTickets() { // peut passer que par une queun ?

        BlockingQueue<SortedBag<Ticket>> ticketsBlockingQueue2 = new ArrayBlockingQueue<>(1);

        try {
            runLater(() ->
                    {
                        try {
                            ticketsBlockingQueue2.add(this.ticketsBlockingQueue.take());
                        } catch (InterruptedException e) {
                            throw new Error(e);
                        }
                    }
            );

            return ticketsBlockingQueue2.take();

        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------


    @Override
    public TurnKind nextTurn() {//comment gerer les diff handler et le type de retour avec if ?

        try {

            runLater(() ->

                    graphicalPlayer.startTurn

                            (

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
                                    }


                            )
            );

            return nextTurn.take();

        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> ts) {

        try {
            runLater(() ->

                    graphicalPlayer.chooseTickets(ts,

                            g -> {

                                try {
                                    ticketsBlockingQueue.put(g);
                                } catch (InterruptedException e) {
                                    throw new Error(e);
                                }

                            }
                    )
            );

            return ticketsBlockingQueue.take();

        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public int drawSlot() {
        //tester comme ca sans bloquer ?

        if (!cardsPlace.isEmpty()) {

            try {
                return cardsPlace.take();

            } catch (InterruptedException e) {
                throw new Error(e);
            }

        } else {
            try {
                runLater(() ->
                        graphicalPlayer.drawCard(cardsPlace::add)
                );

                return cardsPlace.take();

            } catch (InterruptedException e) {
                throw new Error(e);
            }
        }

    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public Route claimedRoute() {

        //passer par run later ?
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

        //checker avec preconditon si la file est vide
        try {
            runLater(() ->
                    graphicalPlayer.chooseAdditionalCards(options,

                            g -> {

                                try {
                                    givenCards.put(g);

                                } catch (InterruptedException e) {
                                    throw new Error(e);
                                }
                            }


                    )
            );

            return givenCards.take();
        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Simplify thw way to check the try and catch
     */
    private void tryCatch() {

    }
}

