package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;
    private final BlockingQueue<ActionHandlers.ChooseTicketsHandler> q = new ArrayBlockingQueue<>(1);




    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        runLater(() -> this.graphicalPlayer = new GraphicalPlayer(ownId,playerNames) );
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> ts) {

        BlockingQueue<SortedBag<Ticket>> r = new ArrayBlockingQueue<>(1);//??
        //ou constructeur

        try {
                runLater(() ->
                    graphicalPlayer.chooseTickets(ts, r::add )
                );

            return r.take();
        }
        catch (InterruptedException e){
          throw new Error(e);
        }


    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }
}
