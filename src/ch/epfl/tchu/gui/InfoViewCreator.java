package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.crypto.Cipher;
import java.util.Map;

import static ch.epfl.tchu.gui.StringsFr.PLAYER_STATS;
import static javafx.beans.binding.Bindings.bindContent;

class InfoViewCreator {

     public static VBox createInfoView(PlayerId playerId,
                                Map<PlayerId,String> name,
                                ObservableGameState observableGameState,
                                ObservableList<Text> observableList){


         VBox playerStats = new VBox();
         playerStats.setId("player-stats");


         for (PlayerId player: PlayerId.ALL) {

             TextFlow statistic = new TextFlow();
             statistic.getStyleClass().add(player.name());


             Circle circle = new Circle();
             circle.setRadius(5);
             circle.getStyleClass().add("filled");

             Text monText = new Text();
             monText.textProperty().bind(expression(player,observableGameState,name));

             statistic.getChildren().addAll(circle,monText);
             playerStats.getChildren().add(statistic);

         }

         TextFlow message = new TextFlow();
         message.setId("game-info");

         if(observableList.size() <= 5 && observableList.size() > 0 ) {
             for (int i = 0; i < observableList.size(); i++) {

                 Text t = new Text();
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

     private static StringExpression expression(PlayerId player,
                                                ObservableGameState observableGameState,
                                                Map<PlayerId,String> name ){

         StringExpression expression;

         if(player.name().equals("PLAYER_1")){

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



}



