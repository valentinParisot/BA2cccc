package  ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;


 class DecksViewCreator {

     private final static int BUTTON_WIDTH = 50;
     private final static int BUTTON_HEIGHT = 5;

    public static HBox createHandView(ObservableGameState state) {


        HBox root = new HBox();
        root.getStylesheets().addAll("decks.css", "colors.css");

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

            compteurnoir.textProperty()
                    .bind(Bindings.convert(state.cardMultiplicity(c))); // question

            compteurnoir.visibleProperty()
                    .bind(Bindings.greaterThan(state.cardMultiplicity(c), 1));

            sp.getChildren().addAll(r1,r2,r3,compteurnoir);
            handPane.getChildren().add(sp);

            sp.visibleProperty()
                    .bind(Bindings.greaterThan(state.cardMultiplicity(c), 0));

        }

        root.getChildren().add(billets);
        root.getChildren().add(handPane);

        return root;
    }

    // "deux propriétés contenant chacune un gestionnaire d'action"?
    // afficher 1seule des 5cartes visibles
    public static VBox createCardsView(ObservableGameState state,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketProperty,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardProperty) {

        VBox root = new VBox();
        root.setId("card-pane");
        root.getStylesheets().addAll("decks.css", "colors.css");

        Button ticketButton = new Button(StringsFr.TICKETS);
        ticketButton.getStyleClass().add("gauged");

        Rectangle backTicket = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backTicket.getStyleClass().add("background");

        Rectangle gaugeTicket = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        gaugeTicket.widthProperty()
                .bind(state.ticketPercentage().multiply(50).divide(100));
        gaugeTicket.getStyleClass().add("foreground");


        ticketButton.setGraphic(new Group(backTicket, gaugeTicket));

        ticketButton.disableProperty()
                .bind(ticketProperty.isNull());

        ticketButton.setOnMouseClicked(e -> {
            ticketProperty.get().onDrawTickets();
        });


        root.getChildren().add(ticketButton);


        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; i++) {// a modifier pour prednre les faceup


            StackPane sp = new StackPane();
            sp.getStyleClass().addAll( "card");//ajouter la couelur

            state.faceUpCard(i).addListener((o,ov,on) ->{ // question

                sp.getStyleClass().setAll(on.color().toString(), "card");

            });

            sp.disableProperty()
                    .bind(cardProperty.isNull());

            cardProperty.get().onDrawCard(i);

            addWagonLoco(sp, root);
        }

        Button cardButton = new Button(StringsFr.CARDS);
        cardButton.getStyleClass().add("gauged");

        Rectangle backCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backCard.getStyleClass().add("background");

        Rectangle gaugeCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);

        gaugeCard.widthProperty()
                .bind(state.cardPercentage().multiply(50).divide(100));
        gaugeCard.getStyleClass().add("foreground");

        cardButton.setGraphic(new Group(backCard, gaugeCard));
        cardButton.setText(StringsFr.CARDS);

        cardButton.disableProperty()
                .bind(cardProperty.isNull());

        cardButton.setOnMouseClicked(e -> {
            cardProperty.get().onDrawCard(-1);
        });

        root.getChildren().add(cardButton);


        return root;
    }

    private static void addWagonLoco(StackPane sp,  VBox root) {

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

    private static void addWagonLoco(StackPane sp, HBox handPane) {

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

        Text compteurnoir = new Text();
        compteurnoir.getStyleClass().add("count");

        sp.getChildren().addAll(r1,r2,r3,compteurnoir);
        handPane.getChildren().add(sp);

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
