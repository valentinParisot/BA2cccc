package ch.epfl.tchu.game;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Route.Level;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class PlayeTest {

    /**SortedBag<Ticket> allTickets = SortedBag.of( new TestPlayer.ChMap().ALL_TICKETS);
    List<Route> claimableRoutes = new ArrayList<>(new TestPlayer.ChMap().ALL_ROUTES);
    Random rng = TestRandomizer.newRandom();
    List<Route> allRoutes = new ArrayList<>(new TestPlayer.ChMap().ALL_ROUTES);
    Map<PlayerId, Player> players;
    Map<PlayerId, String> playersnames;**/





    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        private final List<Route> allRoutes;
        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;
        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        }

        @Override
        public void receiveInfo(String info) {

        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            return null;
        }


        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer

            List<Route> claimableRoutes = ChMap.routes();

            if (claimableRoutes.isEmpty()) {
                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
        }


        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            return null;
        }

        @Override
        public int drawSlot() {
            return 0;
        }

        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            return initialClaimCards;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            return null;
        }


    }


    PlayeTest.TestPlayer testPlayer1 = new TestPlayer(10, ChMap.routes());
    PlayeTest.TestPlayer testPlayer2 = new TestPlayer(10, ChMap.routes());
    Map<PlayerId, Player> playerss = Map.of(PlayerId.PLAYER_1, testPlayer1, PlayerId.PLAYER_2, testPlayer2);
    Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, "Gilles", PlayerId.PLAYER_2, "Hadrien");
    SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
    Random rngg = new Random();


    @Test

    void gamesworks(){
        Game.play(playerss, playerNames, tickets, rngg);
    }



}