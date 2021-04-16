package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

class Serdes {

    //----------------------------------------------------------------------------------------------------

    public static final Serde<Integer> intSerde = Serde.of(
             i -> Integer.toString(i),
             Integer::parseInt);

    //----------------------------------------------------------------------------------------------------

    public static final Serde<String> stringSerde = Serde.of(
            s -> Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)),
            s -> new String(Base64.getDecoder().decode(s),
                    StandardCharsets.UTF_8));

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    //----------------------------------------------------------------------------------------------------

    public static final Serde<List<String>> listOfStringSerde = Serde.listOf(stringSerde, ",");
    public static final Serde<List<Card>> listOfCardSerde = Serde.listOf(cardSerde, ",");
    public static final Serde<List<Route>> listOfRouteSerde = Serde.listOf(routeSerde, ",");
    public static final Serde<SortedBag<Card>> bagOfCardSerde = Serde.bagOf(cardSerde, ",");
    public static final Serde<SortedBag<Ticket>> bagOfTicketSerde = Serde.bagOf(ticketSerde, ",");
    public static final Serde<List<SortedBag<Card>>> bagOfSortedBagCardSerde = Serde.listOf(bagOfCardSerde, ";");

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PublicCardState> PublicCardStateSerde = Serde.of(
       publicCardState -> {
         String s;
         int deckSize ;
         int discardsSize;

         s = String.join(publicCardState.faceUpCards().toString(),"," );
           deckSize = publicCardState.deckSize();
           discardsSize = publicCardState.discardsSize();

         String fin = s + ";" + deckSize +";" + discardsSize;
         return fin;
       }
       ,s -> {
         String[] tab = s.split(";");

         return new PublicCardState(listOfCardSerde.deSerialize(tab[0]), intSerde.deSerialize(tab[1]), intSerde.deSerialize(tab[2]));
            }
    );

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PublicPlayerState> PublicPlayerStateSerde = Serde.of(
            PublicPlayerState -> {
                String s;
                int ticketCount;
                int cardCount;

                s = String.join(PublicPlayerState.routes().toString(),"," );
                ticketCount = PublicPlayerState.ticketCount();
                cardCount = PublicPlayerState.cardCount();

                String fin = ticketCount + ";" + cardCount +";" + s;
                return fin;
            }
            ,s -> {
                String[] tab = s.split(";");

                return new PublicPlayerState (intSerde.deSerialize(tab[0]), intSerde.deSerialize(tab[1]),listOfRouteSerde.deSerialize(tab[2]));
            }
    );

    //----------------------------------------------------------------------------------------------------

    public static final Serde<PlayerState> PlayerStateSerde = Serde.of(
            PlayerState -> {
                String route;
                String ticket;
                String card;

                route = String.join(PlayerState.routes().toString(),"," );
                ticket = String.join(PlayerState.tickets().toString(), "," );
                card =  String.join(PlayerState.cards().toString(), "," );

                String fin = ticket + ";" + card +";" + route;
                return fin;
            }
            ,s -> {
                String[] tab = s.split(";");

                return new PlayerState(bagOfTicketSerde.deSerialize(tab[0]), bagOfCardSerde.deSerialize(tab[1]), listOfRouteSerde.deSerialize(tab[2]));
            }
    );
    //----------------------------------------------------------------------------------------------------

      public static final Serde<PublicGameState> PublicGameStateSerde = Serde.of(
            PublicGameState -> {
                int ticketCount;
                PublicCardState cardState;
                PlayerId currentPlayerId;
                PublicPlayerState playerStateUn;
                PublicPlayerState playerStateDeux;
                PlayerId lastPlayer;

                ticketCount =  PublicGameState.ticketsCount();
                cardState = ; // comment ici ?
                currentPlayerId = PublicGameState.currentPlayerId();
                playerStateUn = PublicGameState.playerState(PLAYER_1);// seulement cela comme operation?
                playerStateDeux = PublicGameState.playerState(PLAYER_2);
                lastPlayer = PublicGameState.lastPlayer();

                String fin = ticketCount + ":" + cardState + ":" + currentPlayerId + ":" + playerStateUn + ":" +  playerStateDeux + ":" + lastPlayer;
                return fin;
            }
            , s -> {
                  String[] tab = s.split(":");

                  // comment gerer la map ?
                  return new PublicGameState(intSerde.deSerialize(tab[0]),PublicCardStateSerde.deSerialize(tab[1]), playerIdSerde.deSerialize(tab[3]), PlayerStateSerde.deSerialize(tab[3]), PlayerStateSerde.deSerialize(tab[4]),playerIdSerde.deSerialize(tab[5]));
              }
    );
    //----------------------------------------------------------------------------------------------------
}
