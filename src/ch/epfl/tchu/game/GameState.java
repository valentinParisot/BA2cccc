package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.*;

public final class GameState extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final Map<PlayerId, PlayerState> playerState ;
    private final CardState cardstate ;

    private GameState ( Deck<Ticket> ticketDeck , CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer){

        super(ticketDeck.size(), cardState, currentPlayerId, Map.copyOf(playerState),lastPlayer);
        this.ticketDeck = ticketDeck;
        this.playerState = playerState;
        this.cardstate = cardState;

    }

    public static GameState initial(SortedBag<Ticket> tickets, Random rng){

         Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
         Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);

         PlayerId firstplayer = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));

         Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for(PlayerId id : PlayerId.ALL){
            PlayerState P =  PlayerState.initial(cardDeck.topCards(4));
            playerState.put(id,P);
            cardDeck = cardDeck.withoutTopCards(4);
        }

        CardState cardstate = CardState.of(cardDeck);

        GameState initial = new GameState(ticketDeck, cardstate, firstplayer, playerState, null);

        return initial;

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

    public GameState withoutTopTickets(int count){

        Preconditions.checkArgument((count >= 0) && (count <= ticketsCount()));

        GameState withoutTopTickets = new GameState(ticketDeck.withoutTopCards(count), cardstate, currentPlayerId(),
                playerState, lastPlayer());

        return withoutTopTickets;
    }

    public Card topCard(){
        Preconditions.checkArgument(!cardstate.isDeckEmpty());
        return cardstate.topDeckCard();
    }

    public GameState withoutTopCard(){

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        GameState withoutTopCard = new GameState(ticketDeck, cardstate.withoutTopDeckCard(),
                currentPlayerId(), playerState,lastPlayer());

        return withoutTopCard;
    }

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){

        GameState withMoreDiscardedCards = new GameState(ticketDeck,
                cardstate.withMoreDiscardedCards(discardedCards),
                currentPlayerId(), playerState, lastPlayer());

        return withMoreDiscardedCards;
    }


    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(cardstate.isDeckEmpty()){
            GameState withCardsDeckRecreatedIfNeeded = new GameState(ticketDeck,
                    cardstate.withDeckRecreatedFromDiscards(rng),
                    currentPlayerId(), playerState, lastPlayer());

            return withCardsDeckRecreatedIfNeeded;
        }

        else {

            GameState gamestate = new GameState(ticketDeck, cardstate, currentPlayerId(), playerState, lastPlayer());

            return gamestate;
        }
    }


    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(playerState(playerId).ticketCount() == 0);

        GameState withInitiallyChosenTickets = new GameState(ticketDeck ,cardstate,
                currentPlayerId(), modif(playerId, playerState.get(playerId).withAddedTickets(chosenTickets))
                ,lastPlayer());

        return  withInitiallyChosenTickets;
    }


    private Map<PlayerId, PlayerState> modif(PlayerId playerId,PlayerState playerState ){
        Map<PlayerId, PlayerState> playerState1 = new EnumMap<>(this.playerState);
        playerState1.put(playerId, playerState);
        return playerState1;
    }


    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){

        for(Ticket t : chosenTickets){
            Preconditions.checkArgument(drawnTickets.contains(t));
        }

        GameState withChosenAdditionalTickets = new GameState(ticketDeck.withoutTopCards(drawnTickets.size()), cardstate,
                currentPlayerId(),modif(currentPlayerId(),
                playerState.get(currentPlayerId()).withAddedTickets(chosenTickets)),
                lastPlayer());

        return withChosenAdditionalTickets;

    }

    public GameState withDrawnFaceUpCard(int slot){

        Preconditions.checkArgument(canDrawCards());

        GameState withDrawnFaceUpCard = new GameState(ticketDeck, cardstate.withDrawnFaceUpCard(slot), currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardstate.faceUpCard(slot))),
                lastPlayer());

        return withDrawnFaceUpCard;

    }

    public GameState withBlindlyDrawnCard(){

        Preconditions.checkArgument(canDrawCards());

        GameState withBlindlyDrawnCard = new GameState(ticketDeck, cardstate.withoutTopDeckCard(),
                currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardstate.topDeckCard())),
                lastPlayer());

        return withBlindlyDrawnCard;
    }


    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){

        GameState withClaimedRoute = new GameState(ticketDeck,
                cardstate.withMoreDiscardedCards(cards), currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withClaimedRoute(route, cards)), lastPlayer());

        return withClaimedRoute;

    }

    public boolean lastTurnBegins(){

        return (lastPlayer() == null && playerState.get(currentPlayerId()).carCount() <= 2);

    }

    public GameState forNextTurn(){

        if(lastTurnBegins()){

            GameState forNextTurn = new GameState(ticketDeck,cardstate,
                    currentPlayerId().next(), playerState, currentPlayerId());
            return forNextTurn;
        }
        else {
            GameState forNextTurn = new GameState(ticketDeck, cardstate,
                    currentPlayerId().next(), playerState, null);
            return forNextTurn;

        }

    }


}
