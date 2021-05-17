package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class ClientMain extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primarystage) {

        List<String> arguments = getParameters().getRaw();
        String name;
        int port;
        if(arguments.isEmpty() || arguments.get(0).isEmpty())
            name = "localhost";
        else
            name = arguments.get(0);

        if(arguments.isEmpty() || arguments.get(1).isEmpty())
            port = 5108;
        else

            port = Integer.parseInt(arguments.get(1));

        GraphicalPlayerAdapter player = new GraphicalPlayerAdapter();
        RemotePlayerClient client = new RemotePlayerClient(player, name, port);
        new Thread(client::run).start();

    }

}
