package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import java.util.*;

public final class GameState extends PublicGameState {

    private final Deck<Ticket> ticketDeck;
    private final Map<PlayerId, PlayerState> playerState ;
    private final CardState cardstate ;
    private final PlayerId currentPlayerId;
    private final PlayerId lastPlayerId;

    private GameState ( Deck<Ticket> ticketDeck ,int ticketsCount, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer){

        super(ticketsCount, cardState, currentPlayerId, Map.copyOf(playerState),lastPlayer);
        this.ticketDeck = ticketDeck;
        this.playerState = playerState;
        this.currentPlayerId = currentPlayerId;
        this.lastPlayerId = lastPlayer;
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
                ticketsCount() - count, cardstate, this.currentPlayerId, this.playerState,this.lastPlayerId);

        return withoutTopTickets;
    }

    public Card topCard(){
        Preconditions.checkArgument(!cardstate.isDeckEmpty());
        return cardstate.topDeckCard();
    }

    public GameState withoutTopCard(){

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        GameState withoutTopCard = new GameState(ticketDeck, ticketsCount() ,cardstate.withoutTopDeckCard(),
                this.currentPlayerId, this.playerState, this.lastPlayerId);

        return withoutTopCard;
    }

    //on modifie ?
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){

        GameState withMoreDiscardedCards = new GameState(ticketDeck, ticketsCount(),
                cardstate.withMoreDiscardedCards(discardedCards), this.currentPlayerId, this.playerState, this.lastPlayerId);

        return withMoreDiscardedCards;
    }


    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(cardstate.isDeckEmpty()){
            GameState g = new GameState(ticketDeck, ticketsCount(), cardstate.withDeckRecreatedFromDiscards(rng), currentPlayerId,
                    playerState, lastPlayerId);
            return g;
        }

        else return this;
    }

    // modif ??
    // player id . next ?
    // on modif playerstate ?
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

    //current id ?
    //modif ?
    //comment on modifie le deck de tickdt ?
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){

        for(Ticket t : chosenTickets){
            Preconditions.checkArgument(drawnTickets.contains(t));
        }

        GameState withChosenAdditionalTickets = new GameState(this.ticketDeck, ticketsCount(), this.cardstate,
                currentPlayerId(),modif(currentPlayerId, playerState.get(currentPlayerId()).withAddedTickets(chosenTickets)),
                lastPlayerId);

        withChosenAdditionalTickets.withoutTopTickets(drawnTickets.size());

        return withChosenAdditionalTickets;

    }

    //on peut modifier les attributs comme ca ? est ce que le code doit etre plus claire ?
    public GameState withDrawnFaceUpCard(int slot){

        Preconditions.checkArgument(canDrawnCards());

        GameState withDrawnFaceUpCard = new GameState(this.ticketDeck, ticketsCount(), cardstate.withDrawnFaceUpCard(slot),currentPlayerId(),
                modif(currentPlayerId, playerState.get(currentPlayerId()).withAddedCard(cardstate.faceUpCard(slot))),
                lastPlayerId);

        return withDrawnFaceUpCard;

    }

    //on doit modifier le cardstate ? ou ca se fait deja
    public GameState withBlindlyDrawnCard(){

        Preconditions.checkArgument(canDrawnCards());

        GameState withBlindlyDrawnCard = new GameState(this.ticketDeck, ticketsCount(), cardstate.withoutTopDeckCard(), currentPlayerId,
                modif(currentPlayerId, playerState.get(currentPlayerId()).withAddedCard(cardstate.topDeckCard())),
                lastPlayerId);

        return withBlindlyDrawnCard;
    }

    //modifier le card state ou pas ? ou with claimed route le fait
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){

        GameState withClaimedRoute = new GameState(this.ticketDeck, ticketsCount(), cardstate.withMoreDiscardedCards(cards),
                currentPlayerId,
                modif(currentPlayerId, playerState.get(currentPlayerId()).withClaimedRoute(route, cards)), lastPlayerId);

        return withClaimedRoute;

    }

    public boolean lastTurnBegins(){

        return (lastPlayerId == null && playerState.get(currentPlayerId()).carCount() <= 2);

    }

    //remplacer comment pour les id ? comme ca  ou avec une egalite de variable
    // quand le current remplace pas on doit metttre null ?
    //methode plus rapide ??
    public GameState forNextTurn(){

        if(lastTurnBegins()){
            GameState forNextTurn = new GameState(this.ticketDeck, this.ticketsCount(), this.cardstate,
                    lastPlayerId, playerState, currentPlayerId);
            return forNextTurn;
        }
        else {
            GameState forNextTurn = new GameState(this.ticketDeck, this.ticketsCount(), this.cardstate,
                    lastPlayerId, playerState, null);
            return forNextTurn;

        }

    }













}
