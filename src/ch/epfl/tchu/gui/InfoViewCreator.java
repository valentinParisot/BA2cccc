package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

import static ch.epfl.tchu.gui.StringsFr.PLAYER_STATS;
import static javafx.beans.binding.Bindings.bindContent;

/**
 * InfoViewCreator
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

class InfoViewCreator {

    //----------------------------------------------------------------------------------------------------

    private final static String PLAYER_1 = "PLAYER_1";
    private final static String FILLED = "filled";
    private final static String SET_ID_PLAYER_STATE = "player-stats";
    private final static String SET_ID_GAME_INFO = "game-info";
    private final static int RADIUS_CIRCLE = 5;
    private final static int MAX_INFO = 5;

    //----------------------------------------------------------------------------------------------------

    /**
     * used to create the information view
     *
     * @param id                  id of the player
     * @param name                the associative table of player names
     * @param observableGameState observable state of play
     * @param observableList      an (observable) list containing information on the progress of the game, in the form of instances of Text
     * @return the info view of the game
     */

    public static VBox createInfoView(PlayerId id, Map<PlayerId, String> name,
                                      ObservableGameState observableGameState,
                                      ObservableList<Text> observableList) {
        VBox playerStats = new VBox();
        playerStats.setId(SET_ID_PLAYER_STATE);

        //statistics(observableGameState, name, playerStats);





        for (PlayerId player: PlayerId.ALL) {

            TextFlow statistic = new TextFlow();
            statistic.getStyleClass().add(player.name());


            Circle circle = new Circle();
            circle.setRadius(RADIUS_CIRCLE);
            circle.getStyleClass().add(FILLED);

            Text monText = new Text();
            monText.textProperty().bind(expression(player,observableGameState,name));

            statistic.getChildren().addAll(circle,monText);
            playerStats.getChildren().add(statistic);

        }

        TextFlow message = new TextFlow();
        message.setId(SET_ID_GAME_INFO);

        if(observableList.size() <= 5 && observableList.size() > 0 ) {
            for (int i = 0; i < observableList.size(); i++) {

                Text t = new Text();
                t.setText(String.valueOf(observableList.get(i))); // j'ai rajouté ça sinon c'était vide non?
                message.getChildren().add(t);

            }
        }
        else if(observableList.size() != 0){
            throw new IllegalArgumentException("trop de messages c'est max 5");
        }

        bindContent(message.getChildren(),observableList);

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        VBox root = new VBox(playerStats,separator1,message);
        root.getStylesheets().addAll("info.css","colors.css");


        return root;
    }

    /** VBox playerStats = new VBox();
     playerStats.setId(SET_ID_PLAYER_STATE);

     statistics(observableGameState, name, playerStats);

     TextFlow message = new TextFlow();
     message.setId(SET_ID_GAME_INFO);

     if (observableList.size() <= MAX_INFO && observableList.size() > 0) {
         for (Text text : observableList) {

             Text t = new Text();
             t.setText(String.valueOf(text));
             message.getChildren().add(t);

         }
     }

     bindContent(message.getChildren(), observableList);

     return createVbox(playerStats, message);**/


    //----------------------------------------------------------------------------------------------------

    /**
     * Create a string expression for a given player about his statistics
     *
     * @param player              given player
     * @param observableGameState observable game state of the game
     * @param name                map of players and name
     * @return StringExpression about his statistics
     */

    private static StringExpression expression(PlayerId player,
                                               ObservableGameState observableGameState,
                                               Map<PlayerId, String> name) {

        StringExpression expression ;

        if (player.name().equals(PLAYER_1)) {

            expression = Bindings.format(PLAYER_STATS,
                    name.get(player),
                    observableGameState.ticketCount(),
                    observableGameState.cardCount(),
                    observableGameState.wagonCunt(),
                    observableGameState.playerPoints());

        } else  { // if + ondiotion

            expression = Bindings.format(PLAYER_STATS,
                    name.get(player),
                    observableGameState.ticketCount2(),
                    observableGameState.cardCount2(),
                    observableGameState.wagonCunt2(),
                    observableGameState.playerPoints2());
        }

        return expression;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * handle the design of statistics for all players
     *
     * @param observableGameState observable game state of the game
     * @param name                map of players and name
     * @param vbox                vbox who contains statistics
     */

    private static void statistics(ObservableGameState observableGameState, Map<PlayerId, String> name, VBox vbox) {
        for (PlayerId id : PlayerId.ALL) {
            TextFlow statistic = new TextFlow();
            statistic.getStyleClass().add(id.name());

            Circle circle = new Circle();
            circle.setRadius(RADIUS_CIRCLE);
            circle.getStyleClass().add(FILLED);

            Text monText = new Text();
            monText.textProperty().bind(expression(id, observableGameState, name));

            statistic.getChildren().addAll(circle, monText);
            vbox.getChildren().add(statistic);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create a vbox with given information
     *
     * @param playerStats vbox about current players statistics
     * @param message     message to show
     * @return vbox with information of the game
     */

    private static VBox createVbox(VBox playerStats, TextFlow message) {

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        VBox vBox = new VBox(playerStats, separator1, message);
        vBox.getStylesheets().addAll("info.css", "colors.css");
        return vBox;
    }

    //----------------------------------------------------------------------------------------------------
}



