package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import javafx.collections.ObservableList;

public class ObservableGameState {

    private final PlayerId playerId;
    private final PlayerState playerState;
    private final PublicGameState gameState;

    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
        this.playerState = null;
        this.gameState = null;
    }

    public void setState(PublicGameState gameState, PlayerState playerState){

    }
}
