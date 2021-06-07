package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
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

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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


    private static int language = 0;
    private final BlockingQueue<Integer> isPlayable = new ArrayBlockingQueue<>(1);

    private boolean isChinese = false;
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

        Platform.setImplicitExit(false);


        Pane root = new Pane();
        Stage menuStage = new Stage();
        menuStage.setScene(new Scene(root));

        ImageView iv = new ImageView();
        root.getChildren().add(iv);
        root.getStylesheets().addAll("newmap.css", "chooser.css");

        // Game menu
        VBox mainMenu = new VBox(20);
        VBox languageMenu = new VBox(20);
        VBox rulesMenu = new VBox(60);

        int offset = 2000;
        int normalX = 100;
        int normalY = 200;

        mainMenu.setTranslateX(normalX);
        mainMenu.setTranslateY(normalY);
        languageMenu.setTranslateX(offset);
        languageMenu.setTranslateY(normalY);
        rulesMenu.setTranslateX(offset);
        rulesMenu.setTranslateY(normalY);

        //MenuButton
        StackPane btnPlay = createSP("  -  JOUER");
        //StackPane btnName = createSP("  -  NOM");
        StackPane btnLanguage = createSP("  -  LANGUES");
        StackPane btnRules = createSP("  -  REGLES");

        StackPane btnFrench = createSP("  -  FRANCAIS");
        StackPane btnChinese = createSP("  -  CHINOIS");
        StackPane btnExit = createSP("  -  RETOUR");

        StackPane btnExit2 = createSP("  -  RETOUR");

        Text title = new Text("REGLES DU JEU");
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 24));
        Text rules = new Text("Piochez des billets et emparez vous des routes nécessaires pour marquer \n" +
                "le plus de points possibles. \n \n" +
                "Mais attention, si vous ne finissez pas tous vos billets avant la fin de la partie, \n" +
                "vous perdrez des points. \n \n" +
                "(Veillez à choisir le même language que votre adversaire)");
        rules.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));
        rulesMenu.getChildren().addAll(title, rules, btnExit2);
        languageMenu.getChildren().addAll(btnFrench, btnChinese, btnExit);
        mainMenu.getChildren().addAll(btnPlay, btnLanguage, btnRules);

        rulesMenu.setAlignment(Pos.CENTER_LEFT);



        Rectangle back = new Rectangle(843, 663);
        back.setFill(javafx.scene.paint.Color.GRAY);
        back.setOpacity(0.4);

        root.getChildren().addAll(back, mainMenu, languageMenu, rulesMenu);


        btnPlay.setOnMouseClicked(e -> {

            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), root);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(evt -> {
                //root.setVisible(false);
                isPlayable.add(1);
                menuStage.hide();

            });
            ft.play();
        });

        btnLanguage.setOnMouseClicked(e -> {
            mainMenu.setTranslateX(offset);
            languageMenu.setTranslateX(normalX);

        });

        btnExit.setOnMouseClicked(e -> {
            languageMenu.setTranslateX(offset);
            mainMenu.setTranslateX(normalX);

        });



        btnFrench.setOnMouseClicked(e -> {
            isChinese = false;
        });

        btnChinese.setOnMouseClicked(e -> {
            isChinese = true;
        });

        btnRules.setOnMouseClicked(e -> {
            mainMenu.setTranslateX(offset);
            rulesMenu.setTranslateX(normalX);

        });

        btnExit2.setOnMouseClicked(e -> {
            rulesMenu.setTranslateX(offset);
            mainMenu.setTranslateX(normalX);

        });

        menuStage.show();

        //----------------------------------------------------------------------------------------------------

        List<String> arg = this.getParameters().getRaw();

        String host = arg.size() >= 1 ? arg.get(0) : HOST;
        int port = arg.size() == 2 ? Integer.parseInt(arg.get(1)) : PORT;


        new Thread(() -> {
            try {
                int n = isPlayable.take();
                RemotePlayerClient client = new RemotePlayerClient(new GraphicalPlayerAdapter(isChinese), host, port);
                client.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }).start();
    }


    private static StackPane createSP(String name) {
        StackPane sp = new StackPane();
        sp.setAlignment(Pos.CENTER_LEFT);

        Text play = new Text(name);
        play.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.ITALIC, 16));
        play.setFill(javafx.scene.paint.Color.BLACK);
        Rectangle back = new Rectangle(180, 30);
        back.setFill(javafx.scene.paint.Color.BLANCHEDALMOND);
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


    //----------------------------------------------------------------------------------------------------

}
