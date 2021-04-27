package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

class DecksViewCreator {

    public HBox createHandView(ObservableGameState state){ // type de retour void?

        HBox root = new HBox();
        root.getStylesheets().addAll("decks.css", "colors.css");


        ListView<String> ticketView = new ListView<String>();
        ticketView.setId("tickets");
        root.getChildren().add(ticketView);


        HBox handPane = new HBox();
        handPane.setId("hand-pane");

        Text counter = new Text();
        counter.getStyleClass().add("count");

        addWagonLoco(handPane, counter);


        root.getChildren().add(handPane);

        return root;
    }
    public VBox createCardsView(ObservableGameState state){ // "deux propriétés contenant chacune un gestionnaire d'action"?

        VBox root = new VBox();
        root.setId("card-pane");
        root.getStylesheets().addAll("decks.css", "colors.css");

        addWagonLoco(root);

        Button ticketButton = button();
        root.getChildren().add(ticketButton);

        Button cardButton = button();
        root.getChildren().add(cardButton);



        return root;
    }

    private void addWagonLoco(Pane root, Node node){

        Rectangle r1 = new Rectangle();
        r1.setWidth(60);
        r1.setHeight(90);
        r1.getStyleClass().add("outside");

        Rectangle r2 = new Rectangle();
        r1.setWidth(40);
        r1.setHeight(70);
        r1.getStyleClass().addAll("filled", "inside");

        Rectangle r3 = new Rectangle();
        r1.setWidth(40);
        r1.setHeight(70);
        r1.getStyleClass().add("train-image");

        for (Card card : Card.ALL) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll("card", card.name());
            stackPane.getChildren().addAll(r1, r2, r3, node);

            root.getChildren().add(stackPane);
        }
    }

    private void addWagonLoco(Pane root){

        Rectangle r1 = new Rectangle();
        r1.setWidth(60);
        r1.setHeight(90);
        r1.getStyleClass().add("outside");

        Rectangle r2 = new Rectangle();
        r1.setWidth(40);
        r1.setHeight(70);
        r1.getStyleClass().addAll("filled", "inside");

        Rectangle r3 = new Rectangle();
        r1.setWidth(40);
        r1.setHeight(70);
        r1.getStyleClass().add("train-image");

        for (Card card : Card.ALL) {
            StackPane stackPane = new StackPane();
            stackPane.getStyleClass().addAll("card", card.name());
            stackPane.getChildren().addAll(r1, r2, r3);

            root.getChildren().add(stackPane);
        }
    }

    private Button button(){

        Button button = new Button();
        button.getStyleClass().add("gauged");

        Rectangle back = new Rectangle();
        back.setHeight(5);
        back.setWidth(50);
        back.getStyleClass().add("background");

        Rectangle fore = new Rectangle();
        fore.setHeight(5);
        fore.setWidth(50);
        fore.getStyleClass().add("foreground");

        Group graphic = new Group();
        graphic.getChildren().addAll(back, fore);

        button.setGraphic(graphic);

        return button;

    }

}
