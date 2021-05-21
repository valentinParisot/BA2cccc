package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Constants.INITIAL_TICKETS_COUNT;
import static ch.epfl.tchu.game.Constants.IN_GAME_TICKETS_COUNT;
import static javafx.application.Platform.isFxApplicationThread;

/**
 * GraphicalPlayer
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class GraphicalPlayer {

    //----------------------------------------------------------------------------------------------------

    private final static String PREFIX_TITLE = "tCHu - ";
    private final static String CHOOSER_CSS = "chooser.css";

    private final static int MAX_TEXT_SIZE = 5;

    private final ObservableGameState observableGameState;
    private final ObservableList<Text> textList;
    private final ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerOP;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerOP;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerOP;

    private final Stage primaryStage;

    //----------------------------------------------------------------------------------------------------

    /**
     * @param playerId    the id corresponding to the player watching
     * @param playerNames the names of all players
     */

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames) {

        observableGameState = new ObservableGameState(playerId);

        textList = FXCollections.observableArrayList();

        drawTicketsHandlerOP = new SimpleObjectProperty<>();
        drawCardHandlerOP = new SimpleObjectProperty<>();
        claimRouteHandlerOP = new SimpleObjectProperty<>();
        primaryStage = new Stage();
        primaryStage.setTitle(PREFIX_TITLE + playerNames.get(playerId));

        Node mapView = MapViewCreator.createMapView(observableGameState, claimRouteHandlerOP, (this::chooseClaimCards));
        Node cardsView = DecksViewCreator.createCardsView(observableGameState, drawTicketsHandlerOP, drawCardHandlerOP);
        Node handView = DecksViewCreator.createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, textList);

        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        primaryStage.setScene(new Scene(borderPane));

        primaryStage.show();

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Set the state
     *
     * @param newGameState   the new state of the game
     * @param newPlayerState the new state of the player
     */

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert isFxApplicationThread();
        observableGameState.setState(newGameState, newPlayerState);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Adding at the bottom of the game progress information,
     * which is presented in the lower part of the information view;
     * this view should only contain the last five messages received
     *
     * @param message the message we received
     */

    public void receiveInfo(String message) {
        assert isFxApplicationThread();

        Text text = new Text(message);
        textList.add(text);
        if (textList.size() > MAX_TEXT_SIZE) {
            textList.remove(0);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Start the turn
     *
     * @param drawTicketsHandler enables/disables the draw of tickets
     * @param drawCardHandler    enables/disables the draw of cards
     * @param claimRouteHandler  enables/disables the claim of a route
     */

    public void startTurn(ActionHandlers.DrawTicketsHandler drawTicketsHandler,
                          ActionHandlers.DrawCardHandler drawCardHandler,
                          ActionHandlers.ClaimRouteHandler claimRouteHandler) {

        assert isFxApplicationThread();

        if (observableGameState.canDrawTickets()) {

            drawTicketsHandlerOP.set(() -> {
                        resetHandlers();
                        drawTicketsHandler.onDrawTickets();
                    }
            );
        }

        if (observableGameState.canDrawCards()) {

            drawCardHandlerOP.set((i) -> {
                        resetHandlers();
                        drawCardHandler.onDrawCard(i);
                    }
            );
        }

        claimRouteHandlerOP.set((route, initial) -> {
                    resetHandlers();
                    claimRouteHandler.onClaimRoute(route, initial);
                }
        );
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Opens a window allowing the player to make his choice once this is confirmed
     * the choice handler is called with this choice as an argument
     *
     * @param tickets              the tickets proposed to the player
     * @param chooseTicketsHandler chooses the tickets
     */

    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler) {
        assert isFxApplicationThread();
        Preconditions.checkArgument(tickets.size() == IN_GAME_TICKETS_COUNT ||
                tickets.size() == INITIAL_TICKETS_COUNT);

        int minimumTicketSize = tickets.size() - 2;
        int minimumTicketCount = tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT;

        Stage ticketStage = createStage(StringsFr.TICKETS_CHOICE);
        VBox vBox = new VBox();

        TextFlow textFlow = createTextFlow(String.format(
                StringsFr.CHOOSE_TICKETS,
                minimumTicketCount,
                StringsFr.plural(tickets.size())));

        Button ticketButton = new Button(StringsFr.CHOOSE);
        ListView<Ticket> listView = new ListView<>(FXCollections.observableList(tickets.toList()));

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ticketButton.disableProperty()
                .bind(Bindings.lessThan(Bindings.size(
                        listView.getSelectionModel().getSelectedItems()), minimumTicketSize));

        ticketStage.setOnCloseRequest(Event::consume);
        ticketButton.setOnAction(e -> {
            ticketStage.hide();
            chooseTicketsHandler.onChooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems()));

        });

        vBox.getChildren().addAll(textFlow, listView, ticketButton);
        createScene(ticketStage, vBox);
        ticketStage.show();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Allows the player to choose a wagon / locomotive card, either one of the five whose side is visible,
     * or the one at the top of the deck once the player has clicked on one of these cards,
     * the manager is called with the player's choice; this method is meant to be called when the player has already
     * drawn a first card and must now draw the second
     *
     * @param drawCardHandler draws a card
     */

    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler) {
        assert isFxApplicationThread();

        drawCardHandlerOP.set((i) -> {
                    resetHandlers();
                    drawCardHandler.onDrawCard(i);
                }
        );
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Opens a window allowing the player to make his choice;
     * once this has been done and confirmed, the choice handler is called with the player's choice as an argument;
     *
     * @param initialCards       all the possible cards for the player to take a route
     * @param chooseCardsHandler chooses the cards
     */

    public void chooseClaimCards(List<SortedBag<Card>> initialCards,
                                 ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();

        Stage cardStage = createStage(StringsFr.CARDS_CHOICE);
        VBox vBox = new VBox();
        TextFlow textFlow = createTextFlow(StringsFr.CHOOSE_CARDS);
        Button cardButton = new Button(StringsFr.CHOOSE);
        ListView<SortedBag<Card>> listView = createListView(initialCards);


        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        cardButton.disableProperty()
                .bind(Bindings.size(listView.getSelectionModel().getSelectedItems()).lessThan(1));

        cardStage.setOnCloseRequest(Event::consume);
        cardButton.setOnAction(e -> {
            cardStage.hide();

            chooseCardsHandler.onChooseCards(SortedBag.of(listView.getSelectionModel().getSelectedItem()));
        });


        vBox.getChildren().addAll(textFlow, listView, cardButton);
        createScene(cardStage, vBox);
        cardStage.show();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * opens a window allowing the player to make his choice;
     * once this has been done and confirmed, the choice handler is called with the player's choice as an argument
     *
     * @param possibleAdditionalCards alla the possible additional cards to take a tunnel
     * @param chooseCardsHandler      chooses the additional cards (can be null)
     */

    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditionalCards,
                                      ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();

        Stage additionalStage = createStage(StringsFr.CARDS_CHOICE);
        VBox vBox = new VBox();
        TextFlow textFlow = createTextFlow(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        Button cardButton = new Button(StringsFr.CHOOSE);
        ListView<SortedBag<Card>> listView = createListView(possibleAdditionalCards);

        additionalStage.setOnCloseRequest(Event::consume);

        cardButton.setOnAction(e -> {

            additionalStage.hide();

            SortedBag<Card> chosenCards = (listView.getSelectionModel().getSelectedItem() != null)
                    ? SortedBag.of(listView.getSelectionModel().getSelectedItem())
                    : SortedBag.of();

            chooseCardsHandler.onChooseCards(chosenCards);

        });

        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));


        vBox.getChildren().addAll(textFlow, listView, cardButton);
        createScene(additionalStage, vBox);
        additionalStage.show();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Reset those Handlers :
     * - drawTicketsHandler
     * - drawCardHandler
     * - claimRouteHandle
     */

    private void resetHandlers() {
        drawTicketsHandlerOP.set(null);
        drawCardHandlerOP.set(null);
        claimRouteHandlerOP.set(null);
    }


    //----------------------------------------------------------------------------------------------------

    /**
     * Create a list view with given List<SortedBag<Card>>
     *
     * @param sortedBags the sortedBags we want to see
     * @return their ListView
     */

    private ListView<SortedBag<Card>> createListView(List<SortedBag<Card>> sortedBags) {

        ObservableList<SortedBag<Card>> List = FXCollections.observableList(sortedBags);

        return new ListView<>(List);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create a text flow with a given string
     *
     * @param s the text corresponding
     * @return a new TextFlow
     */

    private TextFlow createTextFlow(String s) {
        TextFlow textFlow = new TextFlow();

        Text text = new Text();
        text.setText(s);
        textFlow.getChildren().add(text);

        return textFlow;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create a stage with a given string
     *
     * @param s the text corresponding
     * @return a new Stage
     */

    private Stage createStage(String s) {
        Stage additionalStage = new Stage(StageStyle.UTILITY);
        additionalStage.setTitle(s);
        additionalStage.initOwner(primaryStage);
        additionalStage.initModality(Modality.WINDOW_MODAL);

        return additionalStage;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create a scene with the given vbox and set on close request the given stage
     *
     * @param s    stage
     * @param vBox vbox
     */

    private void createScene(Stage s, VBox vBox) {
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add(CHOOSER_CSS);
        s.setScene(scene);

        s.setOnCloseRequest(Event::consume);
    }

    //----------------------------------------------------------------------------------------------------

}
