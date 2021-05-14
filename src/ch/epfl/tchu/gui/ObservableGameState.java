package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ch.epfl.tchu.game.Constants.*;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * ObservableGameState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class ObservableGameState {

    //----------------------------------------------------------------------------------------------------

    public static final int TOTAL_TICKETS_COUNT = 46;
    public static final int CENT = 100;

    //----------------------------------------------------------------------------------------------------

    private PlayerId playerId;
    private PlayerState playerState;
    private PublicGameState publicGameState;

    //----------------------------------------------------------------------------------------------------
    //-----------------------------------Public state of the game-----------------------------------------
    //----------------------------------------------------------------------------------------------------

    private final IntegerProperty ticketPercentage, cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final HashMap<Route, ObjectProperty<PlayerId>> routeOwner;

    //----------------------------------------------------------------------------------------------------
    //-----------------------------------Public state of each players-------------------------------------
    //----------------------------------------------------------------------------------------------------

    private final IntegerProperty ticketCount, cardCount, wagonCount, playerPoints;
    private final IntegerProperty ticketCount2, cardCount2, wagonCount2, playerPoints2;

    //----------------------------------------------------------------------------------------------------
    //--------------------------------Private state of current player ID----------------------------------
    //----------------------------------------------------------------------------------------------------

    private final ObjectProperty<ObservableList<Ticket>> ticketList;
    private final HashMap<Card, IntegerProperty> cardMultiplicity;
    private final HashMap<Route, BooleanProperty> canClaimRoute;

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerId
     */

    public ObservableGameState(PlayerId playerId) {

        this.playerId = playerId;
        playerState = null;
        publicGameState = null;

        ticketPercentage = new SimpleIntegerProperty();
        cardPercentage = new SimpleIntegerProperty();
        faceUpCards = createFaceUpCards();
        routeOwner = createRouteOwner();

        ticketCount = new SimpleIntegerProperty();
        cardCount = new SimpleIntegerProperty();
        wagonCount = new SimpleIntegerProperty();
        playerPoints = new SimpleIntegerProperty();

        ticketCount2 = new SimpleIntegerProperty();
        cardCount2 = new SimpleIntegerProperty();
        wagonCount2 = new SimpleIntegerProperty();
        playerPoints2 = new SimpleIntegerProperty();


        ticketList = new SimpleObjectProperty<>();
        cardMultiplicity = createCardMultiplicity();
        canClaimRoute = createCanClaimRoute();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *modifies all the properties
     * @param newGameState the new PublicGameState
     * @param newPlayerState the new PlayerState
     */

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        this.publicGameState = newGameState;
        this.playerId = newGameState.currentPlayerId();
        this.playerState = newPlayerState;

        ticketPercentage.set(newGameState.ticketsCount() * CENT / TOTAL_TICKETS_COUNT);
        cardPercentage.set(newGameState.cardState().deckSize() * CENT / TOTAL_CARDS_COUNT);

        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }

        for (Route route : ChMap.routes()) {
            if (newGameState.playerState(PLAYER_1).routes().contains(route)) {
                routeOwner.get(route).set(PLAYER_1);
            } else if (newGameState.playerState(PLAYER_2).routes().contains(route)) {
                routeOwner.get(route).set(PLAYER_2);
            } else {
                routeOwner.get(route).set(null);
            }
            canClaimRoute.get(route).set(claimable(route));
        }

        ticketCount.set(newPlayerState.ticketCount());
        cardCount.set(newPlayerState.cardCount());
        wagonCount.set(newPlayerState.carCount());
        playerPoints.set(newPlayerState.claimPoints());

        ticketCount2.set(newGameState.playerState(PLAYER_2).ticketCount());
        cardCount2.set(newGameState.playerState(PLAYER_2).cardCount());
        wagonCount2.set(newGameState.playerState(PLAYER_2).carCount());
        playerPoints2.set(newGameState.playerState(PLAYER_2).claimPoints());

        ticketList.set(FXCollections.observableList(newPlayerState.tickets().toList()));

        for (Card card : Card.ALL) {
            cardMultiplicity.get(card).set(newPlayerState.cards().countOf(card));
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> list = new ArrayList<ObjectProperty<Card>>();
        for (int i = 0; i < FACE_UP_CARDS_COUNT; i++) {
            list.add(new SimpleObjectProperty<>());
        }
        return list;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    private static HashMap<Route, ObjectProperty<PlayerId>> createRouteOwner() {

        HashMap<Route, ObjectProperty<PlayerId>> map = new HashMap<>();

        for (Route route : ChMap.routes()) {
            map.put(route, new SimpleObjectProperty<>());
        }

        return map;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    private static HashMap<Card, IntegerProperty> createCardMultiplicity() {

        HashMap<Card, IntegerProperty> map = new HashMap<>();

        for (Card card : Card.ALL) {
            map.put(card, new SimpleIntegerProperty());
        }
        return map;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    private static HashMap<Route, BooleanProperty> createCanClaimRoute() {

        HashMap<Route, BooleanProperty> map = new HashMap<>();

        for (Route route : ChMap.routes()) {
            map.put(route, new SimpleBooleanProperty());
        }
        return map;
    }

    //----------------------------------------------------------------------------------------------------
    //----------------------------------Public State of the game------------------------------------------
    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentage;
    }

    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentage;
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routeOwner.get(route);
    }

    //----------------------------------------------------------------------------------------------------
    //--------------------------------Public State of each Player-----------------------------------------
    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty ticketCount() {
        return ticketCount;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty cardCount() {
        return cardCount;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty wagonCunt() {
        return wagonCount;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty playerPoints() {
        return playerPoints;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty ticketCount2() {
        return ticketCount2;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty cardCount2() {
        return cardCount2;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty wagonCunt2() {
        return wagonCount2;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty playerPoints2() {
        return playerPoints2;
    }

    //----------------------------------------------------------------------------------------------------
    //--------------------------------Private state of current player ID----------------------------------
    //----------------------------------------------------------------------------------------------------


    public ReadOnlyObjectProperty<ObservableList<Ticket>> ticketList() {
        return ticketList;
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyIntegerProperty cardMultiplicity(Card card) {
        return cardMultiplicity.get(card);
    }

    //----------------------------------------------------------------------------------------------------

    public ReadOnlyBooleanProperty canClaimRoute(Route route) {
        return canClaimRoute.get(route);
    }

    //----------------------------------------------------------------------------------------------------

    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    //----------------------------------------------------------------------------------------------------

    public boolean canDrawCards() {
        return publicGameState.canDrawCards();
    }

    //----------------------------------------------------------------------------------------------------

    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }

    //----------------------------------------------------------------------------------------------------

    private boolean claimable(Route route) {
        return ((playerState.carCount() >= route.length()) && (!(this.possibleClaimCards(route).isEmpty())));

    }

    //----------------------------------------------------------------------------------------------------

}
