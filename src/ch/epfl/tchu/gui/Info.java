package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Info
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Info {

    private final String playerName;

    //----------------------------------------------------------------------------------------------------

    /**
     * build a message generator linked to the player with the given name
     * @param playerName
     */

    public Info(String playerName) {
        this.playerName = playerName;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param card
     * @param count
     * @return
     */

    public static String cardName(Card card, int count) {

        Preconditions.checkArgument(card != null);
        String name = "";

        switch (card) {
            case BLACK:
                name = StringsFr.BLACK_CARD;
                break;
            case VIOLET:
                name = StringsFr.VIOLET_CARD;
                break;
            case BLUE:
                name = StringsFr.BLUE_CARD;
                break;
            case GREEN:
                name = StringsFr.GREEN_CARD;
                break;
            case YELLOW:
                name = StringsFr.YELLOW_CARD;
                break;
            case ORANGE:
                name = StringsFr.ORANGE_CARD;
                break;
            case RED:
                name = StringsFr.RED_CARD;
                break;
            case WHITE:
                name = StringsFr.WHITE_CARD;
                break;
            case LOCOMOTIVE:
                name = StringsFr.LOCOMOTIVE_CARD;
                break;

        }

        name = (name + StringsFr.plural(count));

        String text = String.format("%s %s", count, name);

        return text;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param playerNames
     * @param points
     * @return
     */

    public static String draw(List<String> playerNames, int points) {
        String toDraw = String.join(StringsFr.AND_SEPARATOR, playerNames);
        return String.format(StringsFr.DRAW, toDraw, points);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public String willPlayFirst() {
        return String.format(StringsFr.WILL_PLAY_FIRST, this.playerName);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param count
     * @return
     */

    public String keptTickets(int count) {
        return String.format(StringsFr.KEPT_N_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public String canPlay() {
        return String.format(StringsFr.CAN_PLAY, this.playerName);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param count
     * @return
     */

    public String drewTickets(int count){
        return String.format(StringsFr.DREW_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @return
     */

    public String drewBlindCard() {
        return String.format(StringsFr.DREW_BLIND_CARD, this.playerName);

    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param card
     * @return
     */

    public String drewVisibleCard(Card card) {
        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playerName, card);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param route
     * @return
     */

    private static String routeName(Route route) {
        Preconditions.checkArgument(route != null);
        String routes = (route.station1().name() + StringsFr.EN_DASH_SEPARATOR + route.station2().name());
        return routes;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param cards
     * @return
     */

    private static String cardsDescription(SortedBag<Card> cards) {

        List<String> cartes = new ArrayList<>();

        for (Card c : cards.toSet()) {
            int n = cards.countOf(c);
            String s = cardName(c, n);
            cartes.add(s);
        }

        if (cartes.size() == 1) {
            return cartes.get(0);
        }

        List<String> minusOne = new ArrayList<>();
        minusOne.addAll(cartes);
        minusOne.remove(minusOne.size() - 1);

        String textMinusOne = String.join(", ", minusOne);
        String lastOne = cartes.get(cartes.size() - 1);

        List<String> finalText = new ArrayList<>();
        finalText.add(textMinusOne);
        finalText.add(lastOne);

        return String.join(StringsFr.AND_SEPARATOR, finalText);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param route
     * @param cards
     * @return
     */

    public String claimedRoute(Route route, SortedBag<Card> cards) {

        String gares = routeName(route);
        String cartes = cardsDescription(cards);

        return String.format(StringsFr.CLAIMED_ROUTE, this.playerName, gares, cartes);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param route
     * @param initialCards
     * @return
     */

    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {

        String tunnel = routeName(route);
        String cartes = cardsDescription(initialCards);

        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playerName, tunnel, cartes);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param drawnCards
     * @param additionalCost
     * @return
     */

    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {
        String cards = cardsDescription(drawnCards);
        if (additionalCost == 0) {
            return (String.format(StringsFr.ADDITIONAL_CARDS_ARE, cards)
                    + " " +
                    StringsFr.NO_ADDITIONAL_COST);
        }
        return (String.format(StringsFr.ADDITIONAL_CARDS_ARE, cards)
                + " " +
                String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost)));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param route
     * @return
     */

    public String didNotClaimRoute(Route route){
        String gares = routeName(route);
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this. playerName, gares);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param carCount
     * @return
     */

    public String lastTurnBegins(int carCount){
        Preconditions.checkArgument(carCount<=2);
        return String.format(StringsFr.LAST_TURN_BEGINS, this.playerName, carCount, StringsFr.plural(carCount));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param longestTrail
     * @return
     */

    public String getsLongestTrailBonus(Trail longestTrail){

        String station1 = longestTrail.station1().name();
        String station2 = longestTrail.station2().name();

        return String.format(StringsFr.GETS_BONUS, this.playerName, station1 + StringsFr.EN_DASH_SEPARATOR + station2);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     *
     * @param points
     * @param loserPoints
     * @return
     */

    public String won(int points, int loserPoints){

        return String.format(StringsFr.WINS, this.playerName, points, loserPoints);
    }

    //----------------------------------------------------------------------------------------------------
}

