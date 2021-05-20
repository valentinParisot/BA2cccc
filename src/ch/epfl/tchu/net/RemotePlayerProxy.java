package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * InfoViewCreator
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class RemotePlayerProxy implements Player {

    //----------------------------------------------------------------------------------------------------

    private final BufferedReader r;
    private final BufferedWriter w;
    private final static String SPACE = " ";
    private final static String NOTHING = "";

    //----------------------------------------------------------------------------------------------------

    /**
     * Its constructor takes as argument the "socket"
     * which the proxy uses to communicate through the network with the client by exchanging text messages
     * @param socket socket use to communicate
     * @throws IOException if the channel is non blocking mode
     */

    public RemotePlayerProxy(Socket socket) throws IOException {
        this.r =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream(),
                                US_ASCII));
        this.w =
                new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(),
                                US_ASCII));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * initialize players in the game
     * @param ownId       the ID of the player
     * @param playerNames the name of each player
     */

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        Serde<List<String>> listSerde = Serdes.LIST_OF_STRING_SERDE;
        Serde<PlayerId> idSerde = Serdes.PLAYER_ID_SERDE;

        List<String> list = new ArrayList<>();
        list.add(playerNames.get(PlayerId.PLAYER_1));
        list.add(playerNames.get(PlayerId.PLAYER_2));

        String initPlayers = (idSerde.serialize(ownId)
                                + SPACE
                                + listSerde.serialize(list));

        send(MessageId.INIT_PLAYERS, initPlayers);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make receive the information
     * @param info any information the player should receive
     */

    @Override
    public void receiveInfo(String info) {
        Serde<String> stringSerde = Serdes.STRING_SERDE;
        String rInfo = stringSerde.serialize(info);

        send(MessageId.RECEIVE_INFO, rInfo);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Send the state updated
     * @param newState the new state of the game
     * @param ownState the new state of the player
     */

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        Serde<PublicGameState> pgsSerde = Serdes.PUBLIC_GAME_STATE_SERDE;
        Serde<PlayerState> stringSerde = Serdes.PLAYER_STATE_SERDE;

        String updateState = (pgsSerde.serialize(newState)
                                + SPACE
                                + stringSerde.serialize(ownState));

        send(MessageId.UPDATE_STATE, updateState);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Send the Set initial tickets in the game
     * @param tickets the 5 initial tickets distributed to the player
     */

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        String sITChoice = ticketSerde.serialize(tickets);

        send(MessageId.SET_INITIAL_TICKETS, sITChoice);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication of the chosen initial tickets
     * @return Sorted bag of chosen tickets
     */

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        send(MessageId.CHOOSE_INITIAL_TICKETS, NOTHING);

        return ticketSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication about the kind of the turn
     * @return kind of the turn
     */

    @Override
    public TurnKind nextTurn() {
        Serde<TurnKind> ticketSerde = Serdes.TURN_KIND_SERDE;
        send(MessageId.NEXT_TURN, NOTHING);

        return ticketSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication of the chosen tickets
     * @param options the additional tickets proposed to the player
     * @return
     */

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        send(MessageId.CHOOSE_TICKETS, ticketSerde.serialize(options));

        return ticketSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication about the slot and the action is drawn
     * @return the drawn slot
     */

    @Override
    public int drawSlot() {
        Serde<Integer> intSerde = Serdes.INT_SERDE;
        send(MessageId.DRAW_SLOT, NOTHING);

        return intSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication about the claimed route
     * @return claimed route
     */

    @Override
    public Route claimedRoute() {
        Serde<Route> routeSerde = Serdes.ROUTE_SERDE;
        send(MessageId.ROUTE, NOTHING);

        return routeSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication about the initialClaimCards
     * @return sorted bag of initialClaimCards
     */

    @Override
    public SortedBag<Card> initialClaimCards() {
        Serde<SortedBag<Card>> cardSerde = Serdes.BAG_OF_CARD_SERDE;
        send(MessageId.CARDS, NOTHING);

        return cardSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Make the communication about chooseAdditionalCards
     * @param options the additional cards proposed to the player
     * @return sorted bag of chooseAdditionalCards
     */

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        Serde<SortedBag<Card>> cardSerde = Serdes.BAG_OF_CARD_SERDE;
        Serde<List<SortedBag<Card>>> listSerde = Serdes.LIST_OF_SORTED_BAG_CARD_SERDE;

        send(MessageId.CHOOSE_ADDITIONAL_CARDS, listSerde.serialize(options));

        return cardSerde.deSerialize(receive());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Writes a message to the client
     * @param id         the type of message
     * @param serialized the message we want to send (already serialized)
     */

    private void send(MessageId id, String serialized) {
        try {
            String message = (serialized.equals(NOTHING))
                    ? (id.name())
                    : (id.name() + SPACE + serialized);

            w.write(message);
            w.write('\n');
            w.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * Reads the message sent by the client
     * @return the message received
     */

    private String receive() {
        try {
            String message = r.readLine();
            return message;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //----------------------------------------------------------------------------------------------------
}
