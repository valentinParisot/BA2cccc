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

    //----------------------------------------------------------------------------------------------------

    private final Player player;
    private final String name;
    private final int portNumber;
    private final static String SPACE = " ";
    private final static int LIMIT = -1;

    //----------------------------------------------------------------------------------------------------

    /**
     * constructor
     *
     * @param player the player
     * @param name name of the player
     * @param port the port to use to connect to the proxy
     */

    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.portNumber = port;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * waits for a message from the proxy,
     * splits it using the space character as a separator,
     * determines the type of the message according to the first string resulting from the splitting,
     * depending on this type of message, deserialize the arguments, call the player's corresponding method;
     * if this method returns a result, serializes it to return it to the proxy in response.
     *
     */

    public void run() {
        try {
            Socket socket = new Socket(name, portNumber);
            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(socket.getInputStream(),
                                    US_ASCII));
            BufferedWriter writer =
                    new BufferedWriter(
                            new OutputStreamWriter(socket.getOutputStream(),
                                    US_ASCII));
            String s;

            while ( (s = reader.readLine()) != null) {


                String[] tab = s.split(Pattern.quote(SPACE), LIMIT);
                MessageId firstTab = MessageId.valueOf(tab[0]);
                switcher(firstTab,tab,writer);

               /** switch (MessageId.valueOf(tab[0])) {

                    case INIT_PLAYERS:

                        PlayerId ownId = PLAYER_ID_SERDE.deSerialize(tab[1]);
                        Map<PlayerId, String> playersNames = new HashMap<>();
                        List<String> z = LIST_OF_STRING_SERDE.deSerialize(tab[2]);

                        for (PlayerId id : PlayerId.ALL) {

                            playersNames.put(id, z.get(id.ordinal()));
                        }
                        player.initPlayers(ownId, playersNames);


                        break;
                    case RECEIVE_INFO:

                        String info = STRING_SERDE.deSerialize(tab[1]);
                        player.receiveInfo(info);

                        break;
                    case UPDATE_STATE:

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
                        write(writer,BAG_OF_TICKET_SERDE.serialize(choose));

                        break;
                    case NEXT_TURN:

                        Player.TurnKind turnKind = player.nextTurn();
                        write(writer,TURN_KIND_SERDE.serialize(turnKind));

                        break;
                    case CHOOSE_TICKETS:

                        SortedBag<Ticket> options = BAG_OF_TICKET_SERDE.deSerialize(tab[1]);
                        SortedBag<Ticket> chooseTickets = player.chooseTickets(options);
                        write(writer,BAG_OF_TICKET_SERDE.serialize(chooseTickets));

                        break;
                    case DRAW_SLOT:

                        int drawnSlot = player.drawSlot();
                        write(writer,INT_SERDE.serialize(drawnSlot));

                        break;
                    case ROUTE:

                        Route route = player.claimedRoute();
                        write(writer,ROUTE_SERDE.serialize(route));

                        break;
                    case CARDS:
                        SortedBag<Card> cards = player.initialClaimCards();
                        write(writer,BAG_OF_CARD_SERDE.serialize(cards));

                        break;
                    case CHOOSE_ADDITIONAL_CARDS:

                        List<SortedBag<Card>> options2 = LIST_OF_SORTED_BAG_CARD_SERDE.deSerialize(tab[1]);
                        SortedBag<Card> card = player.chooseAdditionalCards(options2);
                        write(writer,BAG_OF_CARD_SERDE.serialize(card));

                        break;

                    default:
                        break;
                }**/
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * write
     * @param writer BufferedWriter
     * @param string string to write
     * @throws IOException if an I/O error occurs
     */

    private static void write(BufferedWriter writer,String string) throws IOException {

        writer.write(string);
        writer.write('\n');
        writer.flush();

    }

    //----------------------------------------------------------------------------------------------------

    private void switcher(MessageId messageId,String[] tab, BufferedWriter writer){
        try {
        switch (messageId) {

            case INIT_PLAYERS:

                PlayerId ownId = PLAYER_ID_SERDE.deSerialize(tab[1]);
                Map<PlayerId, String> playersNames = new HashMap<>();
                List<String> z = LIST_OF_STRING_SERDE.deSerialize(tab[2]);

                for (PlayerId id : PlayerId.ALL) {

                    playersNames.put(id, z.get(id.ordinal()));
                }
                player.initPlayers(ownId, playersNames);

                break;
            case RECEIVE_INFO:

                String info = STRING_SERDE.deSerialize(tab[1]);
                player.receiveInfo(info);

                break;
            case UPDATE_STATE:

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
                write(writer,BAG_OF_TICKET_SERDE.serialize(choose));

                break;
            case NEXT_TURN:

                Player.TurnKind turnKind = player.nextTurn();
                write(writer,TURN_KIND_SERDE.serialize(turnKind));

                break;
            case CHOOSE_TICKETS:

                SortedBag<Ticket> options = BAG_OF_TICKET_SERDE.deSerialize(tab[1]);
                SortedBag<Ticket> chooseTickets = player.chooseTickets(options);
                write(writer,BAG_OF_TICKET_SERDE.serialize(chooseTickets));

                break;
            case DRAW_SLOT:

                int drawnSlot = player.drawSlot();
                write(writer,INT_SERDE.serialize(drawnSlot));

                break;
            case ROUTE:

                Route route = player.claimedRoute();
                write(writer,ROUTE_SERDE.serialize(route));

                break;
            case CARDS:
                SortedBag<Card> cards = player.initialClaimCards();
                write(writer,BAG_OF_CARD_SERDE.serialize(cards));

                break;
            case CHOOSE_ADDITIONAL_CARDS:

                List<SortedBag<Card>> options2 = LIST_OF_SORTED_BAG_CARD_SERDE.deSerialize(tab[1]);
                SortedBag<Card> card = player.chooseAdditionalCards(options2);
                write(writer,BAG_OF_CARD_SERDE.serialize(card));

                break;

            default:
                break;
        }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}


