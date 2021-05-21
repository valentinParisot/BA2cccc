package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.IntegerProperty;
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
    private final static int R1_WIDTH = 60;
    private final static int R1_HEIGHT = 90;
    private final static int R2_WIDTH = 40;
    private final static int R2_HEIGHT = 70;
    private final static int R3_WIDTH = 40;
    private final static int R3_HEIGHT = 70;

    private final static String HAND_PANE = "hand-pane";
    private final static String TICKETS = "tickets";
    private final static String CARD_PANE = "card-pane";
    private final static String OUTSIDE = "outside";
    private final static String FILLED = "filled";
    private final static String INSIDE = "inside";
    private final static String COUNT = "count";
    private final static String TRAIN_IMAGE = "train-image";
    private final static String GAUGED = "gauged";
    private final static String BACKGROUND = "background";
    private final static String FOREGROUND = "foreground";
    private final static String CARD = "card";
    private final static String NEUTRAL = "NEUTRAL";
    private final static String DECKS = "decks.css";
    private final static String COLORS = "colors.css";

    //----------------------------------------------------------------------------------------------------

    /**
     * creates the private view of a player's hand
     *
     * @param state the actual state of the game we want to show
     * @return the box of the hand view
     */
    public static HBox createHandView(ObservableGameState state) {

        HBox root = new HBox();
        root.getStylesheets().addAll(DECKS, COLORS);

        HBox handPane = new HBox();
        handPane.setId(HAND_PANE);

        ListView<Ticket> billets = new ListView<>(state.ticketList());
        billets.setId(TICKETS);

        for (Card c : Card.ALL) {

            StackPane sp = new StackPane();

            if (c.color() == null) {
                sp.getStyleClass().addAll(NEUTRAL, CARD);

            } else {
                sp.getStyleClass().addAll(c.color().toString(), CARD);
            }

            sp.getChildren().addAll(
                    rOutside(),
                    rFilledInside(),
                    rTrainImage(),
                    blackCounter(state, c));

            handPane.getChildren().add(sp);

            sp.visibleProperty()
                    .bind(Bindings.greaterThan(state.cardMultiplicity(c), 0));

        }

        root.getChildren().addAll(billets, handPane);

        return root;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Main box of the deck view
     *
     * @param state          the actual state
     * @param ticketProperty enables/disables the ticket button
     * @param cardProperty   enables/disables the card button
     * @return the box of the decks view
     */
    public static VBox createCardsView(ObservableGameState state,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> ticketProperty,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> cardProperty) {

        VBox root = new VBox();
        root.setId(CARD_PANE);
        root.getStylesheets().addAll(DECKS, COLORS);

        // creates the draw button for the tickets

        Button ticketButton = createButton(StringsFr.TICKETS,
                percentage((IntegerProperty) state.ticketPercentage()));

        ticketButton.disableProperty()
                .bind(ticketProperty.isNull());

        ticketButton.setOnMouseClicked(e -> ticketProperty.get().onDrawTickets());


        root.getChildren().add(ticketButton);

        // creates FaceUpCards

        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; i++) {


            StackPane sp = new StackPane();
            sp.getStyleClass().add(CARD);

            state.faceUpCard(i).addListener((o, ov, on) -> {

                String color = (on.color() == null) ? NEUTRAL : on.color().toString();

                sp.getStyleClass().setAll(color, CARD);

            });

            sp.disableProperty()
                    .bind(cardProperty.isNull());

            int finalI = i;
            sp.setOnMouseClicked(e -> cardProperty.get().onDrawCard(finalI));

            addWagonLoco(sp, root);
        }

        // creates the draw button for the cards

        Button cardButton = createButton(StringsFr.CARDS,
                percentage((IntegerProperty) state.cardPercentage()));

        cardButton.disableProperty()
                .bind(cardProperty.isNull());

        cardButton.setOnMouseClicked(e -> cardProperty.get().onDrawCard(-1));

        root.getChildren().add(cardButton);

        return root;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create rectangle with outside orientation
     *
     * @return a Rectangle with "outside"
     */

    private static Rectangle rOutside() {
        Rectangle r1 = new Rectangle();
        r1.setWidth(R1_WIDTH);
        r1.setHeight(R1_HEIGHT);
        r1.getStyleClass().add(OUTSIDE);
        return r1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create rectangle filled inside
     *
     * @return a Rectangle with "filled" and "inside"
     */

    private static Rectangle rFilledInside() {
        Rectangle r2 = new Rectangle();
        r2.setWidth(R2_WIDTH);
        r2.setHeight(R2_HEIGHT);
        r2.getStyleClass().addAll(FILLED, INSIDE);
        return r2;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create rectangle with train image
     *
     * @return a Rectangle with "train-image"
     */

    private static Rectangle rTrainImage() {
        Rectangle r3 = new Rectangle();
        r3.setWidth(R3_WIDTH);
        r3.setHeight(R3_HEIGHT);
        r3.getStyleClass().add(TRAIN_IMAGE);
        return r3;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create text with the multiplicity of the given card
     *
     * @param state the actual ObservableGameState
     * @param card  a card in the player's hand
     * @return the Text corresponding to the multiplicity of @card
     */

    private static Text blackCounter(ObservableGameState state, Card card) {
        Text blackCounter = new Text();
        blackCounter.getStyleClass().add(COUNT);

        ReadOnlyIntegerProperty count = state.cardMultiplicity(card);

        blackCounter.textProperty()
                .bind(Bindings.convert(count));

        blackCounter.visibleProperty()
                .bind(Bindings.greaterThan(count, 1));

        return blackCounter;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Adds the Rectangles corresponding to wagons and locomotives images to @sp and add @sp to @root
     *
     * @param sp   a  StackPane
     * @param root a Vbox
     */

    private static void addWagonLoco(StackPane sp, VBox root) {
        Rectangle r1 = new Rectangle();
        r1.setWidth(R1_WIDTH);
        r1.setHeight(R1_HEIGHT);
        r1.getStyleClass().add(OUTSIDE);

        Rectangle r2 = new Rectangle();
        r2.setWidth(R2_WIDTH);
        r2.setHeight(R2_HEIGHT);
        r2.getStyleClass().addAll(FILLED, INSIDE);

        Rectangle r3 = new Rectangle();
        r3.setWidth(R3_WIDTH);
        r3.setHeight(R3_HEIGHT);
        r3.getStyleClass().add(TRAIN_IMAGE);

        sp.getChildren().addAll(r1, r2, r3);
        root.getChildren().add(sp);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create a button with a given name
     *
     * @param name    the button's name
     * @param binding the property of the button's gauge
     * @return a new gauged Button
     */

    private static Button createButton(String name, IntegerBinding binding) {

        Button button = new Button(name);
        button.getStyleClass().add(GAUGED);

        Rectangle backCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backCard.getStyleClass().add(BACKGROUND);

        Rectangle gaugeCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);

        gaugeCard.widthProperty()
                .bind(binding);
        gaugeCard.getStyleClass().add(FOREGROUND);

        button.setGraphic(new Group(backCard, gaugeCard));

        return button;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create an percentage
     *
     * @param property any IntegerProperty
     * @return the property into percentage
     */

    private static IntegerBinding percentage(IntegerProperty property) {
        return property.multiply(50).divide(100);
    }

    //----------------------------------------------------------------------------------------------------

}
