package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;
import java.util.*;

/**
 * Game
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Game {

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param players
     * @param playerNames
     * @param tickets
     * @param rng
     */

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {

        Preconditions.checkArgument((players.size() == 2) && (playerNames.size() == 2));


        Info i1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info i2 = new Info(playerNames.get(PlayerId.PLAYER_2));

        //iterer sur les id ?
        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().initPlayers(entry.getKey(), playerNames);
        }

        GameState myState = GameState.initial(tickets, rng);

        sendInfo(new Info(playerNames.get(myState.currentPlayerId())).willPlayFirst(), players);

       //40 wagons ? et 4 cartes ?

        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().setInitialTicketChoice(myState.topTickets(5));
            myState = myState.withoutTopTickets(5);
        }

        informState(myState, players);

        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            myState = myState.withInitiallyChosenTickets(entry.getKey(), entry.getValue().chooseInitialTickets());
        }

        sendInfo(i1.keptTickets(myState.playerState(PlayerId.PLAYER_1).ticketCount()), players);
        sendInfo(i2.keptTickets(myState.playerState(PlayerId.PLAYER_2).ticketCount()), players);


        boolean endgame = false;

        do {

            Info currentInfo = new Info(playerNames.get(myState.currentPlayerId()));
            sendInfo(currentInfo.canPlay(), players);

            if(myState.lastTurnBegins()){

                sendInfo(currentInfo.lastTurnBegins(myState.playerState(myState.currentPlayerId()).carCount()), players);
                endgame = true;

            }

                Player current = players.get(myState.currentPlayerId());

                informState(myState, players);

                switch (current.nextTurn()) {

                    case DRAW_TICKETS: // besoin d'appeler chooseTickets de Player autrement?
                        sendInfo(currentInfo.drewTickets(3), players);

                        SortedBag<Ticket> chooseTickets = current.chooseTickets(myState.topTickets(3));

                        myState = myState.withChosenAdditionalTickets(myState.topTickets(3),
                                chooseTickets);

                        sendInfo(currentInfo.keptTickets(chooseTickets.size()), players);

                        break;

                    case DRAW_CARDS:
                        int n = 0;
                        do {
                            int drawCount = current.drawSlot();
                            if (0 <= drawCount && drawCount <= 4) {

                                sendInfo(currentInfo.drewVisibleCard(myState.cardState().faceUpCard(drawCount)), players); // comment choper la carte qui prend

                                myState = myState.withDrawnFaceUpCard(drawCount);

                            } else if (drawCount == Constants.DECK_SLOT) {

                                sendInfo(currentInfo.drewBlindCard(), players);

                                myState = myState.withBlindlyDrawnCard();
                            }

                            if(n == 0){

                                informState(myState, players);
                            }

                            n++;
                        } while (n <= 1);

                        break;

                    case CLAIM_ROUTE:
                        Route route = current.claimedRoute();
                        SortedBag<Card> initialClaimCards = current.initialClaimCards();
                        SortedBag.Builder<Card> additional = new SortedBag.Builder<>();


                        if (route.level() == Route.Level.UNDERGROUND) {

                            sendInfo(currentInfo.attemptsTunnelClaim(route, initialClaimCards), players);

                            for (int i = 0; i < 3; ++i) {
                                myState = myState.withCardsDeckRecreatedIfNeeded(rng);
                                additional.add(myState.topCard());
                                myState = myState.withoutTopCard();
                            }
                            SortedBag<Card> c = additional.build();

                            sendInfo(currentInfo.attemptsTunnelClaim(route, initialClaimCards.union(c)), players);

                            int adittionalCardsCount = route.additionalClaimCardsCount(initialClaimCards, c);
                            List<SortedBag<Card>> possibleAdditionalCards = myState.currentPlayerState().possibleAdditionalCards(adittionalCardsCount, initialClaimCards, c);

                            //contains ? ou empty
                            if (route.additionalClaimCardsCount(initialClaimCards, c) >= 1 &&
                                    !possibleAdditionalCards.isEmpty()) {
                                //pas utile si il choisit rien rien se passe
                                if (current.chooseAdditionalCards(possibleAdditionalCards).isEmpty()) {

                                    sendInfo(currentInfo.didNotClaimRoute(route), players);

                                    myState = myState;

                                    break;
                                }
                                else if (!current.chooseAdditionalCards(possibleAdditionalCards).isEmpty()) {

                                    myState = myState.withClaimedRoute(route, current.chooseAdditionalCards(possibleAdditionalCards));

                                    sendInfo(currentInfo.claimedRoute(route, initialClaimCards.union(c)), players);

                                    break;
                                }
                            }
                            if (route.additionalClaimCardsCount(initialClaimCards, c) == 0) {
                                //carte aditiionelles que les joeurs doit jouer ? ou c'est bon?
                                myState = myState.withClaimedRoute(route, initialClaimCards);

                                sendInfo(currentInfo.claimedRoute(route, initialClaimCards.union(c)), players);

                                break;
                            }
                        }
                        if (route.level() == Route.Level.OVERGROUND) {

                            myState = myState.withClaimedRoute(route, initialClaimCards);

                            sendInfo(currentInfo.claimedRoute(route, initialClaimCards), players);

                            break;
                        }
                }

                //normal que ici on call jamais uptdate ?

            if(endgame){

                informState(myState, players);

                Trail p1longest = Trail.longest(myState.playerState(PlayerId.PLAYER_1).routes());
                Trail p2longest = Trail.longest(myState.playerState(PlayerId.PLAYER_2).routes());

                int p1longestSize = p1longest.length();
                int p2longestSize = p2longest.length();

                int p1final = myState.playerState(PlayerId.PLAYER_1).finalPoints();
                int p2final = myState.playerState(PlayerId.PLAYER_2).finalPoints();

                //bonus 10?
                if(p1longestSize > p2longestSize) {
                    sendInfo(i1.getsLongestTrailBonus(p1longest), players);
                    p1final += 10;
                }
                else if (p1longestSize < p2longestSize) {
                    sendInfo(i2.getsLongestTrailBonus(p2longest), players);
                    p2final += 10;
                }
                else if (p1longestSize == p2longestSize){
                    sendInfo(i1.getsLongestTrailBonus(p1longest), players);
                    sendInfo(i2.getsLongestTrailBonus(p2longest), players);
                    p1final += 10;
                    p2final += 10;

                }

                if(p1final > p2final){
                    sendInfo(i1.won(p1final, p2final), players);

                }
                if(p2final > p1final){
                    sendInfo(i2.won(p2final, p1final), players);

                }
                //comment dire que les 2 ont won ?
                if(p2final == p1final){
                    sendInfo(i2.won(p2final, p1final), players);
                    sendInfo(i1.won(p1final, p2final), players);
                }

            }

            else if(!endgame){

                myState = myState.forNextTurn();
            }
            // comment gerer la fin
            //les points ? et bonus ?
            // 1 tour == les 2 joueurs qui jouent alors que la 1 tour c'est juste un joueur
        } while (!endgame);

        //lastTurnBeggins() && currentPlayer.equals(lastPlayer)
        //!myState.currentPlayerId().equals(myState.lastPlayer())

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param info
     * @param players
     */

    private static void sendInfo(String info, Map<PlayerId, Player> players) {
        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().receiveInfo(info);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param newState
     * @param players
     */

    private static void informState(GameState newState, Map<PlayerId, Player> players) {
        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().updateState(newState, newState.playerState(entry.getKey()));
        }
    }

    //----------------------------------------------------------------------------------------------------
}
