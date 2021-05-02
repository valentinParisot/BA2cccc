package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Route;
import javafx.beans.property.ObjectProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    private final static int RECTANGLE_WIDTH = 36;
    private final static int RECTANGLE_HEIGHT = 12;
    private final static int CIRCLE_RADIUS = 3;
    private final static String LOW_LINE = "_";

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

            observableGameState.routeOwner(route).addListener((o,ov,on) -> routes.getStyleClass().add(on.name()));


            generator(route,routes);
            dumpTree(routes);//debug

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

    //----------------------------------------------------------------------------------------------------

    private static void generator(Route route, Group routes){

        for (int i = 1; i <= route.length(); i++) {

            routes.setId(route.id());

            Rectangle r1 = new Rectangle();
            r1.setWidth(RECTANGLE_WIDTH);
            r1.setHeight(RECTANGLE_HEIGHT);
            r1.getStyleClass().add("filled");

            Circle c1 = new Circle();
            c1.setCenterX(12);
            c1.setCenterY(6);
            c1.setRadius(CIRCLE_RADIUS);

            Circle c2 = new Circle();
            c2.setCenterX(24);
            c2.setCenterY(6);
            c2.setRadius(CIRCLE_RADIUS);


            Rectangle voie = new Rectangle();
            voie.setWidth(RECTANGLE_WIDTH);
            voie.setHeight(RECTANGLE_HEIGHT);
            voie.getStyleClass().addAll("track", "filled");

            Group wagon = new Group();
            wagon.getChildren().addAll(r1, c1, c2);
            wagon.getStyleClass().addAll("car");


            Group box = new Group();
            box.setId(route.id() + LOW_LINE + i);
            box.getChildren().addAll(voie, wagon);
            routes.getChildren().add(box);
        }

        if (route.color() == null) {
            routes.getStyleClass().addAll("route", route.level().toString(), "NEUTRAL");

        } else {
            routes.getStyleClass().addAll("route", route.level().toString(), route.color().toString());

        }
    }


    //--------------------------------------------code de déboguage-----------------------------------------

    private static void dumpTree(Node root) {
        dumpTree(0, root);
    }

    private static void dumpTree(int indent, Node root) {
        System.out.printf("%s%s (id: %s, classes: [%s])%n",
                " ".repeat(indent),
                root.getTypeSelector(),
                root.getId(),
                String.join(", ", root.getStyleClass()));
        if (root instanceof Parent) {
            Parent parent = ((Parent) root);
            for (Node child : parent.getChildrenUnmodifiable())
                dumpTree(indent + 2, child);
        }
    }

    //----------------------------------------------------------------------------------------------------

}


