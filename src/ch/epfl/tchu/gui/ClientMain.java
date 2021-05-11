package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {


    public static void main(String[] args) {
        launch();
        // args optionnels?
    }

    @Override
    public void start(Stage stage) throws Exception {

        RemotePlayerClient client = new RemotePlayerClient(new GraphicalPlayerAdapter(),
                "localhost",//getParameters().getRaw().get(0),
                5108);//Integer.parseInt(getParameters().getRaw().get(1)));

        new Thread(() -> client.run()); // juste run()?
    }
}
