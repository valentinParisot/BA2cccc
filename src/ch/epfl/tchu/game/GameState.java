package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.*;

/**
 * GameState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class GameState  extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final Map<PlayerId, PlayerState> playerState ;
    private final CardState cardstate ;

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param ticketDeck
     * @param cardState
     * @param currentPlayerId
     * @param playerState
     * @param lastPlayer
     */

    private GameState ( Deck<Ticket> ticketDeck , CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer){

        super(ticketDeck.size(), cardState, currentPlayerId, Map.copyOf(playerState),lastPlayer);
        this.ticketDeck = ticketDeck;
        this.playerState = playerState;
        this.cardstate = cardState;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param tickets
     * @param rng
     * @return
     */

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

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerId given identity
     * @return
     */

    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(currentPlayerId());

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param count
     * @return
     */

    public SortedBag<Ticket> topTickets(int count){

        Preconditions.checkArgument((count >=0) && (count <= ticketsCount()));

        return ticketDeck.topCards(count);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param count
     * @return
     */

    public GameState withoutTopTickets(int count){

        Preconditions.checkArgument((count >= 0) && (count <= ticketsCount()));

        GameState withoutTopTickets = new GameState(ticketDeck.withoutTopCards(count), cardstate, currentPlayerId(),
                playerState, lastPlayer());

        return withoutTopTickets;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public Card topCard(){
        Preconditions.checkArgument(!cardstate.isDeckEmpty());
        return cardstate.topDeckCard();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public GameState withoutTopCard(){

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        GameState withoutTopCard = new GameState(ticketDeck, cardstate.withoutTopDeckCard(),
                currentPlayerId(), playerState,lastPlayer());

        return withoutTopCard;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param discardedCards
     * @return
     */

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){

        GameState withMoreDiscardedCards = new GameState(ticketDeck,
                cardstate.withMoreDiscardedCards(discardedCards),
                currentPlayerId(), playerState, lastPlayer());

        return withMoreDiscardedCards;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param rng
     * @return
     */

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

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerId
     * @param chosenTickets
     * @return
     */

    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(playerState(playerId).ticketCount() == 0);

        GameState withInitiallyChosenTickets = new GameState(ticketDeck ,cardstate,
                currentPlayerId(), modif(playerId, playerState.get(playerId).withAddedTickets(chosenTickets))
                ,lastPlayer());

        return  withInitiallyChosenTickets;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerId
     * @param playerState
     * @return
     */

    private Map<PlayerId, PlayerState> modif(PlayerId playerId,PlayerState playerState ){
        Map<PlayerId, PlayerState> playerState1 = new EnumMap<>(this.playerState);
        playerState1.put(playerId, playerState);
        return playerState1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param drawnTickets
     * @param chosenTickets
     * @return
     */

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

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param slot
     * @return
     */

    public GameState withDrawnFaceUpCard(int slot){

        Preconditions.checkArgument(canDrawCards());

        GameState withDrawnFaceUpCard = new GameState(ticketDeck, cardstate.withDrawnFaceUpCard(slot), currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardstate.faceUpCard(slot))),
                lastPlayer());

        return withDrawnFaceUpCard;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public GameState withBlindlyDrawnCard(){

        Preconditions.checkArgument(canDrawCards());

        GameState withBlindlyDrawnCard = new GameState(ticketDeck, cardstate.withoutTopDeckCard(),
                currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardstate.topDeckCard())),
                lastPlayer());

        return withBlindlyDrawnCard;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param route
     * @param cards
     * @return
     */

    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){

        GameState withClaimedRoute = new GameState(ticketDeck,
                cardstate.withMoreDiscardedCards(cards), currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withClaimedRoute(route, cards)), lastPlayer());

        return withClaimedRoute;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public boolean lastTurnBegins(){

        return (playerState.get(currentPlayerId()).carCount() <= 2);
        //lastPlayer() == null &&

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public GameState forNextTurn(){

        if(lastTurnBegins()){

            GameState forNextTurn = new GameState(ticketDeck,cardstate,
                    currentPlayerId().next(), playerState, currentPlayerId());
            return forNextTurn;
        }
        else {
            GameState forNextTurn = new GameState(ticketDeck, cardstate,
                    currentPlayerId().next(), playerState, lastPlayer());
            return forNextTurn;

        }

    }

    //----------------------------------------------------------------------------------------------------


}
