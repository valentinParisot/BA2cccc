package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


import static ch.epfl.tchu.net.Serdes.*;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * RemotePlayerClient
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class RemotePlayerClient {

    private final Player player;
    private final String name;
    private final int portNumber;
    private final static String SPACE = " ";

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param player
     * @param name
     * @param port
     */

    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.portNumber = port;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @throws IOException
     */

    public void run() {
    try {//ca suffit pour l'exception ?
        Socket socket = new Socket(name, portNumber);
        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream(),
                                US_ASCII));
        BufferedWriter writer =
                new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(),
                                US_ASCII));

        while (reader.readLine() != null) {
            String s = reader.readLine();
            String[] tab = s.split(Pattern.quote(SPACE), -1);

            switch (MessageId.valueOf(tab[0])) { // valut a utiliser ocmme ca ?e of es

                case INIT_PLAYERS: //non
                    PlayerId ownId = PLAYER_ID_SERDE.deSerialize(tab[1]);
                    Map<PlayerId, String> playersNames = new HashMap<>();

                    for (PlayerId id : PlayerId.ALL) {

                        playersNames.put(PLAYER_ID_SERDE.deSerialize(id.toString()),
                                name);
                    }
                    player.initPlayers(ownId, playersNames);


                    break;
                case RECEIVE_INFO:

                    String info = STRING_SERDE.deSerialize(tab[1]);
                    player.receiveInfo(info);

                    break;
                case UPDATE_STATE:  //comment est rangé la tab voir avec le class de hugo ?

                    PublicGameState publicGameState = PUBLIC_GAME_STATE_SERDE.deSerialize(tab[1]);
                    PlayerState playerState = PLAYER_STATE_SERDE.deSerialize(tab[2]);
                    player.updateState(publicGameState, playerState);

                    break;
                case SET_INITIAL_TICKETS:

                    SortedBag<Ticket> initial = BAG_OF_TICKET_SERDE.deSerialize(tab[1]);
                    player.setInitialTicketChoice(initial);

                    break;
                case CHOOSE_INITIAL_TICKETS:

                    SortedBag<Ticket> choose = player.chooseInitialTickets();
                    writer.write(BAG_OF_TICKET_SERDE.serialize(choose));

                    break;
                case NEXT_TURN:

                    Player.TurnKind turnKind = player.nextTurn();
                    writer.write(TURN_KIND_SERDE.serialize(turnKind));

                    break;
                case CHOOSE_TICKETS:

                    SortedBag<Ticket> options = BAG_OF_TICKET_SERDE.deSerialize(tab[1]);
                    SortedBag<Ticket> chooseTickets = player.chooseTickets(options);
                    writer.write(BAG_OF_TICKET_SERDE.serialize(chooseTickets));

                    break;
                case DRAW_SLOT:

                    int drawnSlot = player.drawSlot();
                    writer.write(INT_SERDE.serialize(drawnSlot));

                    break;
                case ROUTE://claimed route ?

                    Route route = player.claimedRoute();
                    writer.write(ROUTE_SERDE.serialize(route));

                    break;
                case CARDS:
                    SortedBag<Card> cards = player.initialClaimCards();
                    writer.write(BAG_OF_CARD_SERDE.serialize(cards));

                    break;
                case CHOOSE_ADDITIONAL_CARDS:

                    List<SortedBag<Card>> options2 = LIST_OF_SORTED_BAG_CARD_SERDE.deSerialize(tab[1]);
                    SortedBag<Card> card = player.chooseAdditionalCards(options2);
                    writer.write(BAG_OF_CARD_SERDE.serialize(card));

                    break;


                default:
                    break;
            }
        }
    }catch (IOException e) {
        throw new UncheckedIOException(e);
    }
    }
    //----------------------------------------------------------------------------------------------------
}

