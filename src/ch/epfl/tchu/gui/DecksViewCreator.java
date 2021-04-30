package  ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Ticket;
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


public final class DecksViewCreator {


    public static HBox createHandView(ObservableGameState state) {


        HBox root = new HBox();
        root.getStylesheets().addAll("decks.css", "colors.css");

        /**ObservableList<String> names = FXCollections.observableArrayList();

        for (Ticket t : ChMap.tickets()) {
            names.add(t.toString());

        }
        ListView<String> billets = new ListView<>(names);

        for (Ticket t : ChMap.tickets()) {
            billets.getItems().add(t.toString());
        }

        billets.setId("tickets");
        root.getChildren().add(billets);**/


        HBox handPane = new HBox();
        handPane.setId("hand-pane");


        ListView<String> billets = new ListView<>();
        billets.setId("tickets");

       for (Ticket t : ChMap.tickets()) {

            billets.getItems().add(t.toString());

        }

        for (Card c: Card.ALL) {

            StackPane sp = new StackPane();


            if(c.color() == null){

                sp.getStyleClass().addAll("NEUTRAL", "card");

            }else {
                sp.getStyleClass().addAll(c.color().toString(), "card");
            }

            Rectangle r1 = new Rectangle();
            r1.setWidth(60);
            r1.setHeight(90);
            r1.getStyleClass().add("outside");

            Rectangle r2 = new Rectangle();
            r2.setWidth(40);
            r2.setHeight(70);
            r2.getStyleClass().addAll("filled","inside");

            Rectangle r3 = new Rectangle();
            r3.setWidth(40);
            r3.setHeight(70);
            r3.getStyleClass().add("train-image");

            Text compteurnoir = new Text();
            compteurnoir.getStyleClass().add("count");

            sp.getChildren().addAll(r1,r2,r3,compteurnoir);
            handPane.getChildren().add(sp);

        }

        root.getChildren().add(billets);
        root.getChildren().add(handPane);



        return root;





       /** Text counter = new Text();
        counter.getStyleClass().add("count");

        addWagonLoco(handPane, counter);


        root.getChildren().add(handPane);

        return root;**/
    }

    // "deux propriétés contenant chacune un gestionnaire d'action"?
    // afficher 1seule des 5cartes visibles
    public static VBox createCardsView(ObservableGameState state
                                       /*ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketProperty,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardProperty*/) {

        VBox root = new VBox();
        root.setId("card-pane");
        root.getStylesheets().addAll("decks.css", "colors.css");

        Button ticketButton = button();
        ticketButton.setText("Billets");
        root.getChildren().add(ticketButton);


        for (int i = 0; i <5 ; i++) {// a modifier pour prednre les faceup


            StackPane sp = new StackPane();
            sp.getStyleClass().addAll( "card");//ajouter la couelur

            /**if(c.color() == null){

             sp.getStyleClass().addAll("NEUTRAL", "card");

             }else {
             sp.getStyleClass().addAll(c.color().toString(), "card");
             }**/


            Rectangle r1 = new Rectangle();
            r1.setWidth(60);
            r1.setHeight(90);
            r1.getStyleClass().add("outside");

            Rectangle r2 = new Rectangle();
            r2.setWidth(40);
            r2.setHeight(70);
            r2.getStyleClass().addAll("filled", "inside");

            Rectangle r3 = new Rectangle();
            r3.setWidth(40);
            r3.setHeight(70);
            r3.getStyleClass().add("train-image");



            sp.getChildren().addAll(r1, r2, r3);
            root.getChildren().add(sp);
        }

            


        //addWagonLoco(root);

        Button cardButton = button();
        cardButton.setText("Cartes");
        root.getChildren().add(cardButton);


        return root;
    }

    private static void addWagonLoco(Pane root, Node node) {

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

    private static void addWagonLoco(Pane root) {

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

    private static Button button() {

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
