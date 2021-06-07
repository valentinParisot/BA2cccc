package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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

    private final BlockingQueue<Integer> isPlayable = new ArrayBlockingQueue<>(1);

    //----------------------------------------------------------------------------------------------------

    /**
     * Launch the game
     *
     * @param args argument
     */

    public static void main(String[] args) {
        launch(args);
    }

    private boolean isChinese = false;


    //----------------------------------------------------------------------------------------------------

    /**
     * starts the server and initiate players
     *
     * @param stage never used
     */

    @Override
    public void start(Stage stage) {

        Platform.setImplicitExit(false);


        Pane root = new Pane();
        Stage menuStage = new Stage();
        menuStage.setScene(new Scene(root));

        ImageView iv = new ImageView();
        root.getChildren().add(iv);
        root.getStylesheets().add("newmap.css");

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

        Text title = new Text("                         REGLES DU JEU");
        title.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 24));
        Text rules = new Text("Piochez des billets et emparez vous des routes nécessaires pour marquer \n" +
                "le plus de points possibles. \n \n" +
                "Mais attention, si vous ne finissez pas tous vos billets avant la fin de la partie, \n" +
                "vous perdrez des points. \n \n" +
                "(Veillez à choisir le même language que votre adversaire)");
        rules.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14));

        mainMenu.getChildren().addAll(btnPlay, btnLanguage, btnRules);
        languageMenu.getChildren().addAll(btnFrench, btnChinese, btnExit);
        rulesMenu.getChildren().addAll(title, rules, btnExit2);

        rulesMenu.setAlignment(Pos.CENTER_LEFT);

        Rectangle back = new Rectangle(843, 663);
        back.setFill(javafx.scene.paint.Color.GRAY);
        back.setOpacity(0.4);

        root.getChildren().addAll(back, mainMenu, languageMenu, rulesMenu);


        Text name1 = new Text("serverPlayer1");
        Text name2 = new Text("serverPlayer2");
        VBox nameChoice = new VBox(20);
        nameChoice.setAlignment(Pos.CENTER);

        TextField textField1 = new TextField();
        textField1.setPromptText("Entrez le nom du joueur 1");
        textField1.setFocusTraversable(false);
        textField1.setFont(Font.font(16));

        TextField textField2 = new TextField();
        textField2.setPromptText("Entrez le nom du joueur 2");
        textField2.setFocusTraversable(false);
        textField2.setFont(Font.font(16));

        Button okButton = new Button("OK");

        okButton.disableProperty().bind(textField1.textProperty().isEqualTo("").or(textField2.textProperty().isEqualTo("")));
        okButton.setFont(Font.font(16));
        okButton.setOnAction(e -> {
            name1.setText(textField1.getText());
            name2.setText(textField2.getText());
            isPlayable.add(1);
            menuStage.hide();
        });
        //name.toString()

        nameChoice.getChildren().addAll(textField1, textField2, okButton);


        StackPane namePane = new StackPane();
        namePane.setPrefSize(843, 663);
        namePane.getChildren().addAll(new ImageView(), nameChoice);
        namePane.getStylesheets().add("newmap.css");


        btnPlay.setOnMouseClicked(e -> {

            FadeTransition ft = new FadeTransition(Duration.seconds(0.5), root);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(evt -> {

                menuStage.setScene(new Scene(namePane));

            });
            ft.play();
        });

        btnLanguage.setOnMouseClicked(e -> {
            mainMenu.setTranslateX(offset);
            languageMenu.setTranslateX(normalX);

        });

        btnRules.setOnMouseClicked(e -> {
            mainMenu.setTranslateX(offset);
            rulesMenu.setTranslateX(normalX);

        });

        btnExit.setOnMouseClicked(e -> {
            languageMenu.setTranslateX(offset);
            mainMenu.setTranslateX(normalX);

        });

        btnExit2.setOnMouseClicked(e -> {
            rulesMenu.setTranslateX(offset);
            mainMenu.setTranslateX(normalX);

        });


        btnFrench.setOnMouseClicked(e -> {
            isChinese = false;
        });

        btnChinese.setOnMouseClicked(e -> {
            isChinese = true;
        });


        menuStage.show();


        //----------------------------------------------------------------------------------------------------

        List<String> arg = getParameters().getRaw();

        String first = arg.size() >= 1 ? arg.get(0) : FIRST;
        String second = arg.size() == 2 ? arg.get(1) : SECOND;


        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(5108)) {
                try {
                    int n = isPlayable.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Socket socket = serverSocket.accept();
                RemotePlayerProxy playerProxy = new RemotePlayerProxy(socket);


                Map<PlayerId, Player> players = Map.of(PLAYER_1, new GraphicalPlayerAdapter(isChinese), PLAYER_2, playerProxy);
                Map<PlayerId, String> names = Map.of(PLAYER_1, name1.getText(), PLAYER_2, name2.getText());
                Game.play(players, names, SortedBag.of(ChMap.tickets()), new Random());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();


    }

    //----------------------------------------------------------------------------------------------------

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
}
