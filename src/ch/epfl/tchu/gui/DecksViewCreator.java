package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Ticket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.util.List;

class DecksViewCreator {

    public HBox createHandView(ObservableGameState state){

        HBox handView = new HBox();
        handView.getStylesheets().add("decks.css");
        handView.getStylesheets().add("colors.css");

        ListView<String> ticketView = new ListView<String>();
        ticketView.getStyleClass().add("tickets");

        HBox handPane = new HBox();
        handPane.getStyleClass().add("hand-pane");

        handView.getChildren().add(ticketView);
        handView.getChildren().add(handPane);

        return handView;
    }
    //public createCardsView()

}
