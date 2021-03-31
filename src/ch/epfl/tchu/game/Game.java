package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.gui.Info;

import java.util.*;

public final class Game {

    public static void play(Map<PlayerId, Player> players, Map<PlayerId, String> playerNames, SortedBag<Ticket> tickets, Random rng) {

        Preconditions.checkArgument((players.size() == 2) && (playerNames.size() == 2));

        GameState myState = GameState.initial(tickets, rng);
        Info i1 = new Info(playerNames.get(PlayerId.PLAYER_1));
        Info i2 = new Info(playerNames.get(PlayerId.PLAYER_2));

        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().initPlayers(entry.getKey(), playerNames);
        }

        sendInfo(new Info(playerNames.get(myState.currentPlayerId())).willPlayFirst(), players);

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

        Player current = players.get(myState.currentPlayerId());

        switch (current.nextTurn()) {
            case DRAW_TICKETS: // besoin d'appeler chooseTickets de Player autrement?
                myState = myState.withChosenAdditionalTickets(myState.topTickets(3),
                        current.chooseTickets(myState.topTickets(3)));
                break;

            case DRAW_CARDS:
                int n = 0;
                do {
                    int d1 = current.drawSlot();
                    if (0 <= d1 && d1 <= 4) {
                        myState = myState.withDrawnFaceUpCard(d1);
                    } else if (d1 == Constants.DECK_SLOT) {
                        myState = myState.withBlindlyDrawnCard();
                    }
                    n++;
                } while (n <= 1);
                break;

            case CLAIM_ROUT:
                Route route = current.claimedRoute();
                SortedBag<Card> initialClaimCards = current.initialClaimCards();
                SortedBag.Builder<Card> additional = new SortedBag.Builder<>();


                if (route.level() == Route.Level.UNDERGROUND) {
                    for (int i = 0; i < 3; ++i) {
                        additional.add(myState.topCard());
                        myState = myState.withoutTopCard();
                    }

                    SortedBag<Card> c = additional.build();

                    // 3 cas quand c'est un tunnel
                    // a)tu pioches des cartes et tu as des cartes additionelles et tu peux t'emparr de la routw
                    // b) pareil mais peut pas t'emparer de la route
                    // c) pareil mais tu as pas de cartes additionelle et ut peuxc t'emprwer de la route

                    if(route.additionalClaimCardsCount(initialClaimCards, additional.build()) >= 1 && !current.chooseAdditionalCards(List.of(c)).isEmpty()){

                       myState = myState.

                    }


                    if(){

                    }
                    if(route.additionalClaimCardsCount(initialClaimCards, additional.build()) >= 1 && current.chooseAdditionalCards(List.of(c)).isEmpty()){


                    }

                    }

                if (route.level() == Route.Level.OVERGROUND){

                    myState = myState.withClaimedRoute(route,initialClaimCards);
                }



                // route tu t'empares
                //overgroudn acutalise gamestate en disant que il prend la route

                }

        }

    }


    private static void sendInfo(String info, Map<PlayerId, Player> players) {
        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().receiveInfo(info);
        }
    }

    private static void informState(GameState newState, Map<PlayerId, Player> players) {
        for (Map.Entry<PlayerId, Player> entry : players.entrySet()) {
            entry.getValue().updateState(newState, newState.playerState(entry.getKey()));
        }
    }
}
