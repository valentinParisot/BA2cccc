package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * CardBagStringConverter
 * class
 *
 * @author Valentin Parisot (326658)
 * @author Hugo Jeannin (329220)
 */

public class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

    //----------------------------------------------------------------------------------------------------

    private static final String SPACE = " ";
    private static final String COMMA_SPACE = ", ";

    //----------------------------------------------------------------------------------------------------

    /**
     * Create string with given cards
     *
     * @param cards given cards
     * @return String with given cards
     */

    @Override
    public String toString(SortedBag<Card> cards) {
        List<String> carte = new ArrayList<>();

        for (Card c : cards.toSet()) {

            int n = cards.countOf(c);
            String s = (n + SPACE + Info.cardName(c, n));
            carte.add(s);
        }

        if (carte.size() == 1) {
            return carte.get(0);
        }

        List<String> minusOne = new ArrayList<>(carte);
        minusOne.remove(minusOne.size() - 1);

        String textMinusOne = String.join(COMMA_SPACE, minusOne);
        String lastOne = carte.get(carte.size() - 1);

        List<String> finalText = new ArrayList<>();
        finalText.add(textMinusOne);
        finalText.add(lastOne);

        return String.join(StringsFr.AND_SEPARATOR, finalText);
    }

    //----------------------------------------------------------------------------------------------------

    @Override
    public SortedBag<Card> fromString(String s) {
        throw new UnsupportedOperationException();
    }

    //----------------------------------------------------------------------------------------------------
}
