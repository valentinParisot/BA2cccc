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

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {

        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;

    }

    //----------------------------------------------------------------------------------------------------

    public static PlayerState initial(SortedBag<Card> initialCards) {

        Preconditions.checkArgument(initialCards.size() == Constants.INITIAL_CARDS_COUNT);

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

        PlayerState withAddedTickets = new PlayerState(tickets.union(newTickets), this.cards, routes());

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

        PlayerState withAddedCard = new PlayerState(this.tickets, this.cards.union(c), routes());

        return withAddedCard;

    }

    //----------------------------------------------------------------------------------------------------

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){

        PlayerState withAddedCards= new PlayerState(this.tickets, this.cards.union(additionalCards), routes());

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
            if(c == Card.LOCOMOTIVE){
                builder.add(c);

            }
        }

        SortedBag<Card> cards3 = builder.build();
        Set<SortedBag<Card>> set = cards3.subsetsOfSize(additionalCardsCount);
        List<SortedBag<Card>> listSubsets = new ArrayList<>(set);

        listSubsets.sort(
                Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));


        return listSubsets;
    }

    //----------------------------------------------------------------------------------------------------

    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){

        List<Route> routes = routes();
        routes.add(route);

        PlayerState withClaimedRoute= new PlayerState(this.tickets, this.cards.difference(claimCards), routes);

        return withClaimedRoute;

    }

    //----------------------------------------------------------------------------------------------------

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

    public int finalPoints(){

        return claimPoints() + ticketPoints();

    }

    //----------------------------------------------------------------------------------------------------

}