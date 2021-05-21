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

    private final static int RECTANGLE_WIDTH = 36;
    private final static int RECTANGLE_HEIGHT = 12;
    private final static int CIRCLE_RADIUS = 3;
    private final static int C1_X = 12;
    private final static int C1_Y = 6;
    private final static int C2_X = 24;
    private final static int C2_Y = 6;

    private final static String LOW_LINE = "_";
    private final static String ROUTE = "route";
    private final static String FILLED = "filled";
    private final static String NEUTRAL = "NEUTRAL";
    private final static String CAR = "car";
    private final static String TRACK = "track";
    private final static String MAP_CSS = "map.css";
    private final static String COLOR_CSS = "colors.css";

    //----------------------------------------------------------------------------------------------------

    /**
     * Create the view of the map
     *
     * @param observableGameState state of the game observable
     * @param objectProperty      a property containing the action manager to use when the player wants to seize a road
     * @param cardChooser         card chooser
     */

    public static Pane createMapView(ObservableGameState observableGameState,
                                     ObjectProperty<ActionHandlers.ClaimRouteHandler> objectProperty,
                                     CardChooser cardChooser) {

        ImageView iv = new ImageView();
        Pane root = new Pane();
        root.getChildren().add(iv);
        root.getStylesheets().addAll(MAP_CSS, COLOR_CSS);

        Creator(observableGameState, objectProperty, cardChooser, root);

        return root;
    }

    //----------------------------------------------------------------------------------------------------


    @FunctionalInterface
    interface CardChooser {

        /**
         * Called when the player must choose the cards he wishes to use to seize a road.
         *
         * @param options The possibilities open to him
         * @param handler handler the action handler is intended to be used when he has made his choice
         */
        void chooseCards(List<SortedBag<Card>> options, ActionHandlers.ChooseCardsHandler handler);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Generate all geometrics figure that the map needs
     *
     * @param route  route in the map
     * @param routes group of routes in the map
     */

    private static void generator(Route route, Group routes) {

        for (int i = 1; i <= route.length(); i++) {

            routes.setId(route.id());

            Rectangle r1 = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            r1.getStyleClass().add(FILLED);

            Circle c1 = new Circle(CIRCLE_RADIUS);
            c1.setCenterX(C1_X);
            c1.setCenterY(C1_Y);

            Circle c2 = new Circle(CIRCLE_RADIUS);
            c2.setCenterX(C2_X);
            c2.setCenterY(C2_Y);

            Rectangle rectangle = new Rectangle(RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
            rectangle.getStyleClass().addAll(TRACK, FILLED);

            Group wagon = new Group(r1, c1, c2);
            wagon.getStyleClass().addAll(CAR);


            Group box = new Group(rectangle, wagon);
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

    /**
     * Create and add the property of the map
     *
     * @param observableGameState observableGameState of the game
     * @param objectProperty      different property given
     * @param cardChooser         useful when the player want to choose a card
     * @param root                current pane, where the method must put the whole work done
     */

    private static void Creator(ObservableGameState observableGameState,
                                ObjectProperty<ActionHandlers.ClaimRouteHandler> objectProperty,
                                CardChooser cardChooser, Pane root) {

        for (Route route : ChMap.routes()) {

            Group routes = new Group();

            routes.setOnMouseClicked((e) -> {

                List<SortedBag<Card>> possibleClaimCards = observableGameState.possibleClaimCards(route);

                if (possibleClaimCards.size() == 1) {

                    objectProperty.get().onClaimRoute(route, possibleClaimCards.get(0));

                } else {

                    ActionHandlers.ClaimRouteHandler claimRouteH = objectProperty.get();

                    ActionHandlers.ChooseCardsHandler chooseCardsH =
                            chosenCards -> claimRouteH.onClaimRoute(route, chosenCards);
                    cardChooser.chooseCards(possibleClaimCards, chooseCardsH);
                }
            });

            observableGameState.routeOwner(route).addListener((o, ov, on) -> routes.getStyleClass().add(on.name()));

            generator(route, routes);

            root.getChildren().add(routes);
            routes.disableProperty().bind(objectProperty.isNull().or(observableGameState.canClaimRoute(route).not()));
        }
    }

    //----------------------------------------------------------------------------------------------------
}


