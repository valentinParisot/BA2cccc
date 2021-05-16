package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * Serdes
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Serdes {

    //----------------------------------------------------------------------------------------------------

    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";
    private static final String DOUBLE_POINT = ":";

    //----------------------------------------------------------------------------------------------------

    public static final Serde<Integer> INT_SERDE = Serde.of(
            i -> Integer.toString(i),
            Integer::parseInt);

    //----------------------------------------------------------------------------------------------------

    public static final Serde<String> STRING_SERDE = Serde.of(
            s -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            s -> new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8));

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PlayerId> PLAYER_ID_SERDE = Serde.oneOf(PlayerId.ALL);
    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);
    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);
    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());
    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());

    //----------------------------------------------------------------------------------------------------

    public static final Serde<List<String>> LIST_OF_STRING_SERDE = Serde.listOf(STRING_SERDE, COMMA);
    public static final Serde<List<Card>> LIST_OF_CARD_SERDE = Serde.listOf(CARD_SERDE, COMMA);
    public static final Serde<List<Route>> LIST_OF_ROUTE_SERDE = Serde.listOf(ROUTE_SERDE, COMMA);
    public static final Serde<SortedBag<Card>> BAG_OF_CARD_SERDE = Serde.bagOf(CARD_SERDE, COMMA);
    public static final Serde<SortedBag<Ticket>> BAG_OF_TICKET_SERDE = Serde.bagOf(TICKET_SERDE, COMMA);
    public static final Serde<List<SortedBag<Card>>> LIST_OF_SORTED_BAG_CARD_SERDE = Serde.listOf(BAG_OF_CARD_SERDE, SEMICOLON);

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PublicCardState> PUBLIC_CARD_STATE_SERDE = Serde.of(
            publicCardState -> {

                String s;
                int deckSize;
                int discardsSize;
                List<Card> card;

                card = publicCardState.faceUpCards();
                deckSize = publicCardState.deckSize();
                discardsSize = publicCardState.discardsSize();


                return LIST_OF_CARD_SERDE.serialize(card)
                        + SEMICOLON + INT_SERDE.serialize(deckSize)
                        + SEMICOLON + INT_SERDE.serialize(discardsSize);
            }
            , s -> {
                String[] tab = s.split(Pattern.quote(SEMICOLON), -1);

                return new PublicCardState(LIST_OF_CARD_SERDE.deSerialize(tab[0]),
                        INT_SERDE.deSerialize(tab[1]),
                        INT_SERDE.deSerialize(tab[2]));

            }
    );

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE_SERDE = Serde.of(
            publicPlayerState -> {
                List<Route> routes;
                int ticketCount;
                int cardCount;

                routes = publicPlayerState.routes();
                ticketCount = publicPlayerState.ticketCount();
                cardCount = publicPlayerState.cardCount();

                return INT_SERDE.serialize(ticketCount)
                        + SEMICOLON + INT_SERDE.serialize(cardCount)
                        + SEMICOLON + LIST_OF_ROUTE_SERDE.serialize(routes);
            }
            , s -> {
                String[] tab = s.split(Pattern.quote(SEMICOLON), -1);

                return new PublicPlayerState(INT_SERDE.deSerialize(tab[0]),
                        INT_SERDE.deSerialize(tab[1]),
                        LIST_OF_ROUTE_SERDE.deSerialize(tab[2]));
            }
    );

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PlayerState> PLAYER_STATE_SERDE = Serde.of(
            playerState -> {
                SortedBag<Ticket> ticket;
                SortedBag<Card> card;
                List<Route> route;

                ticket = playerState.tickets();
                card = playerState.cards();
                route = playerState.routes();

                return BAG_OF_TICKET_SERDE.serialize(ticket)
                        + SEMICOLON + BAG_OF_CARD_SERDE.serialize(card)
                        +SEMICOLON + LIST_OF_ROUTE_SERDE.serialize(route);
            }
            , s -> {
                String[] tab = s.split(Pattern.quote(SEMICOLON), -1);

                return new PlayerState(BAG_OF_TICKET_SERDE.deSerialize(tab[0]),
                        BAG_OF_CARD_SERDE.deSerialize(tab[1]),
                        LIST_OF_ROUTE_SERDE.deSerialize(tab[2]));
            }
    );
    //----------------------------------------------------------------------------------------------------

    public static final Serde<PublicGameState> PUBLIC_GAME_STATE_SERDE = Serde.of(
            publicGameState -> {
                int ticketCount;
                PublicCardState publicCardState;
                PlayerId currentPlayerId;
                PublicPlayerState playerStateUn;
                PublicPlayerState playerStateDeux;
                PlayerId lastPlayer;


                ticketCount = publicGameState.ticketsCount();
                publicCardState = publicGameState.cardState();
                currentPlayerId = publicGameState.currentPlayerId();
                playerStateUn = publicGameState.playerState(PLAYER_1);
                playerStateDeux = publicGameState.playerState(PLAYER_2);
                String s = null;

                lastPlayer = publicGameState.lastPlayer();

                //gerer la comment on fait
                if((publicGameState.lastPlayer() == null)) {
                    return INT_SERDE.serialize(ticketCount)
                            + DOUBLE_POINT + PUBLIC_CARD_STATE_SERDE.serialize(publicCardState)
                            + DOUBLE_POINT + PLAYER_ID_SERDE.serialize(currentPlayerId)
                            + DOUBLE_POINT + PUBLIC_PLAYER_STATE_SERDE.serialize(playerStateUn)
                            + DOUBLE_POINT + PUBLIC_PLAYER_STATE_SERDE.serialize(playerStateDeux)
                            + DOUBLE_POINT + LIST_OF_STRING_SERDE.serialize(List.of());
                }else
                    return INT_SERDE.serialize(ticketCount)
                            + DOUBLE_POINT + PUBLIC_CARD_STATE_SERDE.serialize(publicCardState)
                            + DOUBLE_POINT + PLAYER_ID_SERDE.serialize(currentPlayerId)
                            + DOUBLE_POINT + PUBLIC_PLAYER_STATE_SERDE.serialize(playerStateUn)
                            + DOUBLE_POINT + PUBLIC_PLAYER_STATE_SERDE.serialize(playerStateDeux)
                            + DOUBLE_POINT + PLAYER_ID_SERDE.serialize(lastPlayer);
            }
            , s -> {
                String[] tab = s.split(Pattern.quote(DOUBLE_POINT), -1);

                Map<PlayerId, PublicPlayerState> playerStateMap = new HashMap<>();


                int i = 3;
                for (PlayerId id : PlayerId.ALL) {

                    playerStateMap.put(id,
                            PUBLIC_PLAYER_STATE_SERDE.deSerialize(tab[i]));
                    //deser les 2 ds des vsar et add ds une map avec player 1 et 2
                    i++;
                }

                if(tab[5].equals("")){
                    return new PublicGameState(INT_SERDE.deSerialize(tab[0]),
                            PUBLIC_CARD_STATE_SERDE.deSerialize(tab[1]),
                            PLAYER_ID_SERDE.deSerialize(tab[2]),// PLAYER_ID_SERDE.deSerialize(tab[2]),
                            playerStateMap,
                            null);
                }
                else {

                    return new PublicGameState(INT_SERDE.deSerialize(tab[0]),
                            PUBLIC_CARD_STATE_SERDE.deSerialize(tab[1]),
                            PLAYER_ID_SERDE.deSerialize(tab[2]),
                            playerStateMap,
                            PLAYER_ID_SERDE.deSerialize(tab[5]));
                }
            }
    );
    //----------------------------------------------------------------------------------------------------
}
