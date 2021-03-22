package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ch.epfl.tchu.game.Constants.INITIAL_TICKETS_COUNT;

public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;


    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PublicPlayerState> playerState, PlayerId lastPlayer) {

        Preconditions.checkArgument((ticketsCount >= 0) && (playerState.size() == 2));
        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = playerState;
        this.lastPlayer = lastPlayer;
    }

    public int TicketsCount() {
        return ticketsCount;
    }

    public boolean canDrawTickets(){
        return (!(cardState.isDeckEmpty()));
    }

    public PublicCardState CardState() {
        return cardState;
    }

    public boolean canDrawnCards(){
        return ((cardState.discardsSize() + cardState.deckSize() >= INITIAL_TICKETS_COUNT));
    }

    public PlayerId CurrentPlayerId(){
        return currentPlayerId;
    }

    public PublicPlayerState playerState(PlayerId playerId){

        return playerState.get(playerId);
    }

    public PublicPlayerState currentPlayerState(){
         return playerState.get(currentPlayerId);
    }

    public List<Route> claimedRoutes(){

        List<Route> claimedRoutes = new ArrayList<>();

        playerState(lastPlayer).routes();
        playerState(currentPlayerId).routes();

        claimedRoutes.addAll(playerState(lastPlayer).routes());
        claimedRoutes.addAll(playerState(currentPlayerId).routes());

        return claimedRoutes;

    }

    public PlayerId lastPlayer(){

        if(lastPlayer == null) {
            return null;
        }
        else {
            return lastPlayer;
        }
    }




}
