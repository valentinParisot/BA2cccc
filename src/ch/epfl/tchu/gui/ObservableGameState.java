package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.util.List;

public class ObservableGameState {


    private final PlayerId playerId;
    private final PlayerState playerState;
    private final PublicGameState gameState;
    /*public static final Integer TICKET_COUNT = 46;
    public static final Integer CARD_COUNT = 10;*/

    // etat publique de la partie
    private final IntegerProperty ticketPercentage, cardPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final HashMap<Route, ObjectProperty<PlayerId>> routeOwner;


    // etat publique de chacun des joueurs
    private final IntegerProperty ticketCount, cardCount, wagonCount, playerPoints;


    // etat privé de playerId passé au constructeur
    private final ObjectProperty<ObservableList<Ticket>> ticketList;
    private final HashMap<Card, IntegerProperty> cardMultiplicity;
    private final HashMap<Route, BooleanProperty> canClaimRoute;


    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
        playerState = null;
        gameState = null;


        ticketPercentage = createTicketPercentage();
        cardPercentage = createCardPercentage();
        faceUpCards = createFaceUpCards();
        routeOwner = createRouteOwner();


        ticketCount = createTicketCount();
        cardCount = createCardCount();
        wagonCount = createWagonCount();
        playerPoints = createPlayerPoints();


        ticketList = createTicketList();
        cardMultiplicity = createCardMultiplicity();
        canClaimRoute = createCanClaimRoute();
    }


    public void setState(PublicGameState gameState, PlayerState playerState){

    }


    //----------------------------------------------------------------------------------------------------
    private static IntegerProperty createTicketPercentage(){
        return new SimpleIntegerProperty();
        //Integer i = Math.round(gameState.ticketsCount() * 100 / TICKET_COUNT);
    }

    private static IntegerProperty createCardPercentage() {
        return new SimpleIntegerProperty();
    }

    private static List<ObjectProperty<Card>> createFaceUpCards(){
        List<ObjectProperty<Card>> list = new ArrayList<ObjectProperty<Card>>();
        for(int i = 0; i<5; i++){
            list.add(new SimpleObjectProperty<Card>());
        }
        return list;
    }

    private static HashMap<Route,ObjectProperty<PlayerId>> createRouteOwner(){

        HashMap<Route, ObjectProperty<PlayerId>> map = new HashMap<>();

        for (Route route : ChMap.routes()) {
            map.put(route, new SimpleObjectProperty<PlayerId>());
        }

        return map;
    }

    //----------------------------------------------------------------------------------------------------
    private static IntegerProperty createTicketCount(){
        return new SimpleIntegerProperty();
    }

    private static IntegerProperty createCardCount(){
        return new SimpleIntegerProperty();
    }

    private static IntegerProperty createWagonCount(){
        return new SimpleIntegerProperty();
    }

    private static IntegerProperty createPlayerPoints(){
        return new SimpleIntegerProperty();
    }

    //----------------------------------------------------------------------------------------------------
    private static ObjectProperty<ObservableList<Ticket>> createTicketList(){
        return new SimpleObjectProperty<ObservableList<Ticket>>();
    }
    
    private static HashMap<Card, IntegerProperty> createCardMultiplicity(){

        HashMap<Card, IntegerProperty> map = new HashMap<>();

        for (Card card : Card.ALL) {
           map.put(card, new SimpleIntegerProperty());
        }
        return map;
    }

    private static HashMap<Route, BooleanProperty> createCanClaimRoute(){

        HashMap<Route, BooleanProperty> map = new HashMap<>();

        for (Route route : ChMap.routes()) {
            map.put(route, new SimpleBooleanProperty());
        }
        return map;
    }







//---- etat publique de la partie -----------------------------------------------------------------------

    public ReadOnlyIntegerProperty ticketPercentage() {
        return ticketPercentage;
    }

    public ReadOnlyIntegerProperty cardPercentage() {
        return cardPercentage;
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }
    // comment l'avoir avec une route plutot qu'un int?
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routeOwner.get(route);
    }


//---- etat publique de chacun des joueurs --------------------------------------------------------------

    public ReadOnlyIntegerProperty ticketCount() {
        return ticketCount;
    }

    public ReadOnlyIntegerProperty cardCount() {
        return cardCount;
    }

    public ReadOnlyIntegerProperty wagonCunt() {
        return wagonCount;
    }

    public ReadOnlyIntegerProperty playerPoints() {
        return playerPoints;
    }


//---- etat privé de playerId passé au constructeur -----------------------------------------------------

    public ReadOnlyObjectProperty<ObservableList<Ticket>> ticketList() {
        return ticketList;
    }

    public ReadOnlyIntegerProperty cardMultiplicity(Card card) {
        return cardMultiplicity.get(card);
    }

    public ReadOnlyBooleanProperty canClaimRoute(Route route) {
        return canClaimRoute.get(route);
    }




    //----------------------------------------------------------------------------------------------------
    public boolean canDrawTickets(){
        return gameState.canDrawTickets();
    }

    public boolean canDrawCards(){
        return gameState.canDrawCards();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        return playerState.possibleClaimCards(route);
    }

    public boolean claimable(Route route){

        return ((playerState.carCount() >= route.length()) && (!(this.possibleClaimCards(route).isEmpty())));


    }

}
