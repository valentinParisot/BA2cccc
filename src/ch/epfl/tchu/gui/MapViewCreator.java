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
    private final static String ROUTE = "route";
    private final static String FILLED = "filled";
    private final static String NEUTRAL = "NEUTRAL";
    private final static String CAR = "car";
    private final static String TRACK = "track";

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
        root.getStylesheets().addAll("map.css","colors.css");


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

    /**
     *
     * @param route
     * @param routes
     */

    private static void generator(Route route, Group routes){

        for (int i = 1; i <= route.length(); i++) {

            routes.setId(route.id());

            Rectangle r1 = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
            r1.getStyleClass().add(FILLED);

            Circle c1 = new Circle(CIRCLE_RADIUS);
            c1.setCenterX(12);
            c1.setCenterY(6);

            Circle c2 = new Circle(CIRCLE_RADIUS);
            c2.setCenterX(24);
            c2.setCenterY(6);

            Rectangle voie = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
            voie.getStyleClass().addAll(TRACK, FILLED);

            Group wagon = new Group(r1, c1, c2);
            wagon.getStyleClass().addAll(CAR);


            Group box = new Group(voie,wagon);
            box.setId(route.id() + LOW_LINE + i);
            routes.getChildren().add(box);
        }

        if (route.color() == null) {
            routes.getStyleClass().addAll(ROUTE, route.level().toString(), NEUTRAL);

        } else {
            routes.getStyleClass().addAll(ROUTE, route.level().toString(), route.color().toString());

        }
    }

    //----------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------
    //------------------------------------------code de déboguage-----------------------------------------
    //----------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------

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
    //----------------------------------------------------------------------------------------------------
    //------------------------------------------code de déboguage-----------------------------------------
    //----------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------------

}


