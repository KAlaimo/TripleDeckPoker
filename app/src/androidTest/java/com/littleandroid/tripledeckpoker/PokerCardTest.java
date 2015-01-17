package com.littleandroid.tripledeckpoker;

import junit.framework.TestCase;

/**
 * Created by Kristen on 1/16/2015.
 */
public class PokerCardTest extends TestCase {
    public void testEquals() {
        PokerCard card1 = new PokerCard(Rank.EIGHT, Suit.HEARTS);
        PokerCard card2 = new PokerCard(Rank.EIGHT, Suit.HEARTS);
        PokerCard card3 = new PokerCard(Rank.EIGHT, Suit.CLUBS);
        PokerCard card4 = new PokerCard(Rank.JACK, Suit.HEARTS);
        PokerCard card5 = new PokerCard(Rank.JOKER, null);

        boolean result = card1.equals(card1);
        assertEquals(true, result);

        result = card1.equals(card2);
        assertEquals(true, result);

        result = card1.equals(card3);
        assertEquals(false, result);

        result = card1.equals(card4);
        assertEquals(false, result);

        result = card1.equals(card5);
        assertEquals(false, result);

        result = card1.isFace();
        assertEquals(false, result);

        result = card4.isFace();
        assertEquals(true, result);

        result = card5.isFace();
        assertEquals(false, result);
    }
}
