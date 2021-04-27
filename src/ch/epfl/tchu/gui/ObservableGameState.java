package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PublicGameState;
import javafx.collections.ObservableList;

public class ObservableGameState {

    private final PlayerId playerId;

    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
    }

    public ObservableGameState setState(PublicGameState gameState){
        return null;
    }
}
