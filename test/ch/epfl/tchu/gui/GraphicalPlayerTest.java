package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.SortedBag.Builder;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class GraphicalPlayerTest extends Application {
    public static void main(String[] args) {
        launch(args); }

    @Override
    public void start(Stage primaryStage) {
        Map<PlayerId, String> playerNames =
                Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");
        GraphicalPlayer p = new GraphicalPlayer(PLAYER_1, playerNames);
        setState(p);

        Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(ChMap.tickets().get(0));
        builder.add(ChMap.tickets().get(1));
        builder.add(ChMap.tickets().get(2));

        DrawTicketsHandler drawTicketsH =
                () -> {
                    p.chooseTickets(builder.build(), (c) -> {System.out.print("Je tire des billets!");});
                    p.receiveInfo("ok");
                    p.receiveInfo("ok");
                    p.receiveInfo("ok");
                    p.receiveInfo("ok");
                    p.receiveInfo("ok");

                };

        p.chooseAdditionalCards(List.of(SortedBag.of(Card.BLACK), SortedBag.of(Card.LOCOMOTIVE), SortedBag.of(Card.BLUE)), (c) -> {System.out.print("Additional cards");});
        DrawCardHandler drawCardH =
                s -> p.drawCard((c) -> {System.out.print("Je tire des cartes!");});
        ClaimRouteHandler claimRouteH =
                (c,r) -> {
                    p.chooseClaimCards(List.of(SortedBag.of(Card.BLACK), SortedBag.of(Card.LOCOMOTIVE), SortedBag.of(Card.BLUE)), (s) -> {System.out.print("Additional cards");});
                    p.receiveInfo("route claimed");
                };

        p.startTurn(drawTicketsH, drawCardH, claimRouteH);
    }

    private void setState(GraphicalPlayer player) {
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
        new ObservableGameState(PLAYER_1).setState(publicGameState, p1State);
        player.setState(publicGameState, p1State);
    }

}