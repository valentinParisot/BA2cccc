package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

                s = String.join(publicCardState.faceUpCards().toString(), COMMA);
                deckSize = publicCardState.deckSize();
                discardsSize = publicCardState.discardsSize();


                return STRING_SERDE.serialize(s)
                        + COMMA + INT_SERDE.serialize(deckSize)
                        + COMMA + INT_SERDE.serialize(discardsSize);
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
                String s;
                int ticketCount;
                int cardCount;

                s = String.join(publicPlayerState.routes().toString(), COMMA);
                ticketCount = publicPlayerState.ticketCount();
                cardCount = publicPlayerState.cardCount();

                return INT_SERDE.serialize(ticketCount)
                        + COMMA + INT_SERDE.serialize(cardCount)
                        + COMMA + STRING_SERDE.serialize(s);
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
                String route;
                String ticket;
                String card;

                route = String.join(playerState.routes().toString(), COMMA);
                ticket = String.join(playerState.tickets().toString(), COMMA);
                card = String.join(playerState.cards().toString(), COMMA);

                return STRING_SERDE.serialize(ticket)
                        + COMMA + STRING_SERDE.serialize(card)
                        + COMMA + STRING_SERDE.serialize(route);
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

                lastPlayer = publicGameState.lastPlayer();

                if((publicGameState.lastPlayer()== null)) { // gerer comme ca quand null est last player ou alors mettre le stringnull?
                    return INT_SERDE.serialize(ticketCount)
                            + DOUBLE_POINT + PUBLIC_CARD_STATE_SERDE.serialize(publicCardState)
                            + DOUBLE_POINT + PLAYER_ID_SERDE.serialize(currentPlayerId)
                            + DOUBLE_POINT + PUBLIC_PLAYER_STATE_SERDE.serialize(playerStateUn)
                            + DOUBLE_POINT + PUBLIC_PLAYER_STATE_SERDE.serialize(playerStateDeux)
                            + DOUBLE_POINT +STRING_SERDE.serialize("");
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

                for (PlayerId id : PlayerId.ALL) {
                    int i = 3;
                    playerStateMap.put(PLAYER_ID_SERDE.deSerialize(id.toString()),
                            PUBLIC_PLAYER_STATE_SERDE.deSerialize(tab[i]));
                    i++;
                }

                return new PublicGameState(INT_SERDE.deSerialize(tab[0]),
                        PUBLIC_CARD_STATE_SERDE.deSerialize(tab[1]),
                        PLAYER_ID_SERDE.deSerialize(tab[2]),
                        playerStateMap,
                        PLAYER_ID_SERDE.deSerialize(tab[5]));
            }
    );
    //----------------------------------------------------------------------------------------------------
}
