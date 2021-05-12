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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
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

    private final static int BUTTON_WIDTH = 50;
    private final static int BUTTON_HEIGHT = 5;
    private final static String PREFIX_TITLE = "tCHu - ";
    private final static String BACKGROUND = "background";
    private final static String FOREGROUND = "foreground";



    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;
    private final ObservableList<Text> textList;
    private final ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerOP;
    private final ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerOP;
    private final ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerOP;

    private final Stage primaryStage;

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerId
     * @param playerNames
     */

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames) {

        observableGameState = new ObservableGameState(playerId);
        this.playerId = playerId;
        this.playerNames = playerNames;

        textList = FXCollections.observableArrayList();

        drawTicketsHandlerOP = new SimpleObjectProperty<>();
        drawCardHandlerOP = new SimpleObjectProperty<>();
        claimRouteHandlerOP = new SimpleObjectProperty<>();


        primaryStage = new Stage();
        primaryStage.setTitle(PREFIX_TITLE + playerNames.get(playerId));

        Node mapView = MapViewCreator.createMapView(observableGameState, claimRouteHandlerOP, this::chooseClaimCards);
        Node cardsView = DecksViewCreator.createCardsView(observableGameState, drawTicketsHandlerOP, drawCardHandlerOP);
        Node handView = DecksViewCreator.createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, textList);

        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        primaryStage.setScene(new Scene(borderPane));

        primaryStage.show();

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param newGameState
     * @param newPlayerState
     */

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert isFxApplicationThread();
        observableGameState.setState(newGameState, newPlayerState);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param message
     */

    public void receiveInfo(String message) {
        assert isFxApplicationThread();

        if(textList.size() == 5) {
            textList.remove(0);
        }
        textList.add(new Text(message));

        InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, textList);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param drawTicketsHandler
     * @param drawCardHandler
     * @param claimRouteHandler
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
     *
     * @param tickets
     * @param chooseTicketsHandler
     */

    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler) {
        assert isFxApplicationThread();
        Preconditions.checkArgument(tickets.size() == IN_GAME_TICKETS_COUNT ||
                                                tickets.size() == INITIAL_TICKETS_COUNT);

        Stage ticketStage = createStage(StringsFr.TICKETS_CHOICE);
        VBox vBox = new VBox();
        TextFlow textFlow = createTextFlow(String.format(StringsFr.CHOOSE_TICKETS, tickets.size(), StringsFr.plural(tickets.size())));
        Button ticketButton = createButton();
        ListView<Ticket> listView = new ListView<>(FXCollections.observableList(tickets.toList()));


        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ticketButton.disableProperty()
                .bind(Bindings.size(listView.getSelectionModel().getSelectedItems())
                        .lessThan(tickets.size() - 2));

        ticketButton.setOnAction(e -> {
            ticketStage.hide();

                chooseTicketsHandler.onChooseTickets(SortedBag.of(listView.getSelectionModel().getSelectedItems()));

        });


        vBox.getChildren().addAll(textFlow, listView, ticketButton);
        createScene(ticketStage,vBox);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param drawCardHandler
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
     *
     * @param initialCards
     * @param chooseCardsHandler
     */

    public void chooseClaimCards(List<SortedBag<Card>> initialCards,
                                 ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();

        Stage cardStage = createStage(StringsFr.CARDS_CHOICE);
        VBox vBox = new VBox();
        TextFlow textFlow = createTextFlow(StringsFr.CHOOSE_CARDS);
        Button cardButton = createButton();
        ListView<SortedBag<Card>> listView = createListView(initialCards);


        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        cardButton.disableProperty()
                .bind(Bindings.size(listView.getSelectionModel().getSelectedItems()).lessThan(1));

        cardButton.setOnAction(e -> {
            cardStage.hide();

                chooseCardsHandler.onChooseCards(SortedBag.of((List) listView.getSelectionModel().getSelectedItems()));
        });


        vBox.getChildren().addAll(textFlow, listView, cardButton);
        createScene(cardStage,vBox);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param possibleAdditionalCards
     * @param chooseCardsHandler
     */

    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditionalCards,
                                      ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();

        Stage additionalStage = createStage(StringsFr.CARDS_CHOICE);
        VBox vBox = new VBox();
        TextFlow textFlow = createTextFlow(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        Button cardButton = createButton();
        ListView<SortedBag<Card>> listView = createListView(possibleAdditionalCards);


        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));


        vBox.getChildren().addAll(textFlow, listView, cardButton);
        createScene(additionalStage,vBox);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     */

    private void resetHandlers() {
        drawTicketsHandlerOP.set(null);
        drawCardHandlerOP.set(null);
        claimRouteHandlerOP.set(null);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    private Button createButton(){
        Button button = new Button(StringsFr.CHOOSE);

        Rectangle back = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        back.getStyleClass().add(BACKGROUND);
        Rectangle fore = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        fore.getStyleClass().add(FOREGROUND);
        button.setGraphic(new Group(back, fore));

        return button;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param o
     * @return
     */

    private ListView<SortedBag<Card>> createListView(List<SortedBag<Card>> o){

        ObservableList<SortedBag<Card>> List = FXCollections.observableList(o);

        return new ListView<>(List);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param s
     * @return
     */

    private TextFlow createTextFlow(String s){
        TextFlow textFlow = new TextFlow();

        Text text = new Text();
        text.setText(s);
        textFlow.getChildren().add(text);

        return textFlow;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param s
     * @return
     */

    private Stage createStage(String s){
        Stage additionalStage = new Stage(StageStyle.UTILITY);
        additionalStage.setTitle(s);
        additionalStage.initOwner(primaryStage);
        additionalStage.initModality(Modality.WINDOW_MODAL);

        return additionalStage;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create a scene with the given vbox and set on close request the given stage
     * @param s stage
     * @param vBox vbox
     */

    private void createScene(Stage s,VBox vBox){
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        s.setScene(scene);

        s.setOnCloseRequest(Event::consume);
    }

    //----------------------------------------------------------------------------------------------------

}
