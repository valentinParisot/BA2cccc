package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;

  class Serdes {


    public static final Serde<Integer> intSerde = Serde.of(
             i -> Integer.toString(i),
             Integer::parseInt);

    public static final Serde<String> stringSerde =



    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());



    public static final Serde<List<String>> listOfStringSerde = Serde.listOf(stringSerde, ",");
    public static final Serde<List<Card>> listOfCardSerde = Serde.listOf(cardSerde, ",");
    public static final Serde<List<Route>> listOfRouteSerde = Serde.listOf(routeSerde, ",");
    public static final Serde<SortedBag<Card>> bagOfCardSerde = Serde.bagOf(cardSerde, ",");
    public static final Serde<SortedBag<Ticket>> bagOfTicketSerde = Serde.bagOf(ticketSerde, ",");
    public static final Serde<List<SortedBag<Card>>> bagOfSortedBagCardSerde = Serde.listOf(bagOfCardSerde, ";");



    public static final Serde<PublicCardState> PublicCardStateSerde =
    public static final Serde<PublicPlayerState> PublicPlayerStateSerde =
    public static final Serde<PlayerState> PlayerStateSerde =
    public static final Serde<PublicGameState> PublicGameStateSerde =



}
