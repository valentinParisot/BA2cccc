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
            s -> new String(Base64.getDecoder().decode(s), StandardCharsets.UTF_8));

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
                StringJoiner sj = new StringJoiner(SEMICOLON);

                sj
                        .add(LIST_OF_CARD_SERDE.serialize(publicCardState.faceUpCards()))
                        .add(INT_SERDE.serialize(publicCardState.deckSize()))
                        .add(INT_SERDE.serialize(publicCardState.discardsSize()));

                return sj.toString();
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
                StringJoiner sj = new StringJoiner(SEMICOLON);

                sj
                        .add(INT_SERDE.serialize(publicPlayerState.ticketCount()))
                        .add(INT_SERDE.serialize(publicPlayerState.cardCount()))
                        .add(LIST_OF_ROUTE_SERDE.serialize(publicPlayerState.routes()));

                return sj.toString();
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
                StringJoiner sj = new StringJoiner(SEMICOLON);

                sj
                        .add(BAG_OF_TICKET_SERDE.serialize(playerState.tickets()))
                        .add(BAG_OF_CARD_SERDE.serialize(playerState.cards()))
                        .add(LIST_OF_ROUTE_SERDE.serialize(playerState.routes()));

                return sj.toString();
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
                StringJoiner sj = new StringJoiner(DOUBLE_POINT);
                String lastPlayer = publicGameState.lastPlayer() == null ?
                        STRING_SERDE.serialize("") :
                        PLAYER_ID_SERDE.serialize(publicGameState.lastPlayer());

                sj
                        .add(INT_SERDE.serialize(publicGameState.ticketsCount()))
                        .add(PUBLIC_CARD_STATE_SERDE.serialize(publicGameState.cardState()))
                        .add(PLAYER_ID_SERDE.serialize(publicGameState.currentPlayerId()))
                        .add(PUBLIC_PLAYER_STATE_SERDE.serialize(publicGameState.playerState(PLAYER_1)))
                        .add(PUBLIC_PLAYER_STATE_SERDE.serialize(publicGameState.playerState(PLAYER_2)))
                        .add(lastPlayer);

                return sj.toString();

            }
            , s -> {

                String[] tab = s.split(Pattern.quote(DOUBLE_POINT), -1);

                PublicPlayerState publicPlayerState1 = PUBLIC_PLAYER_STATE_SERDE.deSerialize(tab[3]);
                PublicPlayerState publicPlayerState2 = PUBLIC_PLAYER_STATE_SERDE.deSerialize(tab[4]);

                PlayerId playerId = tab[5].equals("") ?
                        null :
                        PLAYER_ID_SERDE.deSerialize(tab[5]);

                return new PublicGameState(INT_SERDE.deSerialize(tab[0]),
                        PUBLIC_CARD_STATE_SERDE.deSerialize(tab[1]),
                        PLAYER_ID_SERDE.deSerialize(tab[2]),
                        Map.of(PLAYER_1,publicPlayerState1,
                                PLAYER_2,publicPlayerState2),
                        playerId);
            }
    );
    //----------------------------------------------------------------------------------------------------
}
