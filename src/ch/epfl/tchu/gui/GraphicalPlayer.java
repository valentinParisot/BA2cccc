package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;

public class GraphicalPlayer {

    public GraphicalPlayer(PlayerId id){
        ObservableGameState gameState = new ObservableGameState(id);

    }
}
