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

        String initPlayers = (idSerde.serialize(ownId)
                + " "
                + listSerde.serialize(list));

        send(MessageId.INIT_PLAYERS, initPlayers);

    }

    @Override
    public void receiveInfo(String info) {

        Serde<String> stringSerde = Serdes.STRING_SERDE;
        String rInfo = stringSerde.serialize(info);

        send(MessageId.RECEIVE_INFO, rInfo);
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

        Serde<PublicGameState> pgsSerde = Serdes.PUBLIC_GAME_STATE_SERDE;
        Serde<PlayerState> stringSerde = Serdes.PLAYER_STATE_SERDE;

        String updateState = (pgsSerde.serialize(newState)
                + " "
                + stringSerde.serialize(ownState));

        send(MessageId.UPDATE_STATE, updateState);

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        String sITChoice = ticketSerde.serialize(tickets);

        send(MessageId.SET_INITIAL_TICKETS, sITChoice);

    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {  // correct pour les retours non void? r.readLine() bien utilisé?

        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        send(MessageId.CHOOSE_INITIAL_TICKETS, "");

        SortedBag<Ticket> cITickets = ticketSerde.deSerialize(receive());

        return cITickets;
    }

    @Override
    public TurnKind nextTurn() {

        Serde<TurnKind> ticketSerde = Serdes.TURN_KIND_SERDE;
        send(MessageId.NEXT_TURN, "");

        TurnKind nextTurn = ticketSerde.deSerialize(receive());

        return nextTurn;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {

        Serde<SortedBag<Ticket>> ticketSerde = Serdes.BAG_OF_TICKET_SERDE;
        send(MessageId.CHOOSE_TICKETS, ticketSerde.serialize(options));

        SortedBag<Ticket> chooseTickets = ticketSerde.deSerialize(receive());

        return chooseTickets;
    }

    @Override
    public int drawSlot() {

        Serde<Integer> intSerde = Serdes.INT_SERDE;
        send(MessageId.DRAW_SLOT, "");

        int drawSlot = intSerde.deSerialize(receive()).intValue();

        return drawSlot;
    }

    @Override
    public Route claimedRoute() {

        Serde<Route> routeSerde = Serdes.ROUTE_SERDE;
        send(MessageId.ROUTE, "");

        Route cRoute = routeSerde.deSerialize(receive());

        return cRoute;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {

        Serde<SortedBag<Card>> cardSerde = Serdes.BAG_OF_CARD_SERDE;
        send(MessageId.CARDS, "");

        SortedBag<Card> iCCards = cardSerde.deSerialize(receive());

        return iCCards;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {

        Serde<SortedBag<Card>> cardSerde = Serdes.BAG_OF_CARD_SERDE;
        Serde<List<SortedBag<Card>>> listSerde = Serdes.LIST_OF_SORTED_BAG_CARD_SERDE;

        send(MessageId.CHOOSE_ADDITIONAL_CARDS, listSerde.serialize(options));
        SortedBag<Card> cACards = cardSerde.deSerialize(receive());

        return cACards;
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

            String message = (serialized.equals(""))
                    ? (id.name())
                    : (id.name() + " " + serialized);

            w.write(message);
            w.write('\n');
            w.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private String receive() {  // receive = string?

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

            String message = r.readLine();
            return message;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }
}
