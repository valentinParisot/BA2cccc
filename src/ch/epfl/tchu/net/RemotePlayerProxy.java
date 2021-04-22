package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class RemotePlayerProxy implements Player {

    private final Socket socket;

    public RemotePlayerProxy(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {

        Serde<List<String>> listSerde = Serdes.LIST_OF_STRING_SERDE;
        Serde<PlayerId> idSerde = Serdes.PLAYER_ID_SERDE;

        List<String> list = new ArrayList<>();
        list.add(playerNames.get(PlayerId.PLAYER_1));
        list.add(playerNames.get(PlayerId.PLAYER_2));

        String iP = (idSerde.serialize(ownId)
                + " "
                + listSerde.serialize(list));

        send(MessageId.INIT_PLAYERS, iP);

    }

    @Override
    public void receiveInfo(String info) {

        Serde<String> stringSerde = Serdes.STRING_SERDE;
        String rInf = stringSerde.serialize(info);

        send(MessageId.RECEIVE_INFO, rInf);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

        Serde<PublicGameState> pgsSerde = Serdes.PUBLIC_GAME_STATE_SERDE;
        Serde<PlayerState> stringSerde = Serdes.PLAYER_STATE_SERDE;

        String uS = (pgsSerde.serialize(newState)
                + " "
                + stringSerde.serialize(ownState));

        send(MessageId.UPDATE_STATE, uS);

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        String sITC = ticketSerde.serialize(tickets);

        send(MessageId.SET_INITIAL_TICKETS, sITC);

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return null;
    }

    @Override
    public TurnKind nextTurn() {
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        return null;
    }

    @Override
    public int drawSlot() {
        return 0;
    }

    @Override
    public Route claimedRoute() {
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        return null;
    }

    private void send(MessageId id, String serialized) {

        try (ServerSocket s0 = new ServerSocket(5108);
             Socket s = s0.accept();
             BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(s.getInputStream(),
                                     US_ASCII));
             BufferedWriter w =
                     new BufferedWriter(
                             new OutputStreamWriter(s.getOutputStream(),
                                     US_ASCII))) {

            w.write(id.name() + " " + serialized);
            w.write('\n');
            w.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private void receive() {

        try (ServerSocket s0 = new ServerSocket(5108);
             Socket s = s0.accept();
             BufferedReader r =
                     new BufferedReader(
                             new InputStreamReader(s.getInputStream(),
                                     US_ASCII));
             BufferedWriter w =
                     new BufferedWriter(
                             new OutputStreamWriter(s.getOutputStream(),
                                     US_ASCII))) {

            r.readLine();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
