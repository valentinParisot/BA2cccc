package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

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
    private final List<Route> routes;

    //----------------------------------------------------------------------------------------------------

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {

        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
        this.routes = routes;

    }

    //----------------------------------------------------------------------------------------------------

    public static PlayerState initial(SortedBag<Card> initialCards) {

        Preconditions.checkArgument(initialCards.size() == 4);

        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        SortedBag<Ticket> tickets = builder.build();

        ArrayList<Route> routes = new ArrayList<>();

        PlayerState initial = new PlayerState(tickets, initialCards, routes);

        return initial;

    }

    //----------------------------------------------------------------------------------------------------

    public SortedBag<Ticket> tickets(){

        return tickets;
    }

    //----------------------------------------------------------------------------------------------------

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){

        PlayerState withAddedTickets = new PlayerState(tickets.union(newTickets), this.cards, this.routes);

        return withAddedTickets;
    }

    //----------------------------------------------------------------------------------------------------

    public SortedBag<Card> cards(){

        return cards;
    }

    //----------------------------------------------------------------------------------------------------

    public PlayerState withAddedCard(Card card){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(card);
        builder.build();

        SortedBag<Card> c = builder.build();

        PlayerState withAddedCard = new PlayerState(this.tickets, this.cards.union(c), this.routes);

        return withAddedCard;

    }

    //----------------------------------------------------------------------------------------------------

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){

        PlayerState withAddedCards= new PlayerState(this.tickets, this.cards.union(additionalCards), this.routes);

        return withAddedCards;

    }

    //----------------------------------------------------------------------------------------------------

    public boolean canClaimRoute(Route route){

        List<SortedBag<Card>> possibleClaimCards = possibleClaimCards(route);

        return ((carCount() >= route.length()) && (!(possibleClaimCards.isEmpty())));
    }

    //----------------------------------------------------------------------------------------------------

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

    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){

        return null;
    }

    //----------------------------------------------------------------------------------------------------

    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){

        routes.add(route);

        PlayerState withClaimedRoute= new PlayerState(this.tickets, this.cards.difference(claimCards), routes);

        return withClaimedRoute;

    }

    //----------------------------------------------------------------------------------------------------

    public int ticketPoints(){


        // besoin du batissuer statioon partiton
        return 0;

    }

    //----------------------------------------------------------------------------------------------------

    public int finalPoints(){

        return claimPoints() + ticketPoints();

    }

    //----------------------------------------------------------------------------------------------------

}