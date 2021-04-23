package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.Ticket;
import ch.epfl.tchu.gui.Info;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.*;

public final class TestServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Starting server!");
        try (ServerSocket serverSocket = new ServerSocket(5100);
             Socket socket = serverSocket.accept()) {
            Player playerProxy = new RemotePlayerProxy(socket);

            var playerNames = Map.of(PLAYER_1, "Ada",
                    PLAYER_2, "Charles");

            Info i1 = new Info(playerNames.get(PlayerId.PLAYER_1));

            SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());




            playerProxy.initPlayers(PLAYER_1, playerNames);

            playerProxy.receiveInfo(i1.drewBlindCard());

           //playerProxy.updateState();

            playerProxy.setInitialTicketChoice(tickets);
        }
        System.out.println("Server done!");
    }
}
