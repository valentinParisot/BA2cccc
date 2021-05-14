package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    private final GraphicalPlayerAdapter player = new GraphicalPlayerAdapter();

    public static void main(String[] args) {
        launch();

    }

    @Override
    public void start(Stage stage) {

        RemotePlayerClient client;

        if (getParameters().getRaw().size() == 0) {
            client = new RemotePlayerClient(player,
                    "localhost",
                    5108);

        } else if (getParameters().getRaw().size() == 1) {
             client = new RemotePlayerClient(player,
                    getParameters().getRaw().get(0),
                    5108);

        } else {
            client = new RemotePlayerClient(player,
                    getParameters().getRaw().get(0),
                    Integer.parseInt(getParameters().getRaw().get(1)));
        }
        new Thread(() -> client.run()).start();

    }
}
