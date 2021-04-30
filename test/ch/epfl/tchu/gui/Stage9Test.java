package ch.epfl.tchu.gui;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.DecksViewCreator;
import ch.epfl.tchu.gui.MapViewCreator;
import ch.epfl.tchu.gui.ObservableGameState;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class Stage9Test extends Application {

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        ObservableGameState gameState = new ObservableGameState(PLAYER_1);

        Node mapView = MapViewCreator
                .createMapView();
        Node cardsView = DecksViewCreator
                .createCardsView(gameState);
        Node handView = DecksViewCreator
                .createHandView(gameState);

        BorderPane mainPane =
                new BorderPane(mapView, null, cardsView, handView, null);
        primaryStage.setScene(new Scene(mainPane));
        primaryStage.show();
        //setState(gameState);

        /** ObjectProperty<ClaimRouteHandler> claimRoute =
         new SimpleObjectProperty<>(Stage9Test::claimRoute);
         ObjectProperty<DrawTicketsHandler> drawTickets =
         new SimpleObjectProperty<>(Stage9Test::drawTickets);
         ObjectProperty<DrawCardHandler> drawCard =
         new SimpleObjectProperty<>(Stage9Test::drawCard);

         Node mapView = MapViewCreator
         .createMapView(gameState, claimRoute, Stage9Test::chooseCards);
         Node cardsView = DecksViewCreator
         .createCardsView(gameState, drawTickets, drawCard);
         Node handView = DecksViewCreator
         .createHandView(gameState);

         BorderPane mainPane =
         new BorderPane(mapView, null, cardsView, handView, null);
         primaryStage.setScene(new Scene(mainPane));
         primaryStage.show();**/

         setState(gameState);
    }

    private void setState(ObservableGameState gameState) {
     PlayerState p1State =
     new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 3)),
     SortedBag.of(1, Card.WHITE, 3, Card.RED),
     ChMap.routes().subList(0, 3));

     PublicPlayerState p2State =
     new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

     Map<PlayerId, PublicPlayerState> pubPlayerStates =
     Map.of(PLAYER_1, p1State, PLAYER_2, p2State);
     PublicCardState cardState =
     new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
     PublicGameState publicGameState =
     new PublicGameState(36, cardState, PLAYER_1, pubPlayerStates, null);
     gameState.setState(publicGameState, p1State);
     }

    private static void claimRoute(Route route, SortedBag<Card> cards) {
        System.out.printf("Prise de possession d'une route : %s - %s %s%n",
                route.station1(), route.station2(), cards);
    }

    /**private static void chooseCards(List<SortedBag<Card>> options,
     ChooseCardsHandler chooser) {
     chooser.onChooseCards(options.get(0));
     }**/

    private static void drawTickets() {
        System.out.println("Tirage de billets !");
    }

    private static void drawCard(int slot) {
        System.out.printf("Tirage de cartes (emplacement %s)!\n", slot);
    }


    public void start2(Stage primaryStage) {

        final Image image = new Image("map.png");
        final ImageView imageView = new ImageView(image);
        final Pane root = new Pane();
        root.getChildren().setAll(imageView);
        final Scene scene = new Scene(root, 1000, 1000);
        primaryStage.setTitle("Test d'affichage d'image");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*private void setState(ObservableGameState gameState) {
        PlayerState p1State =
                new PlayerState(SortedBag.of(ChMap.tickets().subList(0, 3)),
                        SortedBag.of(1, Card.WHITE, 3, Card.RED),
                        ChMap.routes().subList(0, 3));

        PublicPlayerState p2State =
                new PublicPlayerState(0, 0, ChMap.routes().subList(3, 6));

        Map<PlayerId, PublicPlayerState> pubPlayerStates =
                Map.of(PLAYER_1, p1State, PLAYER_2, p2State);
        PublicCardState cardState =
                new PublicCardState(Card.ALL.subList(0, 5), 110 - 2 * 4 - 5, 0);
        PublicGameState publicGameState =
                new PublicGameState(36, cardState, PLAYER_1, pubPlayerStates, null);
        gameState.setState(publicGameState, p1State);
    }*/

}