package ch.epfl.tchu.gui;

import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

class MenuViewCreator {


    public static Pane createMenuView() {

        Pane root = new Pane();


        ImageView iv = new ImageView();
        root.getChildren().add(iv);
        root.getStylesheets().addAll("newmap.css");


        //----------------------------------------------------------------------------------------------

        // Game menu
        VBox menu0 = new VBox(10);
        //VBox menu1 = new VBox(10);

        menu0.setTranslateX(100);
        menu0.setTranslateY(200);
        //menu1.setTranslateX(100);
        //menu1.setTranslateY(200);

        //MenuButton
        StackPane btnPlay = createSP("  -  JOUER");

        btnPlay.setOnMouseClicked(e -> {
            FadeTransition ft = new FadeTransition(Duration.seconds(1.2), root);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(evt -> {
                root.setVisible(false);
                //primaryStage.setScene(new Scene(gamePane));
            });
            ft.play();
        });

        menu0.getChildren().addAll(btnPlay);


        Rectangle back = new Rectangle(843, 663);
        back.setFill(Color.GRAY);
        back.setOpacity(0.4);

        root.getChildren().addAll(back, menu0);

        return root;
    }


    private static StackPane createSP(String name) {
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.CENTER_LEFT);

        Text play = new Text(name);
        play.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
        play.setFill(Color.BLACK);
        Rectangle back = new Rectangle(180, 30);
        back.setFill(Color.BLANCHEDALMOND);
        back.setOpacity(0.7);
        sp.getChildren().addAll(back, play);

        sp.setOnMouseEntered(e -> {
            play.setTranslateX(10);
            play.setTranslateY(-3);
            back.setTranslateX(10);
            back.setTranslateY(-3);
        });

        sp.setOnMouseExited(e -> {
            play.setTranslateX(0);
            play.setTranslateY(0);
            back.setTranslateX(0);
            back.setTranslateY(0);
        });

        DropShadow drop = new DropShadow(50, Color.BLACK);
        drop.setInput(new Glow());

        sp.setOnMousePressed(e -> sp.setEffect(drop));

        sp.setOnMouseReleased(e -> sp.setEffect(null));

        return sp;

    }
}
