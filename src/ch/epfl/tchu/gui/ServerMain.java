package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * ServerMain
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class ServerMain extends Application {

    //----------------------------------------------------------------------------------------------------

    private static final String FIRST = "Ada";
    private static final String SECOND = "Charles";

    //----------------------------------------------------------------------------------------------------

    /**
     * Launch the game
     *
     * @param args argument
     */

    public static void main(String[] args) {
        launch(args);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * starts the server and initiate players
     *
     * @param stage never used
     */

    @Override
    public void start(Stage stage) {

        try (ServerSocket serverSocket = new ServerSocket(5108)) {
            Socket socket = serverSocket.accept();

            List<String> arg = getParameters().getRaw();

            String first = arg.size() >= 1 ? arg.get(0) : FIRST;
            String second = arg.size() == 2 ? arg.get(1) : SECOND;

            RemotePlayerProxy playerProxy = new RemotePlayerProxy(socket);

            Map<PlayerId, Player> players = Map.of(PLAYER_1, new GraphicalPlayerAdapter(), PLAYER_2, playerProxy);
            Map<PlayerId, String> names = Map.of(PLAYER_1, first, PLAYER_2, second);


            new Thread(() -> Game.play(players, names, SortedBag.of(ChMap.tickets()), new Random())).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //----------------------------------------------------------------------------------------------------
}
