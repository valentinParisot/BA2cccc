/*
 *  Author:	Gilles de Waha
 *  Date: 	11 mars 2021
 */
package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import ch.epfl.tchu.gui.Info;
import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.SortedBag.Builder;
import ch.epfl.tchu.game.Route.Level;

public class InfoTest {

    Info info = new Info("Gilles");

    @Test
    void cardNameWorks() {
        Card card = Card.BLUE;
        int count = 2;
        assertEquals("2 bleues", Info.cardName(card, count));
    }

    @Test
    void drawWorks() {
        assertEquals("\nGilles et Hadrien sont ex æqo avec 50 points !\n", Info.draw(List.of("Gilles", "Hadrien"), 50));
    }

    @Test
    void willPlayFirstWorks() {
        assertEquals("Gilles jouera en premier.\n\n", info.willPlayFirst());
    }

    @Test
    void keptTicketsWorks() {
        assertEquals("Gilles a gardé 2 billets.\n", info.keptTickets(2));
    }

    @Test
    void canPlayWorks() {
        assertEquals("\nC'est à Gilles de jouer.\n", info.canPlay());
    }

    @Test
    void drewTicketsWorks() {
        assertEquals("Gilles a tiré 1 billet...\n", info.drewTickets(1));
    }

    @Test
    void drewBlindCardsWorks() {
        assertEquals("Gilles a tiré une carte de la pioche.\n", info.drewBlindCard());
    }

    @Test
    void drewVisibleCardWorks() {
        assertEquals("Gilles a tiré une carte bleue visible.\n", info.drewVisibleCard(Card.BLUE));
    }

    @Test
    void claimedRoutesWorks() {
        final Station LAU = new Station(13, "Lausanne");
        final Station NEU = new Station(19, "Neuchâtel");
        List<Card> list = List.of(Card.LOCOMOTIVE, Card.RED, Card.BLACK);
        Builder<Card> builder = new SortedBag.Builder<Card>();
        for (Card c : list) {
            builder.add(2, c);
        }
        SortedBag<Card> cards = builder.build();
        assertEquals("Gilles a pris possession de la route Lausanne – Neuchâtel au moyen de 2 noires, 2 rouges et 2 locomotives.\n", info.claimedRoute(new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null), cards));
    }

    @Test
    void attemptsTunnelClaimedWorks() {
        final Station LAU = new Station(13, "Lausanne");
        final Station NEU = new Station(19, "Neuchâtel");
        List<Card> list = List.of(Card.LOCOMOTIVE, Card.RED, Card.BLACK);
        Builder<Card> builder = new SortedBag.Builder<Card>();
        for (Card c : list) {
            builder.add(2, c);
        }
        SortedBag<Card> cards = builder.build();
        assertEquals("Gilles tente de s'emparer du tunnel Lausanne – Neuchâtel au moyen de 2 noires, 2 rouges et 2 locomotives !\n", info.attemptsTunnelClaim(new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null), cards));
    }

    @Test
    void drewAdditionalCardsWorks() {
        assertEquals("Les cartes supplémentaires sont 2 noires et 1 locomotive. " + "Elles impliquent un coût additionnel de 1 carte.\n", info.drewAdditionalCards(SortedBag.of(2, Card.BLACK, 1, Card.LOCOMOTIVE), 1));
        assertEquals("Les cartes supplémentaires sont 2 noires et 1 locomotive. " + "Elles n'impliquent aucun coût additionnel.\n", info.drewAdditionalCards(SortedBag.of(2, Card.BLACK, 1, Card.LOCOMOTIVE), 0));
    }

    @Test
    void didNotClaimRouteWorks() {
        final Station LAU = new Station(13, "Lausanne");
        final Station NEU = new Station(19, "Neuchâtel");
        assertEquals("Gilles n'a pas pu (ou voulu) s'emparer de la route Lausanne – Neuchâtel.\n", info.didNotClaimRoute(new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null)));
    }

    @Test
    void lastTurnBeginsWorks() {
        assertEquals("\nGilles n'a plus que 2 wagons, le dernier tour commence !\n", info.lastTurnBegins(2));
    }



    @Test
    void wonWorks() {
        assertEquals("\nGilles remporte la victoire avec 100 points, contre 1 point !\n", info.won(100, 1));
    }

    @Test
    void getsLongestTrailBonusWorks() {
        final Station LAU = new Station(13, "Lausanne");
        final Station NEU = new Station(19, "Neuchâtel");
        final Station SOL = new Station(26, "Soleure");
       // Trail trail = new Trail(List.of(new Route("LAU_NEU_1", LAU, NEU, 4, Level.OVERGROUND, null), new Route("NEU_SOL_1", NEU, SOL, 4, Level.OVERGROUND, Color.GREEN)), LAU, SOL, 24);
       /// assertEquals("\nGilles reçoit un bonus de 10 points pour le plus long trajet (Lausanne – Soleure).\n", info.getsLongestTrailBonus(trail));
    }
}
