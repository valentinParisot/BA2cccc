package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.function.Supplier;

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

    private final PlayerId playerId;
    private PlayerState playerState;
    private PublicGameState publicGameState;

    //----------------------------------------------------------------------------------------------------
    //-----------------------------------Public state of the game-----------------------------------------
    //----------------------------------------------------------------------------------------------------

    private final IntegerProperty ticketPercentage, cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final Map<Route, ObjectProperty<PlayerId>> routeOwner;

    //----------------------------------------------------------------------------------------------------
    //-----------------------------------Public state of each players-------------------------------------
    //----------------------------------------------------------------------------------------------------

    private final Map<PlayerId, IntegerProperty> ticketCounts, cardCounts, wagonCounts, playerPoints;

    //----------------------------------------------------------------------------------------------------
    //--------------------------------Private state of current player ID----------------------------------
    //----------------------------------------------------------------------------------------------------

    private final ObservableList<Ticket> ticketList;
    private final HashMap<Card, IntegerProperty> cardMultiplicity;
    private final Map<Route, BooleanProperty> canClaimRoute;

    //----------------------------------------------------------------------------------------------------

    /**
     * At creation, all the properties have their default value:
     * null for those containing an object,
     * 0 for those containing an integer,
     * false for those containing a Boolean value.
     *
     * @param playerId the id of the player "watching"
     */

    public ObservableGameState(PlayerId playerId) {

        this.playerId = playerId;
        playerState = null;
        publicGameState = null;

        ticketPercentage = new SimpleIntegerProperty();
        cardPercentage = new SimpleIntegerProperty();
        faceUpCards = createFaceUpCards();
        routeOwner = MapCreator(ChMap.routes(), SimpleObjectProperty::new);

        ticketList = FXCollections.observableArrayList();
        cardMultiplicity = createCardMultiplicity();
        canClaimRoute = MapCreator(ChMap.routes(), SimpleBooleanProperty::new);

        ticketCounts = MapCreator(PlayerId.ALL, SimpleIntegerProperty::new);
        cardCounts = MapCreator(PlayerId.ALL, SimpleIntegerProperty::new);
        wagonCounts = MapCreator(PlayerId.ALL, SimpleIntegerProperty::new);
        playerPoints = MapCreator(PlayerId.ALL, SimpleIntegerProperty::new);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * This method updates all of the properties described below based on these two states
     *
     * @param newGameState   the new PublicGameState
     * @param newPlayerState the new PlayerState
     */

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {

        this.publicGameState = newGameState;
        this.playerState = newPlayerState;

        PercentageHandler(newGameState);

        RouteHandler(newGameState);

        PlayerIdHandler(newGameState);

        TicketHandler(newPlayerState);

        CardHandler(newPlayerState);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Instantiates the faceUpCards to null
     *
     * @return a null list
     */

    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> list = new ArrayList<>();
        for (int i = 0; i < FACE_UP_CARDS_COUNT; i++) {
            list.add(new SimpleObjectProperty<>());
        }
        return list;
    }

    /**
     * Instantiates the multiplicity of each card in the player's hand to 0
     *
     * @return a null map
     */

    private static HashMap<Card, IntegerProperty> createCardMultiplicity() {

        HashMap<Card, IntegerProperty> map = new HashMap<>();

        for (Card card : Card.ALL) {
            map.put(card, new SimpleIntegerProperty());
        }
        return map;
    }

    //----------------------------------------------------------------------------------------------------
    //----------------------------------Public State of the game------------------------------------------
    //----------------------------------------------------------------------------------------------------

    /**
     * @return the percentage of a player's tickets
     */

    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentage;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the percentage of a player's cards
     */

    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentage;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param slot the index of the faceUpCard
     * @return the n° @slot faceUpCard property
     */

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route the route we want to know the owner of
     * @return the @route 's owner property
     */

    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routeOwner.get(route);
    }


    //----------------------------------------------------------------------------------------------------
    //--------------------------------Public State of each Player-----------------------------------------
    //----------------------------------------------------------------------------------------------------

    /**
     * @param id the id corresponding to the player
     * @return the number of tickets of @id
     */

    public ReadOnlyIntegerProperty ticketCount(PlayerId id) {
        return ticketCounts.get(id);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param id the id corresponding to the player
     * @return the number of cards of @id
     */

    public ReadOnlyIntegerProperty cardCount(PlayerId id) {
        return cardCounts.get(id);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param id the id corresponding to the player
     * @return the number of cars of @id
     */

    public ReadOnlyIntegerProperty wagonCount(PlayerId id) {
        return wagonCounts.get(id);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param id the id corresponding to the player
     * @return the amount of points of @id
     */

    public ReadOnlyIntegerProperty playerPoints(PlayerId id) {
        return playerPoints.get(id);
    }


    //----------------------------------------------------------------------------------------------------
    //--------------------------------Private state of current player ID----------------------------------
    //----------------------------------------------------------------------------------------------------

    /**
     * @return the list of tickets of the player watching
     */

    public ObservableList<Ticket> ticketList() {
        return ticketList;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param card any card of tCHu
     * @return the multiplicity of @card in the hand of the player watching
     */

    public ReadOnlyIntegerProperty cardMultiplicity(Card card) {
        return cardMultiplicity.get(card);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route any route of tCHu
     * @return if the player watching can claim @route
     */

    public ReadOnlyBooleanProperty canClaimRoute(Route route) {
        return canClaimRoute.get(route);
    }


    //----------------------------------------------------------------------------------------------------
    //-----------------------------------------Other methods----------------------------------------------
    //----------------------------------------------------------------------------------------------------

    /**
     * @return if the current player can draw a ticket
     */

    public boolean canDrawTickets() {
        return publicGameState.canDrawTickets();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return if the current player can draw a card
     */

    public boolean canDrawCards() {
        return publicGameState.canDrawCards();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route any route of tCHu
     * @return all the cards that the player can use to claim @route
     */

    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Create new map with the generics given
     *
     * @param OO  Given list of the generic types
     * @param PP  Given supplier of results
     * @param <O> Key
     * @param <P> Value
     * @return new HashMap with the given generic types
     */
    private <O, P> Map<O, P> MapCreator(List<O> OO, Supplier<P> PP) {
        Map<O, P> hash = new HashMap<>();
        for (O o : OO) {
            hash.put(o, PP.get());
        }
        return hash;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Handle the whole observable percentage
     * ticketPercentage and cardPercentage
     *
     * @param newGameState newGameState
     */

    private void PercentageHandler(PublicGameState newGameState) {
        ticketPercentage.set(newGameState.ticketsCount() * CENT / TOTAL_TICKETS_COUNT);
        cardPercentage.set(newGameState.cardState().deckSize() * CENT / TOTAL_CARDS_COUNT);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Handle the whole observable route and face up cards
     *
     * @param newGameState newGameState
     */

    private void RouteHandler(PublicGameState newGameState) {
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
        }

        Set<List<Station>> se = new HashSet<>();
        for (Route r : newGameState.claimedRoutes()) {
            se.add(r.stations());
        }
        for (Route r : canClaimRoute.keySet()) {

            canClaimRoute.get(r).setValue((newGameState.currentPlayerId().equals(playerId)) &&
                    (playerState.canClaimRoute(r)) && !(se.contains(r.stations())));

        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Handle the whole observable PlayerId
     *
     * @param newGameState newGameState
     */

    private void PlayerIdHandler(PublicGameState newGameState) {

        for (PlayerId id : PlayerId.ALL) {

            ticketCounts.get(id).set(newGameState.playerState(id).ticketCount());
            cardCounts.get(id).set(newGameState.playerState(id).cardCount());
            wagonCounts.get(id).set(newGameState.playerState(id).carCount());
            playerPoints.get(id).set(newGameState.playerState(id).claimPoints());

        }

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Handle the whole observable Ticket
     *
     * @param newPlayerState newPlayerState
     */

    private void TicketHandler(PlayerState newPlayerState) {
        ticketList.clear();
        for (Ticket t : newPlayerState.tickets()) {
            ticketList.add(t);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Handle the whole observable card
     *
     * @param newPlayerState newPlayerState
     */

    public void CardHandler(PlayerState newPlayerState) {
        for (Card card : Card.ALL) {
            cardMultiplicity.get(card).set(newPlayerState.cards().countOf(card));
        }
    }

    //----------------------------------------------------------------------------------------------------

}
