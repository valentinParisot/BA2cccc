package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Info
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public final class Info {

    //----------------------------------------------------------------------------------------------------

    private final String playerName;

    //----------------------------------------------------------------------------------------------------


    public Info(String playerName) {
        this.playerName = playerName;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param card  card
     * @param count multiplicity of the card
     * @return the name of the card (singular or plural depending on count)
     * @throws IllegalArgumentException id card is null
     */
    public static String cardName(Card card, int count) {

        String name = "";

        switch (Objects.requireNonNull(card)) {
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
            default:
                break;

        }

        name = (name + StringsFr.plural(count));

        return name;
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param playerNames names of the 2 players
     * @param points      their points
     * @return a message stating that the 2 players finished the game ex æqo (+ @points)
     */
    public static String draw(List<String> playerNames, int points) {

        return String.join(StringsFr.AND_SEPARATOR, playerNames);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return a message stating that the current player will play first
     */
    public String willPlayFirst() {

        return String.format(StringsFr.WILL_PLAY_FIRST, this.playerName);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param count number of tickets that the player kept
     * @return a message stating that the player kept @count tickets
     */
    public String keptTickets(int count) {

        return String.format(StringsFr.KEPT_N_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return a message stating that the player can play
     */
    public String canPlay() {

        return String.format(StringsFr.CAN_PLAY, this.playerName);
    }

    //----------------------------------------------------------------------------------------------------


    /**
     * @param count number of drawn tickets
     * @return a message stating that the player drew @count tickets
     */
    public String drewTickets(int count) {

        return String.format(StringsFr.DREW_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @return a message stating that the player drew a card from the deck
     */
    public String drewBlindCard() {

        return String.format(StringsFr.DREW_BLIND_CARD, this.playerName);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param card one of the 5 visible cards
     * @return a message stating that the player drew @card
     */
    public String drewVisibleCard(Card card) {

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

        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playerName, name);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route route
     * @return the 2 stations of @route separated by " - "
     * @throws IllegalArgumentException if route is null
     */
    private static String routeName(Route route) {

        Objects.requireNonNull(route);

        return (route.station1().name() + StringsFr.EN_DASH_SEPARATOR + route.station2().name());
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param cards a group of cards
     * @return the cards form @cards with their multiplicity
     */
    private static String cardsDescription(SortedBag<Card> cards) {

        List<String> carte = new ArrayList<>();

        for (Card c : cards.toSet()) {

            int n = cards.countOf(c);
            String s = (n + " " + cardName(c, n));
            carte.add(s);
        }

        if (carte.size() == 1) {
            return carte.get(0);
        }

        List<String> minusOne = new ArrayList<>(carte);
        minusOne.remove(minusOne.size() - 1);

        String textMinusOne = String.join(", ", minusOne);
        String lastOne = carte.get(carte.size() - 1);

        List<String> finalText = new ArrayList<>();
        finalText.add(textMinusOne);
        finalText.add(lastOne);

        return String.join(StringsFr.AND_SEPARATOR, finalText);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route the route that the player took
     * @param cards cards used by the player
     * @return a message stating that the player took @route with @cards
     */
    public String claimedRoute(Route route, SortedBag<Card> cards) {

        String gare = routeName(route);
        String carte = cardsDescription(cards);

        return String.format(StringsFr.CLAIMED_ROUTE, this.playerName, gare, carte);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route        the tunnel the player wants to take
     * @param initialCards the cards the player wants to use
     * @return a message stating that the player wants to take @route with @initialCards
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards) {

        String tunnel = routeName(route);
        String carte = cardsDescription(initialCards);

        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playerName, tunnel, carte);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param drawnCards     the 3 additional cards the player drew
     * @param additionalCost the cost of @drawnCards
     * @return a message stating that the player drew @drawnCards and it implies a cost of @additionalCost
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost) {

        String cards = cardsDescription(drawnCards);

        if (additionalCost == 0) {

            return (String.format(StringsFr.ADDITIONAL_CARDS_ARE, cards) +
                    StringsFr.NO_ADDITIONAL_COST);
        }

        return (String.format(StringsFr.ADDITIONAL_CARDS_ARE, cards) +
                String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost)));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param route route
     * @return a message stating that the player couldn't or didn't want to claim @route
     */
    public String didNotClaimRoute(Route route) {

        String gare = routeName(route);

        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this.playerName, gare);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param carCount car count
     * @return a message stating that the player only has @carCount wagons remaining and hence the last turn begins
     * @throws IllegalArgumentException if car count is less or equal to 2
     */
    public String lastTurnBegins(int carCount) {

        Preconditions.checkArgument(carCount <= 2);

        return String.format(StringsFr.LAST_TURN_BEGINS, this.playerName, carCount, StringsFr.plural(carCount));
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param longestTrail the longest or one of the longest trail of the game
     * @return a message stating that the player earned a bonus for having @longestTrail
     */
    public String getsLongestTrailBonus(Trail longestTrail) {

        String station1 = longestTrail.station1().name();
        String station2 = longestTrail.station2().name();

        return String.format(StringsFr.GETS_BONUS, this.playerName, station1 + StringsFr.EN_DASH_SEPARATOR + station2);
    }

    //----------------------------------------------------------------------------------------------------

    /**
     * @param points      points of the player
     * @param loserPoints points of its opponent
     * @return a message stating that the player won the game with @points and its opponent lost with @looserPoints
     */
    public String won(int points, int loserPoints) {

        return String.format(StringsFr.WINS, this.playerName, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

    //----------------------------------------------------------------------------------------------------
}

