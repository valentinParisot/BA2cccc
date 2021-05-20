package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * DecksViewCreator
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

class DecksViewCreator {

    //----------------------------------------------------------------------------------------------------

    private final static int BUTTON_WIDTH = 50;
    private final static int BUTTON_HEIGHT = 5;

    //----------------------------------------------------------------------------------------------------

    /**
     * creates the private view of a player's hand
     * @param state the actual state of the game we want to show
     * @return
     */

    public static HBox createHandView(ObservableGameState state) {

        HBox root = new HBox();
        root.getStylesheets().addAll("decks.css", "colors.css");

        HBox handPane = new HBox();
        handPane.setId("hand-pane");

        ListView<Ticket> billets = new ListView<>(state.ticketList());
        billets.setId("tickets");

        for (Card c : Card.ALL) {

            StackPane sp = new StackPane();

            if (c.color() == null) {
                sp.getStyleClass().addAll("NEUTRAL", "card");

            } else {
                sp.getStyleClass().addAll(c.color().toString(), "card");
            }

            sp.getChildren().addAll(
                    rOutside(),
                    rFilledInside(),
                    rTrainImage(),
                    compteurNoir(state,c));

            handPane.getChildren().add(sp);

            sp.visibleProperty()
                    .bind(Bindings.greaterThan(state.cardMultiplicity(c), 0));

        }

        root.getChildren().addAll(billets,handPane);

        return root;
    }

    //----------------------------------------------------------------------------------------------------

    public static VBox createCardsView(ObservableGameState state,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketProperty,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardProperty) {

        VBox root = new VBox();
        root.setId("card-pane");
        root.getStylesheets().addAll("decks.css", "colors.css");

        // crée le bouton de la pioche des tickets

        Button ticketButton = createButton(StringsFr.TICKETS,
                state.ticketPercentage().multiply(50).divide(100));

        ticketButton.disableProperty()
                .bind(ticketProperty.isNull());

        ticketButton.setOnMouseClicked(e -> {
            ticketProperty.get().onDrawTickets();
        });


        root.getChildren().add(ticketButton);

        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; i++) { // crée les FaceUpCards


            StackPane sp = new StackPane();
            sp.getStyleClass().add("card");

            state.faceUpCard(i).addListener((o, ov, on) -> {

                if (on.color() == null) {
                    sp.getStyleClass().setAll("NEUTRAL", "card");
                } else
                    sp.getStyleClass().setAll(on.color().toString(), "card");

            });

            sp.disableProperty()
                    .bind(cardProperty.isNull());

            int finalI = i;
            sp.setOnMouseClicked(e -> {
                cardProperty.get().onDrawCard(finalI);
            });

            addWagonLoco(sp, root);
        }

        // crée le bouton de a pioche des cartes

        Button cardButton = createButton(StringsFr.CARDS,
                state.cardPercentage().multiply(50).divide(100));

        cardButton.disableProperty()
                .bind(cardProperty.isNull());

        cardButton.setOnMouseClicked(e -> {
            cardProperty.get().onDrawCard(-1);
        });

        root.getChildren().add(cardButton);

        return root;
    }

    //----------------------------------------------------------------------------------------------------

    private static Rectangle rOutside(){
        Rectangle r1 = new Rectangle();
        r1.setWidth(60);
        r1.setHeight(90);
        r1.getStyleClass().add("outside");
        return r1;
    }

    private static Rectangle rFilledInside(){
        Rectangle r2 = new Rectangle();
        r2.setWidth(40);
        r2.setHeight(70);
        r2.getStyleClass().addAll("filled", "inside");
        return r2;
    }

    private static Rectangle rTrainImage(){
        Rectangle r3 = new Rectangle();
        r3.setWidth(40);
        r3.setHeight(70);
        r3.getStyleClass().add("train-image");
        return r3;
    }

    private static Text compteurNoir(ObservableGameState state, Card c){
        Text compteurnoir = new Text();
        compteurnoir.getStyleClass().add("count");

        ReadOnlyIntegerProperty count = state.cardMultiplicity(c);

        compteurnoir.textProperty()
                .bind(Bindings.convert(count));

        compteurnoir.visibleProperty()
                .bind(Bindings.greaterThan(count, 1));

        return compteurnoir;
    }

    private static void addWagonLoco(StackPane sp, VBox root) {

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

    private static Button createButton(String name, IntegerBinding binding) {

        Button button = new Button(name);
        button.getStyleClass().add("gauged");

        Rectangle backCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backCard.getStyleClass().add("background");

        Rectangle gaugeCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);

        gaugeCard.widthProperty()
                .bind(binding);
        gaugeCard.getStyleClass().add("foreground");

        button.setGraphic(new Group(backCard, gaugeCard));

        return button;
    }

    //----------------------------------------------------------------------------------------------------

}
