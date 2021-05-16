package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class ServerMain extends Application {

    private String p1 = "Ada";
    private String p2 = "Charles";

    public static void main(String[] args) {
        launch();
    }

    /**
     * starts the server an calls Game.play()
     *
     * @param stage never used
     */

    @Override
    public void start(Stage stage) { // comment utiliser le try?



        try (ServerSocket serverSocket = new ServerSocket(5108);
             Socket socket = serverSocket.accept()) {
            RemotePlayerProxy playerProxy = new RemotePlayerProxy(socket);


            if (getParameters().getRaw().size() == 1) {

                p1 = getParameters().getRaw().get(0);

            } else if (getParameters().getRaw().size() == 2) {
                p1 = getParameters().getRaw().get(0);
                p2 = getParameters().getRaw().get(1);
            }

            new Thread(() -> Game.play(Map.of(PLAYER_1, new GraphicalPlayerAdapter(), PLAYER_2, playerProxy),
                    Map.of(PLAYER_1, p1, PLAYER_2, p2),
                    SortedBag.of(ChMap.tickets()),
                    new Random())
            );


        } catch (IOException e) {
            throw (new UncheckedIOException(e));
        }
    }
}
