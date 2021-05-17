package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        GraphicalPlayerAdapter player = new GraphicalPlayerAdapter();

        String name = "localhost";
        int port = 5108;

        if (getParameters().getRaw().size() == 1) {

            name = getParameters().getRaw().get(0);

        } else if (getParameters().getRaw().size() > 1) {
            name = getParameters().getRaw().get(0);
            port = Integer.parseInt(getParameters().getRaw().get(1));
        }

        RemotePlayerClient client = new RemotePlayerClient(player, name, port);
        new Thread(client::run).start();

    }
}
