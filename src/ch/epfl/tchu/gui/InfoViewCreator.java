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
    private final static String SETID_PLAYER_STATE = "player-stats";
    private final static String SETID_GAME_INFO = "game-info";
    private final static int RADIUS_CIRCLE = 5;

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerId
     * @param name
     * @param observableGameState
     * @param observableList
     * @return
     */

     public static VBox createInfoView(PlayerId playerId,
                                Map<PlayerId,String> name,
                                ObservableGameState observableGameState,
                                ObservableList<Text> observableList){


         VBox playerStats = new VBox();
         playerStats.setId(SETID_PLAYER_STATE );


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
         message.setId(SETID_GAME_INFO);

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

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param player
     * @param observableGameState
     * @param name
     * @return
     */

     private static StringExpression expression(PlayerId player,
                                                ObservableGameState observableGameState,
                                                Map<PlayerId,String> name ){

         StringExpression expression;

         if(player.name().equals(PLAYER_1)){

             expression = Bindings.format(PLAYER_STATS,
                     name.get(player),
                     observableGameState.ticketCount(),
                     observableGameState.cardCount(),
                     observableGameState.wagonCunt(),
                     observableGameState.playerPoints());

         }else {

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

}



