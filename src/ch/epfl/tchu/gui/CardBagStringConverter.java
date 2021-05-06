package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

    @Override
    public String toString(SortedBag<Card> cards) {
        List<String> carte = new ArrayList<>();

        for (Card c : cards.toSet()) {

            int n = cards.countOf(c);
            String s = (n + " " + Info.cardName(c, n));
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

    @Override
    public SortedBag<Card> fromString(String s) {
        throw new UnsupportedOperationException();
    }
}
