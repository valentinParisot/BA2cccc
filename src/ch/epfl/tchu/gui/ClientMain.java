package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import static javafx.application.Platform.runLater;

public class ClientMain extends Application {

    private final Player player = new GraphicalPlayerAdapter();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        if (getParameters().getRaw().size() == 0) {
            RemotePlayerClient client = new RemotePlayerClient(player,
                    "localhost",
                    5108);

            new Thread(() -> client.run()).start();

        } else if (getParameters().getRaw().size() == 1) {
            RemotePlayerClient client = new RemotePlayerClient(player,
                    getParameters().getRaw().get(0),
                    5108);

            new Thread(() -> client.run()).start();

        } else {
            RemotePlayerClient client = new RemotePlayerClient(player,
                    getParameters().getRaw().get(0),
                    Integer.parseInt(getParameters().getRaw().get(1)));

            new Thread(() -> client.run()).start();
        }
        // juste run()?
    }

}
