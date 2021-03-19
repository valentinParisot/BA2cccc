package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * PlayerState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class PlayerState extends PublicPlayerState {

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    //----------------------------------------------------------------------------------------------------

    /**
     * build a PlayerState
     * @param tickets sortedbag of ticksts
     * @param cards sortedbag of cards
     * @param routes list of routes
     */

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {

        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * the initial state of a player to whom the given initial cards have been dealt; in this initial state,
     * the player does not yet have any tickets, and has not taken any road
     * @param initialCards
     * @return initial state of a player
     * @throws IllegalArgumentException if the number of initial cards is not equal to 4
     */

    public static PlayerState initial(SortedBag<Card> initialCards) {

        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);
        PlayerState initial = new PlayerState(SortedBag.of(), initialCards, List.of());

        return initial;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return Sortedbag of tickets
     */

    public SortedBag<Ticket> tickets(){

        return this.tickets;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * which returns an identical state to the receiver, except that the player also has the given tickets
     * @param newTickets sortedbag of tickets
     * @return
     */

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){

        PlayerState withAddedTickets = new PlayerState(tickets.union(newTickets), this.cards, routes());

        return withAddedTickets;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return sortedbag of cards
     */

    public SortedBag<Card> cards(){

        return this.cards;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * which returns an identical state to the receiver, except that the player also has the given card
     * @param card
     * @return
     */

    public PlayerState withAddedCard(Card card){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(card);
        builder.build();

        SortedBag<Card> c = builder.build();

        PlayerState withAddedCard = new PlayerState(this.tickets, this.cards.union(c), routes());

        return withAddedCard;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * which returns an identical state to the receiver, except that the player also has the given cards,
     * @param additionalCards sortedbag of card
     * @return dentical state to the receiver with the cards
     */

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){

        PlayerState withAddedCards= new PlayerState(this.tickets, this.cards.union(additionalCards), routes());

        return withAddedCards;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns true iff the player can seize the given route, i.e. if he has enough cars left
     * and if he has the necessary cards
     * @param route route
     * @return true or false
     */

    public boolean canClaimRoute(Route route){

        return ((carCount() >= route.length()) && (!(this.possibleClaimCards(route).isEmpty())));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns the list of all the sets of cards that the player could use to take possession of the given route,
     * @param route route
     * @return list of all the sets of cards
     * @throws IllegalArgumentException if the player does not have enough cars to seize the road,
     */

    public List<SortedBag<Card>> possibleClaimCards(Route route){

        Preconditions.checkArgument((carCount() >= route.length()));

        List<SortedBag<Card>> p = route.possibleClaimCards();

        for(SortedBag<Card> s : p){
            if(!(cards.contains(s))){
                p.remove(s);
            }

        }
        return p;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns the list of all the sets of cards that the player could use to seize a tunnel,
     * sorted in ascending order of the number of locomotive cards,
     * knowing that he initially laid the cards initialCards,
     * that the 3 cards drawn from the top of the deck are drawnCards,
     * and these force the player to lay down additionalCardsCount cards
     * @param additionalCardsCount additionalcardscount
     * @param initialCards initialcards sortedbag of card
     * @param drawnCards drawncards sortedbag of card
     * @return the list of all the sets
     * @throws IllegalArgumentException if the number of additional cards is not between 1 and 3 (inclusive),
     * if the set of initial cards is empty or contains more than 2 different types of cards,
     * or if the set of cards drawn does not contain exactly 3 cards
     */

    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){

        Preconditions.checkArgument((additionalCardsCount <= 3 && additionalCardsCount >= 1)
                        && (!(initialCards.size() == 0))
                && (initialCards.toSet().size() <= 2)
         && (drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS));

        SortedBag<Card> cards2 = cards.difference(initialCards);

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();

        for(Card c : cards2) {
            if(initialCards.contains(c)){
               builder.add(c);
            }
            if(c.equals(Card.LOCOMOTIVE) ){
                builder.add(c);

            }
        }

        SortedBag<Card> cards3 = builder.build();

        Set<SortedBag<Card>> set = cards3.subsetsOfSize(additionalCardsCount);

        List<SortedBag<Card>> listSubsets = new ArrayList<>(set);

        listSubsets.sort(Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));

        return listSubsets;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns an identical state to the receiver,
     * except that the player has also seized the given route using the given cards,
     * @param route route
     * @param claimCards sortedbag of card
     * @return an identical state to the receiver with less cards and more routes
     */

    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){

        List<Route> routes = routes();
        routes.add(route);

        PlayerState withClaimedRoute= new PlayerState(this.tickets, this.cards.difference(claimCards), routes);

        return withClaimedRoute;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * which returns the number of points — possibly negative — obtained by the player thanks to his tickets
     * @return ticketPoints
     */

    public int ticketPoints(){

        int n = 0;

        for(Route r : routes()){
            if(r.station1().id() > n ){
                n = r.station1().id();
            }
            if(r.station2().id() > n ){
                n = r.station2().id();
            }
        }

        StationPartition.Builder c = new StationPartition.Builder(n+1);

        for(Route r : routes()){
            c.connect(r.station1(), r.station2());
        }

        StationConnectivity v = c.build();

        int ticketPoints = 0;

        for(Ticket t : tickets){

            ticketPoints += t.points(v);

        }

        return ticketPoints;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * returns all of the points obtained by the player at the end of the game,
     * namely the sum of the points returned by the claimPoints and ticketPoints methods
     * @return finalPoints
     */

    public int finalPoints(){

        return claimPoints() + ticketPoints();

    }

    //----------------------------------------------------------------------------------------------------

}