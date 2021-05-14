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
        runLater(() ->
                graphicalPlayerBlockingQueue.add(new GraphicalPlayer(ownId, playerNames))
        );

        try {
        graphicalPlayer = graphicalPlayerBlockingQueue.take();
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

        try {
            return ticketsBlockingQueue.take();

        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------


    @Override
    public TurnKind nextTurn() {//comment gerer les diff handler et le type de retour avec if ?

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

        try {

            return nextTurn.take();

        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> ts) {

            runLater(() ->

                    graphicalPlayer.chooseTickets(ts,ticketsBlockingQueue::add)
            );

        try {

            return ticketsBlockingQueue.take();

        } catch (InterruptedException e) {
            throw new Error(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public int drawSlot() {
        //tester comme ca sans bloquer ?


        if (cardsPlace.isEmpty()) {
            runLater(() ->
                    graphicalPlayer.drawCard(cardsPlace::add)
            );


        }
        try {

                return cardsPlace.take();


        } catch (InterruptedException e) {
            throw new Error(e);
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

            runLater(() ->
                    graphicalPlayer.chooseAdditionalCards(options,givenCards::add


                    )
            );
        try {

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

