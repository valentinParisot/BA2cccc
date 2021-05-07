package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.binding.Binding;
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

import static javafx.application.Platform.isFxApplicationThread;

public class GraphicalPlayer {

    private final static int BUTTON_WIDTH = 50;
    private final static int BUTTON_HEIGHT = 5;

    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;
    private ObservableList<Text> textList;
    private ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketsHandlerOP;
    private ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandlerOP;
    private ObjectProperty<ActionHandlers.ClaimRouteHandler> claimRouteHandlerOP;

    private final Stage primaryStage;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames) {

        observableGameState = new ObservableGameState(playerId);
        this.playerId = playerId;
        this.playerNames = playerNames;

        textList = FXCollections.observableArrayList();

        drawTicketsHandlerOP = new SimpleObjectProperty<ActionHandlers.DrawTicketsHandler>();
        drawCardHandlerOP = new SimpleObjectProperty<ActionHandlers.DrawCardHandler>();
        claimRouteHandlerOP = new SimpleObjectProperty<ActionHandlers.ClaimRouteHandler>();


        primaryStage = new Stage();
        primaryStage.setTitle("tCHu - " + playerNames.get(playerId));

        Node mapView = MapViewCreator.createMapView(observableGameState, claimRouteHandlerOP, this::chooseClaimCards);
        Node cardsView = DecksViewCreator.createCardsView(observableGameState, drawTicketsHandlerOP, drawCardHandlerOP);
        Node handView = DecksViewCreator.createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, textList);

        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        primaryStage.setScene(new Scene(borderPane));

    }


    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        assert isFxApplicationThread();
        observableGameState.setState(newGameState, newPlayerState);
    }

    public void receiveInfo(String message) {
        assert isFxApplicationThread();

        if (textList.size() == 5) {
            textList.remove(0);
            textList.add(new Text(message));
        } else {
            textList.add(new Text(message));
        }

        InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, textList);
    }

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
            resetHandlers();
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

    public void chooseTicket(SortedBag<Ticket> tickets, ActionHandlers.ChooseTicketsHandler chooseTicketsHandler) {
        assert isFxApplicationThread();
        Preconditions.checkArgument(tickets.size() == 3 || tickets.size() == 5);

        Stage ticketStage = new Stage(StageStyle.UTILITY);
        ticketStage.setTitle(StringsFr.TICKETS_CHOICE);
        ticketStage.initOwner(primaryStage);
        ticketStage.initModality(Modality.WINDOW_MODAL);

        VBox vBox = new VBox();

        TextFlow textFlow = new TextFlow();
        Text text = new Text();
        text.setText(String.format(StringsFr.CHOOSE_TICKETS, tickets.size(), StringsFr.plural(tickets.size())));
        textFlow.getChildren().add(text);

        ObservableList<Ticket> ticketsList = FXCollections.observableList(tickets.toList());

        ListView<ObservableList<Ticket>> listView = new ListView(ticketsList);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button ticketButton = new Button(StringsFr.CHOOSE);

        Rectangle backTicket = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backTicket.getStyleClass().add("background");
        Rectangle foreTicket = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        foreTicket.getStyleClass().add("foreground");

        ticketButton.setGraphic(new Group(backTicket, foreTicket));
        ticketButton.disableProperty()
                .bind(Bindings.size(listView.getSelectionModel().getSelectedItems())
                        .lessThan(tickets.size() - 2));

        ticketButton.setOnAction(e -> {
            ticketStage.hide();
            chooseTicketsHandler.onChooseTickets(SortedBag.of((List) listView.getSelectionModel().getSelectedItems()));
        });

        vBox.getChildren().addAll(textFlow, listView, ticketButton);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        ticketStage.setScene(scene);

        ticketStage.setOnCloseRequest(Event::consume);

    }

    public void drawCard(ActionHandlers.DrawCardHandler drawCardHandler) {
        assert isFxApplicationThread();
        resetHandlers();

    }

    public void chooseClaimCards(List<SortedBag<Card>> initialCards,
                                 ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();

        Stage cardStage = new Stage(StageStyle.UTILITY);
        cardStage.setTitle(StringsFr.CARDS_CHOICE);
        cardStage.initOwner(primaryStage);
        cardStage.initModality(Modality.WINDOW_MODAL);

        VBox vBox = new VBox();

        TextFlow textFlow = new TextFlow();
        Text text = new Text();
        text.setText(StringsFr.CHOOSE_CARDS);
        textFlow.getChildren().add(text);

        ListView<SortedBag<Card>> listView = new ListView<SortedBag<Card>>();
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        Button cardButton = new Button(StringsFr.CHOOSE);

        Rectangle backCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backCard.getStyleClass().add("background");
        Rectangle foreCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        foreCard.getStyleClass().add("foreground");

        cardButton.setGraphic(new Group(backCard, foreCard));
        cardButton.disableProperty()
                .bind(Bindings.size(listView.getSelectionModel().getSelectedItems()).lessThan(1));

        vBox.getChildren().addAll(textFlow, listView, cardButton);
        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        cardStage.setScene(scene);

        cardStage.setOnCloseRequest(Event::consume);
    }

    public void chooseAdditionalCards(SortedBag<Card> possibleAdditionalCards,
                                      ActionHandlers.ChooseCardsHandler chooseCardsHandler) {
        assert isFxApplicationThread();

        Stage additionalStage = new Stage(StageStyle.UTILITY);
        additionalStage.setTitle(StringsFr.CARDS_CHOICE);
        additionalStage.initOwner(primaryStage);
        additionalStage.initModality(Modality.WINDOW_MODAL);

        VBox vBox = new VBox();

        TextFlow textFlow = new TextFlow();
        Text text = new Text();
        text.setText(StringsFr.CHOOSE_ADDITIONAL_CARDS);
        textFlow.getChildren().add(text);

        ListView<SortedBag<Card>> listView = new ListView<SortedBag<Card>>();
        listView.setCellFactory(v ->
                new TextFieldListCell<>(new CardBagStringConverter()));

        Button cardButton = new Button(StringsFr.CHOOSE);

        Rectangle backCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        backCard.getStyleClass().add("background");
        Rectangle foreCard = new Rectangle(BUTTON_WIDTH, BUTTON_HEIGHT);
        foreCard.getStyleClass().add("foreground");

        cardButton.setGraphic(new Group(backCard, foreCard));

        vBox.getChildren().addAll(textFlow, listView, cardButton);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");
        additionalStage.setScene(scene);

        additionalStage.setOnCloseRequest(Event::consume);
    }

    private void resetHandlers() {
        drawTicketsHandlerOP.set(null);
        drawCardHandlerOP.set(null);
        claimRouteHandlerOP.set(null);
    }

}
