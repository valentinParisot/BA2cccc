package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.util.Map;

import static javafx.application.Platform.isFxApplicationThread;

public class GraphicalPlayer {

    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;
    private ObservableList<Text> observableList;
    private ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerOP;
    private ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerOP;
    private ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerOP;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){

        observableGameState = new ObservableGameState(playerId);
        this.playerId = playerId;
        this.playerNames = playerNames;

        drawTicketsHandlerOP = new SimpleObjectProperty<ActionHandlers.DrawTicketsHandler>();
        drawCardHandlerOP = new SimpleObjectProperty<ActionHandlers.DrawCardHandler>();
        claimRouteHandlerOP = new SimpleObjectProperty<ActionHandlers.ClaimRouteHandler>();
    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState){
        assert isFxApplicationThread();
        observableGameState.setState(newGameState, newPlayerState);
    }

    public void receiveInfo(String message){
        assert isFxApplicationThread();
        observableList.add(new Text(message)); // comment en avoir 5 maximum? avoir accès aux 4 autres infos?
        InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, observableList);

    }

    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler,
                          ActionHandlers.DrawCardHandler drawCardHandler,
                          ActionHandlers.ClaimRouteHandler claimRouteHandler){

        assert isFxApplicationThread();

        if (observableGameState.canDrawTickets()) {
            drawTicketsHandlerOP.set(drawTicketsHandler); // reset juste après... quel intérêt?
            resetHandlers();
        }
        if (observableGameState.canDrawCards()){
            drawCardHandlerOP.set(drawCardHandler);
            resetHandlers();
        }
        claimRouteHandlerOP.set(claimRouteHandler);
        resetHandlers();

    }

    private void chooseTicket(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler){
        assert tickets.size() == 3 || tickets.size() == 5;

    }

    private void resetHandlers(){
        drawTicketsHandlerOP.set(null);
        drawCardHandlerOP.set(null);
        claimRouteHandlerOP.set(null);
    }

}
