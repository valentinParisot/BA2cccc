package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * PublicGameState
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class PublicGameState {

    //----------------------------------------------------------------------------------------------------

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;
    private final static int MIN_DECK_DISCARD = 5;
    private final static int EXACT_NUMBER_OF_KEY = 2;

    //----------------------------------------------------------------------------------------------------

    /**
     * constructs the public part of the state of a game of tCHu
     *
     * @param ticketsCount    the bank of tickets has a size of ticketsCount
     * @param cardState       the public state of the wagon / locomotive cards is cardState
     * @param currentPlayerId the current player is currentPlayerId
     * @param playerState     the public state of the players is contained in playerState
     * @param lastPlayer      the identity of the last player is lastPlayer
     * @throws IllegalArgumentException if the size of the draw pile is strictly negative or if playerState
     *                                  does not contain exactly two key / value pairs
     * @throws NullPointerException     if one of the other arguments (except lastPlayer) Is null
     */

    public PublicGameState(int ticketsCount,
                           PublicCardState cardState,
                           PlayerId currentPlayerId,
                           Map<PlayerId, PublicPlayerState> playerState,
                           PlayerId lastPlayer) {

        Preconditions.checkArgument((ticketsCount >= 0) && (playerState.size() == EXACT_NUMBER_OF_KEY));

        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = playerState;
        this.lastPlayer = lastPlayer;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the size of the bank of banknotes
     */

    public int ticketsCount() {
        return ticketsCount;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return true if it is possible to draw banknotes, i.e. if the draw pile is not empty
     */

    public boolean canDrawTickets() {
        return (!(ticketsCount() == 0));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the public part of the state of the wagon / locomotive cards
     */

    public PublicCardState cardState() {
        return cardState;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return true iff it is possible to draw cards, i.e. if the draw pile and the discard pile contain
     * at least 5 cards between them
     */

    public boolean canDrawCards() {
        return ((cardState.deckSize() + cardState.discardsSize() >= MIN_DECK_DISCARD ));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the identity of the current player
     */

    public PlayerId currentPlayerId() {
        return currentPlayerId;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param playerId given identity
     * @return the public part of the player's state of given identity
     */

    public PublicPlayerState playerState(PlayerId playerId) {
        return playerState.get(playerId);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the public part of the current player's state
     */

    public PublicPlayerState currentPlayerState() {
        return playerState.get(currentPlayerId);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the totality of the roads which one or the other of the players seized
     */

    public List<Route> claimedRoutes() {

        List<Route> claimedRoutes = new ArrayList<>();

        for (PublicPlayerState p : playerState.values()) {

            claimedRoutes.addAll(p.routes());

        }

        return claimedRoutes;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return the identity of the last player, or null if it is not yet known because the last round has not started
     */

    public PlayerId lastPlayer() {
        return lastPlayer;
    }

    //----------------------------------------------------------------------------------------------------

}
