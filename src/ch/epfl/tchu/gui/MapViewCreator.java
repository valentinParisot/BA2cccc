package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.List;

/**
 * MapViewCreator
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public abstract class MapViewCreator {

    /**
     * create the view of the map
     *
     * @param observableGameState state of the game observable
     * @param ObjectProperty      a property containing the action manager to use when the player wants to seize a road
     * @param cardChooser         card chooser
     */
    /**public createMapView(ObservableGameState observableGameState,
                         ObjectProperty<ClaimRouteHandler> ObjectProperty,
                         CardChooser cardChooser) {


    }**/

    //----------------------------------------------------------------------------------------------------


    public static void createMapView() {

        Image image = new Image("map.css");
        ImageView iv1 = new ImageView(image);
        final Pane root = new Pane();
        root.getChildren().setAll(iv1);
        root.getStylesheets().addAll("map.css", "colors.css");

        Rectangle r1 = new Rectangle();
        r1.setWidth(36);
        r1.setHeight(12);
        r1.getStyleClass().addAll("filled");

        Circle c1 = new Circle();
        c1.setCenterX(12);
        c1.setCenterY(6);
        c1.setRadius(3);

        Circle c2 = new Circle();
        c2.setCenterX(24);
        c2.setCenterY(6);
        c2.setRadius(3);


        Rectangle voie = new Rectangle();
        voie.setWidth(36);
        voie.setHeight(12);
        voie.getStyleClass().addAll("track", "filled");

        Group wagon = new Group();
        wagon.getChildren().addAll(r1, c1, c2);
        wagon.getStyleClass().addAll("car");


        for (Route r : ChMap.routes()) {

            Group routes = new Group();
            routes.setId(r.id());
            routes.getStyleClass().addAll("route", "UNDERGROUND", "NEUTRAL");


            for (int i = 1; i <= r.length(); i++) {
                Group box = new Group();
                box.setId(r.id() + "_" + i);
                box.getChildren().addAll(voie, wagon);
                routes.getChildren().add(box);
            }

            root.getChildren().setAll(routes);
        }
    }

    //----------------------------------------------------------------------------------------------------


    @FunctionalInterface
    interface CardChooser {

        /**
         * called when the player must choose the cards he wishes to use to seize a road.
         *
         * @param options The possibilities open to him
         *                handler the action handler is intended to be used when he has made his choice
         */
        void chooseCards(List<SortedBag<Card>> options/*, ChooseCardsHandler handler */);

    }
}


