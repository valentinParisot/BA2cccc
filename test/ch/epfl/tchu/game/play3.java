package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.*;

class GameTest1 {




        @Test
        public void playTest(){
            Random rng = TestRandomizer.newRandom();
            Map<PlayerId, Player> Players = new EnumMap<>(PlayerId.class);
            Map<PlayerId, String> playerNames = new EnumMap<>(PlayerId.class);
            Players.put(PlayerId.PLAYER_1, new TestPlayer(2021, ChMap.routes()));
            Players.put(PlayerId.PLAYER_2, new TestPlayer(2021, ChMap.routes()));
            playerNames.put(PlayerId.PLAYER_1, "Elyes");
            playerNames.put(PlayerId.PLAYER_2, "Rassan");
            Game.play(Players, playerNames, SortedBag.of(ChMap.tickets()), rng);
        }
        private static final class TestPlayer implements Player {
            private static final int TURN_LIMIT = 1000;

            private final Random rng;
            // Toutes les routes de la carte
            private final List<Route> allRoutes;

            private int turnCounter;
            private PlayerState ownState;
            private PublicGameState gameState;
            private PlayerId ownId;
            private Map<PlayerId, String> playerNames;
            // Lorsque nextTurn retourne CLAIM_ROUTE
            private Route routeToClaim;
            private SortedBag<Card> initialClaimCards;
            private List<Ticket> ticketChoice = new ArrayList<>();

            public TestPlayer(long randomSeed, List<Route> allRoutes) {
                this.rng = new Random(randomSeed);
                this.allRoutes = List.copyOf(allRoutes);
                this.turnCounter = 0;
            }

            @Override
            public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
                this.ownId = ownId;
                this.playerNames = new EnumMap<PlayerId, String>(playerNames);
                receiveInfo("Vous êtes " + ownId.toString() + " et vous jouez face à " +
                        ownId.next().toString());
            }

            @Override
            public void receiveInfo(String info) {
                System.out.println(info);
            }

            @Override
            public void updateState(PublicGameState newState, PlayerState ownState) {
                this.gameState = newState;
                this.ownState = ownState;
                if (this.gameState.currentPlayerId() == PlayerId.PLAYER_1) {
                    System.out.println(playerNames.get(ownId) + ": ");
                    receiveInfo("Cartes faces ouvertes: " + this.gameState.cardState().faceUpCards());
                    receiveInfo("Vos cartes: " + this.ownState.cards());
                    receiveInfo("Nombre de wagons: " + this.ownState.carCount());
                    receiveInfo("Vos routes: " + this.ownState.routes());
                    receiveInfo("Vos tickets: " + this.ownState.tickets());
                    receiveInfo("Points: " + this.ownState.finalPoints());
                    System.out.println();
                    receiveInfo("C'est le tour de " + playerNames.get(this.gameState.currentPlayerId()));
                }
            }

            @Override
            public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
                this.ticketChoice = tickets.toList();
            }

            @Override
            public SortedBag<Ticket> chooseInitialTickets() {
                List<Ticket> tickets = new ArrayList<>();
                int nbr = rng.nextInt(3) + 3;
                for (int i = 0; i < nbr; ++i) {
                    tickets.add(ticketChoice.remove(rng.nextInt(ticketChoice.size())));
                }
                return SortedBag.of(tickets);
            }

            @Override
            public TurnKind nextTurn() {
                turnCounter += 1;
                if (turnCounter > TURN_LIMIT)
                    throw new Error("Trop de tours joués !");

                List<Route> claimableRoutes = new ArrayList<>();
                // Détermine les routes dont ce joueur peut s'emparer
                allRoutes.forEach(r -> {
                    if(ownState.canClaimRoute(r)){
                        claimableRoutes.add(r);
                    }
                });
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
                List<Ticket> tickets = new ArrayList<>();
                int nbr = rng.nextInt(2) + 1;
                for (int i = 0; i < nbr; ++i) {
                    tickets.add(options.toList().remove(rng.nextInt(ticketChoice.size())));
                }
                return SortedBag.of(tickets);
            }

            @Override
            public int drawSlot() {
                return rng.nextInt(6)%6-1;
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

                //return options.get(rng.nextInt(options.size()));
                if(options.isEmpty()){
                    return SortedBag.of();
                }else
                return options.get(rng.nextInt(options.size()));
            }

            @Override
            public void playMenu(){

            }

            @Override
            public void startGame() {

            }
        }


}
