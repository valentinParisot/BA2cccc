package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

/**
 * ClientMain
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class ClientMain extends Application {

    //----------------------------------------------------------------------------------------------------

    private static final String HOST = "localhost";
    private static final int PORT = 5108;

    //----------------------------------------------------------------------------------------------------

    public static void main(String[] args) {
        launch(args);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * start the server to play local or online (run after the server)
     *
     * @param stage not used
     */
    @Override
    public void start(Stage stage) {
        List<String> arg = this.getParameters().getRaw();

        String host = arg.size() >= 1 ? arg.get(0) : HOST;
        int port = arg.size() == 2 ? Integer.parseInt(arg.get(1)) : PORT;

        RemotePlayerClient client = new RemotePlayerClient(new GraphicalPlayerAdapter(), host, port);
        new Thread(client::run).start();
    }

    //----------------------------------------------------------------------------------------------------

}
