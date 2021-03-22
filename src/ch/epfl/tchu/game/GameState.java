package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public final class GameState extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final Map<PlayerId, PlayerState> playerState ;
    private final CardState cardstate ;
    private final PlayerId id1;
    private final PlayerId id2;

    private GameState ( Deck<Ticket> ticketDeck ,int ticketsCount, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer){

        super(ticketsCount, cardState, currentPlayerId, Map.copyOf(playerState),lastPlayer);
        this.ticketDeck = ticketDeck;
        this.playerState = playerState;
        this.id1 = currentPlayerId;
        this.id2 = lastPlayer;
        this.cardstate = cardState;

    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng){

         Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
         Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);

         PlayerId id1 = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));
         PlayerId id2 = null;

         Map<PlayerId, PlayerState> playerState = new TreeMap<>();

        for(PlayerId id : PlayerId.ALL){
            PlayerState P =  PlayerState.initial(cardDeck.topCards(4));
            playerState.put(id,P);
            cardDeck = cardDeck.withoutTopCards(4);
        }

        CardState cardstate = CardState.of(cardDeck);

        GameState g = new GameState(ticketDeck, ticketDeck.size(), cardstate, id1, playerState, id2);

        return g;

    }

    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(currentPlayerId());

    }

    public SortedBag<Ticket> topTickets(int count){

        Preconditions.checkArgument((count >=0) && (count <= ticketsCount()));

        return ticketDeck.topCards(count);

    }

    // - count ou pas
    // cardstate change ?
    // les id 1,2 ou current et last ? constructeur et la ?
    public GameState withoutTopTickets(int count){

        Preconditions.checkArgument((count >= 0) && (count <= ticketsCount()));

        GameState withoutTopTickets = new GameState(ticketDeck.withoutTopCards(count),
                ticketsCount() - count, cardstate, this.id1, this.playerState,this.id2);

        return withoutTopTickets;
    }

    public Card topCard(){
        Preconditions.checkArgument(!cardstate.isDeckEmpty());
        return cardstate.topDeckCard();
    }

    public GameState withoutTopCard(){

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        GameState withoutTopCard = new GameState(ticketDeck, ticketsCount() ,cardstate.withoutTopDeckCard(),
                this.id1 , this.playerState, this.id2);

        return withoutTopCard;
    }

    //on modifie ?
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){

        GameState withMoreDiscardedCards = new GameState(ticketDeck, ticketsCount(),
                cardstate.withMoreDiscardedCards(discardedCards), this.id1 , this.playerState, this.id2);

        return withMoreDiscardedCards;
    }


    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(cardstate.isDeckEmpty()){
            GameState g = new GameState(ticketDeck, ticketsCount(), cardstate.withDeckRecreatedFromDiscards(rng), id1,
                    playerState, id2);
            return g;
        }

        else return this;
    }

    // modif ??
    // player id . next ?
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(playerState(playerId).ticketCount() == 0);

        GameState withInitiallyChosenTickets = new GameState(ticketDeck ,ticketsCount() ,cardstate,
                playerId, modif(playerId, playerState.get(playerId).withAddedTickets(chosenTickets))
                , playerId.next());

        return  withInitiallyChosenTickets;
    }

    //id.next ?
    private Map<PlayerId, PlayerState> modif(PlayerId playerId,PlayerState playerState ){
        Map<PlayerId, PlayerState> playerState1 = new EnumMap<>(this.playerState);
        playerState1.put(playerId, playerState);
        return playerState1;
    }

    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){

        for(Ticket t : chosenTickets){
            Preconditions.checkArgument(drawnTickets.contains(t));
        }

        SortedBag<Ticket> ticketsNotchoose =  drawnTickets.difference(chosenTickets);



    }











}
