package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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

    public int ticketsCount() {
        return ticketsCount;
    }

    public boolean canDrawTickets(){
        return (!(cardState.isDeckEmpty()));
    }

    public PublicCardState cardState() {
        return cardState;
    }

    public boolean canDrawCards(){

        return ((cardState.deckSize() + cardState.discardsSize() >= 5));

    }

    public PlayerId currentPlayerId(){
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

        for(PublicPlayerState p  : playerState.values()) {

           claimedRoutes.addAll(p.routes());

        }
        return claimedRoutes;

    }

    public PlayerId lastPlayer(){

       return lastPlayer;
    }




}
