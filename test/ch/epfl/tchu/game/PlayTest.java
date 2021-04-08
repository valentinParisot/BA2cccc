package ch.epfl.tchu.game;
import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.*;

public class PlayTest {

    PlayTest.TestPlayer testPlayer1 = new TestPlayer(10, ChMap.routes(),PlayerId.PLAYER_2);
    PlayTest.TestPlayer testPlayer2 = new TestPlayer(10, ChMap.routes(),PlayerId.PLAYER_1);

    Map<PlayerId, Player> players = Map.of(PlayerId.PLAYER_1, testPlayer1, PlayerId.PLAYER_2, testPlayer2);
    Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, "valentin", PlayerId.PLAYER_2, "hugo");
    SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
    Random rng = new Random();


    @Test

    void game(){
        Game.play(players, playerNames, tickets, rng);
    }


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
        private SortedBag<Ticket> tickets ;
        private final int id;


        public TestPlayer(long randomSeed, List<Route> allRoutes,PlayerId id) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);

            this.turnCounter = 0;
            this.id = (id == PlayerId.PLAYER_1)?
                    1:
                    2;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

            System.out.println("initplayer activer");
            //compter que ca compte une fois

        }

        @Override
        public void receiveInfo(String info) {

            System.out.print(info);

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
            final Station BAL = new Station(1, "Bâle");
            final Station BER = new Station(3, "Berne");
            final Station BRI = new Station(4, "Brigue");
            final Station STG = new Station(27, "Saint-Gall");

            SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
            builder.add(new Ticket(BAL, BER, 5));
            builder.add(new Ticket(BAL, BRI, 10));
            builder.add(new Ticket(BAL, STG, 8));
            SortedBag<Ticket> initialTickets = builder.build();
            return initialTickets;
        }


        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer

            List<Route> allRoutesOfGame = allRoutes;
            List<Route> claimableRoutes = new ArrayList<>();
            for(int i=0;i<allRoutes.size();i++){
                if(ownState.canClaimRoute(allRoutesOfGame.get(i))){
                    claimableRoutes.add(allRoutes.get(i));
                }
            }

            if (claimableRoutes.isEmpty() && gameState.canDrawCards()) {

                return TurnKind.DRAW_CARDS;

            } else {
                int routeIndex = Math.abs(rng.nextInt(claimableRoutes.size()));
                System.out.println("RNG FAUX " + routeIndex);
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
        }




        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {

           /** Random random = new Random();
            int number = random.nextInt(6);
            int r = -1 + number;
            if(r <2) {
                final Station BER = new Station(3, "Berne");
                final Station COI = new Station(6, "Coire");
                return SortedBag.of(new Ticket(BER, COI, 10));
            }
            else if (r<5 && r> 1){
                final Station BAL = new Station(1, "Bâle");
                final Station BEL = new Station(2, "Bellinzone");
                return SortedBag.of(new Ticket(BAL, BEL, 10));

            }else {
                final Station KRE = new Station(12, "Kreuzlingen");
                final Station LAU = new Station(13, "Lausanne");
                return SortedBag.of(new Ticket(KRE, LAU, 10));
            }
            **/

            SortedBag.Builder bagBuilder = new SortedBag.Builder();
            int randomAmountOfTickets = rng.nextInt(Constants.IN_GAME_TICKETS_COUNT)+1;
            for (int i = 0; i < randomAmountOfTickets; i++){
                bagBuilder.add(options.get(i));
            }
            return bagBuilder.build();


        }

        @Override
        public int drawSlot() {
            Random random = new Random();
            int number = random.nextInt(6);
            return -1 + number;
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

                SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
                for (SortedBag<Card> option : options) {
                    builder.add(option);
                }
                return builder.build();
            }
        }

        private static final List<Card> ALL_CARDS = List.of(Card.values());
        private SortedBag<Card> allCards() {
            var cardsBuilder = new SortedBag.Builder<Card>();
            cardsBuilder.add(14, Card.LOCOMOTIVE);
            for (Card card : Card.CARS)
                cardsBuilder.add(12, card);
            var cards = cardsBuilder.build();
            return cards;
        }
        SortedBag<Card> cards = allCards();


    }




