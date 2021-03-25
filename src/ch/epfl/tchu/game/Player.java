package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

public interface Player {

    public enum TurnKind{

        DRAW_TICKETS, DRAW_CARDS, CLAIM_ROUT;

        public static final List<TurnKind> ALL = List.of(values());
    }

    //------------------------------------------------------------------------------------------------

    /**
     * tells the player @ownID and @playerNames
     * @param ownId the ID of the player
     * @param playerNames the name of each player
     */
    public abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    //------------------------------------------------------------------------------------------------

    /**
     *tells the player @info
     * @param info any information the player should receive
     */
    public abstract void receiveInfo(String info);

    //------------------------------------------------------------------------------------------------

    /**
     *when the game state changes, tells the player @newState and @ownState
     * @param newState the new state of the game
     * @param ownState the new state of the player
     */
    public abstract void updateState(PublicGameState newState, PlayerState ownState);

    //------------------------------------------------------------------------------------------------

    /**
     * at the begenning of the game, tells the player she/he received @tickets
     * @param tickets the 5 initial tickets distributed to the player
     */
    public abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);

    //------------------------------------------------------------------------------------------------

    /**
     *only called at the begenning of the game
     * @return the tickets the player wants to keep from the ones initially distibuted
     */
    public abstract SortedBag<Ticket> chooseInitialTickets();

    //------------------------------------------------------------------------------------------------

    /**
     * called before every turn of the player,
     * asks the player what kind of turn she/he wants to play next
     * @return the player's answer
     */
    public abstract TurnKind nextTurn();

    //------------------------------------------------------------------------------------------------

    /**
     * callled when the player choose to draw additional tickets (@option),
     * asks the player which ones she/he wants to keep
     * @param options the additional tickets proposed to the player
     * @return the tickets the player wants to keep
     */
    public abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    //------------------------------------------------------------------------------------------------

    /**
     * called when the player decided to draw some locomotive/wagon cards,
     * asks the player where does she/he wants to draw the cards from
     * @return an int 0<=i<=4 if it's from the face up cards,
     * or an int i = Constants.DECK_SLOT if it's from the deck
     */
    public abstract int drawSlot();

    //------------------------------------------------------------------------------------------------

    /**
     * called when the players tries to claim a route
     * @return the route the players tries to claim
     */
    public abstract Route claimedRoute();

    //------------------------------------------------------------------------------------------------

    /**
     * called when the players tries to claim a route
     * @return which initial cards the player wants to take the route with
     */
    public abstract SortedBag<Card> initialClaimCards();

    //------------------------------------------------------------------------------------------------

    /**
     * called when the players tries to claim a tunnel,
     * asks the player which cards form @option she/he wants to keep
     * @param options the additional cards proposed to the player
     * @return the player's choice (if it's empty it means that the player doesn't want to or can't choose any card)
     */
    public abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);


}
