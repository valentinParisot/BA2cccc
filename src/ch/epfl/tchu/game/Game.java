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
     * @param players
     * @param playerNames
     * @param tickets
     * @param rng
     */

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {

        Preconditions.checkArgument((players.size() == 2) && (playerNames.size() == 2));

        GameState gameState = GameState.initial(tickets, rng);

        Info i1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info i2 = new Info(playerNames.get(PlayerId.PLAYER_2));


        List<String> nameList = new ArrayList<>();
        for (Map.Entry<PlayerId, String> entry : playerNames.entrySet()) {
            nameList.add(entry.getValue());
        }

        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().initPlayers(entry.getKey(), playerNames);
        }

        sendInfo(new Info(playerNames.get(gameState.currentPlayerId())).willPlayFirst(), players);


        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().setInitialTicketChoice(gameState.topTickets(5));
            gameState = gameState.withoutTopTickets(5);
        }

        updateState(gameState, players);

        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            gameState = gameState.withInitiallyChosenTickets(entry.getKey(), entry.getValue().chooseInitialTickets());
        }

        sendInfo(i1.keptTickets(gameState.playerState(PlayerId.PLAYER_1).ticketCount()), players);
        sendInfo(i2.keptTickets(gameState.playerState(PlayerId.PLAYER_2).ticketCount()), players);

        int end = 0;
        boolean endwhile = false;


        do {

            Info currentInfo = new Info(playerNames.get(gameState.currentPlayerId()));

            sendInfo(currentInfo.canPlay(), players);

            Player current = players.get(gameState.currentPlayerId());

            updateState(gameState, players);

            switch (current.nextTurn()) {

                case DRAW_TICKETS:
                    sendInfo(currentInfo.drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);

                    SortedBag<Ticket> chooseTickets = current.chooseTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT));

                    gameState = gameState.withChosenAdditionalTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT), chooseTickets);

                    sendInfo(currentInfo.keptTickets(chooseTickets.size()), players);

                    break;

                case DRAW_CARDS:

                    int n = 0;


                    do {

                        if (n == 1) {
                            updateState(gameState, players);
                        }

                        int drawCount = current.drawSlot();
                        if (0 <= drawCount && drawCount <= 4) {

                            sendInfo(currentInfo.drewVisibleCard(gameState.cardState().faceUpCard(drawCount)), players);

                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            gameState = gameState.withDrawnFaceUpCard(drawCount);
                            updateState(gameState, players);

                        } else if (drawCount == Constants.DECK_SLOT) {

                            sendInfo(currentInfo.drewBlindCard(), players);
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            gameState = gameState.withBlindlyDrawnCard();

                        }

                        if (n == 0) {

                            updateState(gameState, players);
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

                        for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i) {
                            gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                            additional.add(gameState.topCard());
                            gameState = gameState.withoutTopCard();
                        }
                        SortedBag<Card> drawnCards = additional.build();

                        int addCardsCount = route.additionalClaimCardsCount(initialClaimCards, drawnCards);

                        sendInfo(currentInfo.drewAdditionalCards(drawnCards, addCardsCount), players);

                        if (addCardsCount >= 1 && !gameState
                                .currentPlayerState()
                                .possibleAdditionalCards(addCardsCount, initialClaimCards, drawnCards).isEmpty()) {

                            List<SortedBag<Card>> possibleAdditionalCards = gameState
                                    .currentPlayerState()
                                    .possibleAdditionalCards(addCardsCount, initialClaimCards, drawnCards);

                            SortedBag<Card> selectedAddCards = current.chooseAdditionalCards(possibleAdditionalCards);

                            if (selectedAddCards.isEmpty()) {

                                sendInfo(currentInfo.didNotClaimRoute(route), players);
                                gameState = gameState.withMoreDiscardedCards(drawnCards.union(initialClaimCards));
                                gameState = gameState.withMoreDiscardedCards(drawnCards.union(drawnCards));
                                break;

                            } else {

                                initialClaimCards = initialClaimCards.union(selectedAddCards);
                                gameState = gameState.withMoreDiscardedCards(drawnCards.difference(selectedAddCards));

                            }
                        } else {

                            gameState = gameState.withMoreDiscardedCards(initialClaimCards);

                        }

                    }
                    sendInfo(currentInfo.claimedRoute(route, initialClaimCards), players);

                    gameState = gameState.withClaimedRoute(route, initialClaimCards);

                    break;

                default:
                    break;
            }


            if (gameState.lastTurnBegins()) {

                sendInfo(currentInfo.lastTurnBegins(gameState.playerState(gameState.currentPlayerId()).carCount()), players);

               for (int u = 0; u < 2; ++u) {

                    Info currentInfo2 = new Info(playerNames.get(gameState.currentPlayerId()));

                    sendInfo(currentInfo2.canPlay(), players);


                    Player current2 = players.get(gameState.currentPlayerId());

                    updateState(gameState, players);

                    switch (current2.nextTurn()) {

                        case DRAW_TICKETS:
                            sendInfo(currentInfo2.drewTickets(Constants.IN_GAME_TICKETS_COUNT), players);

                            SortedBag<Ticket> chooseTickets = current2.chooseTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT));

                            gameState = gameState.withChosenAdditionalTickets(gameState.topTickets(Constants.IN_GAME_TICKETS_COUNT), chooseTickets);

                            sendInfo(currentInfo2.keptTickets(chooseTickets.size()), players);

                            break;

                        case DRAW_CARDS:

                            int n = 0;

                            do {

                                if (n == 1) {
                                    updateState(gameState, players);
                                }

                                int drawCount = current2.drawSlot();
                                if (0 <= drawCount && drawCount <= 4) {

                                    sendInfo(currentInfo2.drewVisibleCard(gameState.cardState().faceUpCard(drawCount)), players);

                                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);

                                    gameState = gameState.withDrawnFaceUpCard(drawCount);

                                    updateState(gameState, players);

                                } else if (drawCount == Constants.DECK_SLOT) {

                                    sendInfo(currentInfo2.drewBlindCard(), players);
                                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                    gameState = gameState.withBlindlyDrawnCard();

                                }

                                if (n == 0) {

                                    updateState(gameState, players);
                                }

                                n++;
                            } while (n <= 1);


                            break;

                        case CLAIM_ROUTE:
                            Route route = current2.claimedRoute();
                            SortedBag<Card> initialClaimCards = current2.initialClaimCards();
                            SortedBag.Builder<Card> additional = new SortedBag.Builder<>();

                            if (route.level() == Route.Level.UNDERGROUND) {

                                sendInfo(currentInfo2.attemptsTunnelClaim(route, initialClaimCards), players);

                                for (int i = 0; i < Constants.ADDITIONAL_TUNNEL_CARDS; ++i) {
                                    gameState = gameState.withCardsDeckRecreatedIfNeeded(rng);
                                    additional.add(gameState.topCard());
                                    gameState = gameState.withoutTopCard();
                                }
                                SortedBag<Card> drawnCards = additional.build();

                                int addCardsCount = route.additionalClaimCardsCount(initialClaimCards, drawnCards);

                                sendInfo(currentInfo2.drewAdditionalCards(drawnCards, addCardsCount), players);

                                if (addCardsCount >= 1 && !(gameState.currentPlayerState().canClaimRoute(route))) {

                                    List<SortedBag<Card>> possibleAdditionalCards = gameState
                                            .currentPlayerState()
                                            .possibleAdditionalCards(addCardsCount, initialClaimCards, drawnCards);

                                    SortedBag<Card> selectedAddCards = current2.chooseAdditionalCards(possibleAdditionalCards);

                                    if (selectedAddCards.isEmpty()) {

                                        sendInfo(currentInfo2.didNotClaimRoute(route), players);
                                        gameState = gameState.withMoreDiscardedCards(drawnCards.union(initialClaimCards));
                                        gameState = gameState.withMoreDiscardedCards(drawnCards.union(drawnCards));
                                        break;

                                    } else {

                                        initialClaimCards = initialClaimCards.union(selectedAddCards);
                                        gameState = gameState.withMoreDiscardedCards(drawnCards.difference(selectedAddCards));

                                    }
                                } else {

                                    gameState = gameState.withMoreDiscardedCards(initialClaimCards);

                                }

                            }
                            sendInfo(currentInfo2.claimedRoute(route, initialClaimCards), players);
                            gameState = gameState.withClaimedRoute(route, initialClaimCards);

                            break;

                        default:
                            break;
                    }

                    gameState = gameState.forNextTurn();

                }

            }


            else {
                gameState = gameState.forNextTurn();
            }


        } while (!gameState.currentPlayerId().equals(gameState.lastPlayer()));


        Trail p1longest = Trail.longest(gameState.playerState(PlayerId.PLAYER_1).routes());
        Trail p2longest = Trail.longest(gameState.playerState(PlayerId.PLAYER_2).routes());

        int p1longestSize = p1longest.length();
        int p2longestSize = p2longest.length();

        int p1final = gameState.playerState(PlayerId.PLAYER_1).finalPoints();
        int p2final = gameState.playerState(PlayerId.PLAYER_2).finalPoints();

        updateState(gameState, players);

        if (p1longestSize > p2longestSize) {
            sendInfo(i1.getsLongestTrailBonus(p1longest), players);
            p1final += Constants.LONGEST_TRAIL_BONUS_POINTS;
        } else if (p1longestSize < p2longestSize) {
            sendInfo(i2.getsLongestTrailBonus(p2longest), players);
            p2final += Constants.LONGEST_TRAIL_BONUS_POINTS;
        } else if (p1longestSize == p2longestSize) {
            sendInfo(i1.getsLongestTrailBonus(p1longest), players);
            sendInfo(i2.getsLongestTrailBonus(p2longest), players);
            p1final += Constants.LONGEST_TRAIL_BONUS_POINTS;
            p2final += Constants.LONGEST_TRAIL_BONUS_POINTS;

        }

        if (p1final > p2final) {
            sendInfo(i1.won(p1final, p2final), players);

        }
        if (p2final > p1final) {
            sendInfo(i2.won(p2final, p1final), players);

        }

        if (p2final == p1final) {
            sendInfo(i1.draw(nameList, p1final), players);
        }

    }

    //----------------------------------------------------------------------------------------------------

    /**
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
     * @param newState
     * @param players
     */

    private static void updateState(GameState newState, Map<PlayerId, Player> players) {

        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().updateState(newState, newState.playerState(entry.getKey()));
        }

    }

    //----------------------------------------------------------------------------------------------------


}
