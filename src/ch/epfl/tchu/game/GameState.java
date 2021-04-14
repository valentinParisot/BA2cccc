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

public final class GameState extends PublicGameState {

    //----------------------------------------------------------------------------------------------------

    private final static int TOP_CARDS_CHOSEN = 4;
    private final static int MIN_CAR = 2;

    //----------------------------------------------------------------------------------------------------

    /**
     * @param tickets tickets given
     * @param rng     random generator
     * @return the initial state of a game of tCHu in which the bank of tickets contains the tickets given
     * and the deck of cards contains the cards of Constants.ALL_CARDS, without the 8 (2 × 4) above,
     * distributed to the players; these pickaxes are shuffled by means of the given random generator,
     * which is also used to randomly choose the identity of the first player
     */

    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {

        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);
        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(PlayerId.COUNT));

        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);

        for (PlayerId id : PlayerId.ALL) {

            PlayerState P = PlayerState.initial(cardDeck.topCards(TOP_CARDS_CHOSEN));
            playerState.put(id, P);
            cardDeck = cardDeck.withoutTopCards(TOP_CARDS_CHOSEN);

        }

        CardState cardstate = CardState.of(cardDeck);

        return new GameState(ticketDeck, cardstate, firstPlayer, playerState, null);
    }

    //----------------------------------------------------------------------------------------------------

    private final Deck<Ticket> ticketDeck;
    private final Map<PlayerId, PlayerState> playerState;
    private final CardState cardstate;

    //----------------------------------------------------------------------------------------------------

    /**
     * @param ticketDeck      ticketDeck
     * @param cardState       cardState
     * @param currentPlayerId currentPlayerId
     * @param playerState     playerState
     * @param lastPlayer      lastPlayer
     */

    private GameState(Deck<Ticket> ticketDeck,
                      CardState cardState,
                      PlayerId currentPlayerId,
                      Map<PlayerId, PlayerState> playerState,
                      PlayerId lastPlayer) {

        super(ticketDeck.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);
        this.ticketDeck = ticketDeck;
        this.playerState = playerState;
        this.cardstate = cardState;

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param count count
     * @return the tickets count from the top of the pickaxe
     * @throws IllegalArgumentException if count is not between 0 and the size of the pickaxe (included)
     */

    public SortedBag<Ticket> topTickets(int count) {

        Preconditions.checkArgument((count >= 0) && (count <= ticketsCount()));

        return ticketDeck.topCards(count);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param count count
     * @return an identical state to the receiver, but without the tickets count of the top of the pile
     * @throws IllegalArgumentException if count is not between 0 and the size of the pickaxe (included)
     */

    public GameState withoutTopTickets(int count) {

        Preconditions.checkArgument((count >= 0) && (count <= ticketsCount()));

        return new GameState(ticketDeck.withoutTopCards(count), cardstate, currentPlayerId(), playerState, lastPlayer());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the card to the top of the deck
     * @throws IllegalArgumentException if the pickaxe is empty
     */

    public Card topCard() {

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        return cardstate.topDeckCard();
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return an identical state to the receiver but without the card at the top of the deck
     * @throws IllegalArgumentException if the pickaxe is empty
     */

    public GameState withoutTopCard() {

        Preconditions.checkArgument(!cardstate.isDeckEmpty());

        return new GameState(ticketDeck, cardstate.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param discardedCards discardCards
     * @return an identical state to the receiver but with the given cards added to the discard pile
     */

    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {

        return new GameState(ticketDeck, cardstate.withMoreDiscardedCards(discardedCards),
                currentPlayerId(), playerState, lastPlayer());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param rng random generator
     * @return an identical state to the receiver unless the deck of cards is empty, in which case it is recreated
     * from the discard pile, shuffled using the given random generator
     */

    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {

        if (cardstate.isDeckEmpty()) {

            return new GameState(ticketDeck, cardstate.withDeckRecreatedFromDiscards(rng),
                    currentPlayerId(), playerState, lastPlayer());
        } else {

            return this;
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param playerId      receiver
     * @param chosenTickets tickets chosen
     * @return a identical state to the receiver but in which the given tickets have been added to the given player's hand
     * @throws IllegalArgumentException if the player in question already has at least one ticket
     */

    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {

        Preconditions.checkArgument(playerState(playerId).ticketCount() == 0);

        return new GameState(ticketDeck, cardstate, currentPlayerId(),
                modif(playerId, playerState.get(playerId).withAddedTickets(chosenTickets)), lastPlayer());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param drawnTickets  drawnTickets
     * @param chosenTickets chosenTicket of the player
     * @return a state identical to the receiver, but in which the current player has drawn the tickets drawnTickets
     * from the top of the deck, and chosen to keep those contained in chosenTicket
     * @throws IllegalArgumentException if the set of banknotes kept is not included in that of the banknotes drawn
     */

    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {

        for (Ticket t : chosenTickets) {

            Preconditions.checkArgument(drawnTickets.contains(t));
        }

        return new GameState(ticketDeck.withoutTopCards(drawnTickets.size()),
                cardstate,
                currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedTickets(chosenTickets)),
                lastPlayer());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param slot the given location among the faceUpCard
     * @return an identical state to the receiver except that the face-up card at the given location
     * has been placed in the current player's hand, and replaced by the one at the top of the draw pile
     * @throws IllegalArgumentException if it is not possible to draw cards, i.e. if canDrawCards returns false
     */

    public GameState withDrawnFaceUpCard(int slot) {

        Preconditions.checkArgument(canDrawCards());

        return new GameState(ticketDeck,
                cardstate.withDrawnFaceUpCard(slot),
                currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardstate.faceUpCard(slot))),
                lastPlayer());

    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return an identical state to the receiver except that the top card of the draw pile
     * has been placed in the hand of the current player
     * @throws IllegalArgumentException if it is not possible to draw cards, i.e. if canDrawCards returns false
     */

    public GameState withBlindlyDrawnCard() {

        Preconditions.checkArgument(canDrawCards());

        return new GameState(ticketDeck,
                cardstate.withoutTopDeckCard(),
                currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardstate.topDeckCard())),
                lastPlayer());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route given route seized
     * @param cards given cards
     * @return an identical state to the receiver but in which the current player has seized
     * the given route by means of the given cards
     */

    public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {

        return new GameState(ticketDeck,
                cardstate,
                currentPlayerId(),
                modif(currentPlayerId(), playerState.get(currentPlayerId()).withClaimedRoute(route, cards)),
                lastPlayer())
                .withMoreDiscardedCards(cards);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return true iff the last turn begins, ie if the identity of the last player is currently unknown
     * but the current player has only two cars or less left;
     * this method should only be called at the end of a player's turn
     */

    public boolean lastTurnBegins() {
        return ((lastPlayer() == null) && (playerState.get(currentPlayerId()).carCount() <= MIN_CAR));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return who ends the current player's turn, ie returns an identical state to the receiver except that
     * the current player is the one following the current current player; furthermore,
     * if lastTurnBegins returns true, the current current player becomes the last player.
     */

    public GameState forNextTurn() {

        if (lastTurnBegins()) {

            return new GameState(ticketDeck, cardstate, currentPlayerId().next(), playerState, currentPlayerId());
        } else {

            return new GameState(ticketDeck, cardstate, currentPlayerId().next(), playerState, lastPlayer());
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Can transform a given player ID and his playerState in an EnumMap
     *
     * @param playerId    playerId
     * @param playerState playerState
     * @return Map of <player,playerState>
     */

    private Map<PlayerId, PlayerState> modif(PlayerId playerId, PlayerState playerState) {

        Map<PlayerId, PlayerState> playerState1 = new EnumMap<>(this.playerState);
        playerState1.put(playerId, playerState);

        return playerState1;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param playerId given identity
     * @return complete state of the given identity player, and not just his public part
     */

    @Override
    public PlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the complete state of the current player, and not just its public part
     */

    @Override
    public PlayerState currentPlayerState() {
        return playerState.get(currentPlayerId());
    }

    //----------------------------------------------------------------------------------------------------

}
