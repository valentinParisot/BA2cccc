package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
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

class MapViewCreator {

    //----------------------------------------------------------------------------------------------------

    /**
     * create the view of the map
     *
     * @param observableGameState state of the game observable
     * @param objectProperty      a property containing the action manager to use when the player wants to seize a road
     * @param cardChooser         card chooser
     */

    public static Pane createMapView(ObservableGameState observableGameState,
     ObjectProperty<ActionHandlers.ClaimRouteHandler> objectProperty,
     CardChooser cardChooser){

        ImageView iv = new ImageView();
        Pane root = new Pane();
        root.getChildren().add(iv);
        root.getStylesheets().add("map.css");
        root.getStylesheets().add("colors.css");


        for (Route route : ChMap.routes()) {

            Group routes = new Group();

            routes.setOnMouseClicked((e) -> {

                List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(route);

                if(possibleClaimCards.size() == 1 ){

                    objectProperty.get().onClaimRoute(route, possibleClaimCards.get(0));

                }else {

                    ActionHandlers.ClaimRouteHandler claimRouteH = objectProperty.get() ;
                    ActionHandlers.ChooseCardsHandler chooseCardsH =
                            chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
                    cardChooser.chooseCards(possibleClaimCards, chooseCardsH);

                }

            });

            observableGameState.routeOwner(route).addListener((o,ov,on) ->{ routes.getStyleClass().add(on.name()); });

            for (int i = 1; i <= route.length(); i++) {

                routes.setId(route.id());

                Rectangle r1 = new Rectangle();
                r1.setWidth(36);
                r1.setHeight(12);
                r1.getStyleClass().add("filled");

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


                Group box = new Group();
                box.setId(route.id() + "_" + i);
                box.getChildren().addAll(voie, wagon);
                routes.getChildren().add(box);

            }

            if (route.color() == null) {
                routes.getStyleClass().addAll("route", route.level().toString(), "NEUTRAL");

            } else {
                routes.getStyleClass().addAll("route", route.level().toString(), route.color().toString());

            }

            root.getChildren().add(routes);

            routes.disableProperty().bind(objectProperty.isNull().or(observableGameState.canClaimRoute(route).not()));
        }

        return root ;

    }



    //----------------------------------------------------------------------------------------------------


    @FunctionalInterface
    interface CardChooser {

        /**
         * called when the player must choose the cards he wishes to use to seize a road.
         *
         * @param options The possibilities open to him
         * @param handler handler the action handler is intended to be used when he has made his choice
         */
        void chooseCards(List<SortedBag<Card>> options , ActionHandlers.ChooseCardsHandler handler );

    }

}


