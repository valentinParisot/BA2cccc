package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
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
    private final List<ObjectProperty<PlayerId>> routeOwner;


    // etat publique de chacun des joueurs
    private final IntegerProperty ticketCount, cardCount, wagonCount, playerPoints;


    // etat privé de playerId passé au constructeur
    private final ObjectProperty<ObservableList<Ticket>> ticketList;
    private final List<IntegerProperty> cardMultiplicity;
    private final List<BooleanProperty> canClaimRoute;


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

    private static List<ObjectProperty<PlayerId>> createRouteOwner(){
        List<ObjectProperty<PlayerId>> list = new ArrayList<ObjectProperty<PlayerId>>();

        for (Route route : ChMap.routes()) {
            list.add(new SimpleObjectProperty<PlayerId>());
        }

        return list;
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
    
    private static List<IntegerProperty> createCardMultiplicity(){
        List<IntegerProperty> list = new ArrayList<>();

        for (Card card : Card.ALL) {
            list.add(new SimpleIntegerProperty());
        }
        return list;
    }

    private static List<BooleanProperty> createCanClaimRoute(){
        List<BooleanProperty> list = new ArrayList<>();

        for (Route route : ChMap.routes()) {
            list.add(new SimpleBooleanProperty());
        }
        return list;
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
    public ReadOnlyObjectProperty<PlayerId> routeOwner(int slot) {
        return routeOwner.get(slot);
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

    public ReadOnlyIntegerProperty cardMultiplicity(int slot) {
        return cardMultiplicity.get(slot);
    }

    public ReadOnlyBooleanProperty canClaimRoute(int slot) {
        return canClaimRoute.get(slot);
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
