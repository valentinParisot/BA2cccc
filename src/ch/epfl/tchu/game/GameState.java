package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public final class GameState extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final Map<PlayerId, PlayerState> playerState ;
    private final CardState cardstate ;
    private final PlayerId id1;
    private final PlayerId id2;
    private final int ticketsCount;

    private  GameState ( Deck<Ticket> ticketDeck ,int ticketsCount, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer){

        super(ticketsCount, cardState, currentPlayerId, Map.copyOf(playerState),lastPlayer);
        this.ticketDeck = ticketDeck;
        this.playerState = playerState;
        this.id1 = currentPlayerId;
        this.id2 = lastPlayer;
        this.cardstate = cardState;
        this.ticketsCount = ticketsCount;

    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng){

         Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
         Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);

         PlayerId id1 = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));
         PlayerId id2 = id1.next();

         Map<PlayerId, PlayerState> playerState = new TreeMap<>();

        for(PlayerId id : PlayerId.ALL){
            PlayerState P =  PlayerState.initial(cardDeck.topCards(4));
            playerState.put(id,P);
            cardDeck = cardDeck.withoutTopCards(4);
        }

        CardState cardstate = CardState.of(cardDeck);

        GameState g = new GameState(ticketDeck, ticketDeck.size(), cardstate, id1,playerState, id2);

        return g;

    }

    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(CurrentPlayerId());

    }

    public SortedBag<Ticket> topTickets(int count){

        Preconditions.checkArgument((count >=0) && (count <= ticketsCount));

        return ticketDeck.topCards(count);

    }

    public GameState withoutTopTickets(int count){

        Preconditions.checkArgument(count >= 0 && count <= cardstate.deckSize());
        GameState withoutTopTickets = new GameState(ticketDeck.withoutTopCards(count),
                ticketsCount, this.cardstate, this.id1, this.playerState,this.id2);
        return withoutTopTickets;
    }

    public Card topCard(){
        Preconditions.checkArgument(!cardstate.isDeckEmpty());
        return cardstate.topDeckCard();
    }

    public GameState withoutTopCard(){

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        GameState withoutTopCard = new GameState(ticketDeck, ticketsCount ,cardstate.withoutTopDeckCard(),
                this.id1 , this.playerState, this.id2);

        return withoutTopCard;
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){

        GameState withMoreDiscardedCards = new GameState(ticketDeck, ticketsCount,
                cardstate.withMoreDiscardedCards(discardedCards), this.id1 , this.playerState, this.id2);

        return withMoreDiscardedCards;
    }

    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(cardstate.isDeckEmpty()){
            GameState g = new GameState(ticketDeck, ticketsCount, cardstate.withDeckRecreatedFromDiscards(rng), id1,
                    playerState, id2);
            return g;
        }

        else return this;
    }

    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(playerState(playerId).ticketCount() == 0);

        GameState withInitiallyChosenTickets = new GameState(ticketDeck ,chosenTickets.size(),cardstate,
                playerId, withAddedTickets(chosenTickets), playerId.next());

        return  withInitiallyChosenTickets;
    }

    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(!drawnTickets.union(chosenTickets).isEmpty());
        SortedBag<Ticket> tickets =  drawnTickets.union(chosenTickets);
        ticketDeck.differe


    }











}
