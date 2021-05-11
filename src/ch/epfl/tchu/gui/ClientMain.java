package ch.epfl.tchu.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class ClientMain extends Application {


    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        getParameters().getRaw().get(0);

    }
}
