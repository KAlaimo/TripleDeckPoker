package com.littleandroid.tripledeckpoker;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.security.spec.PKCS8EncodedKeySpec;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void runTest() {

        testPokerCard();
        testPokerDeck();
        testJacksOrBetterPokerHand();
        testDoubleBonusPokerHand();
        testDoubleDoubleBonusPokerHand();
   }

    private void testPokerCard() {
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

    private void testPokerDeck() {
        PokerDeck deck = new PokerDeck(DeckSize.DECK_52);

        int cardCount = deck.getCardCount();
        assertEquals(52, cardCount);

        PokerCard card1 = deck.dealCard();
        cardCount = deck.getCardCount();
        assertEquals(51, cardCount);

        boolean firstCardRankIsAce = card1.getRank() == Rank.ACE;
        assertEquals(true, firstCardRankIsAce);

        PokerDeck deck2 = new PokerDeck(DeckSize.DECK_53);
        cardCount = deck2.getCardCount();
        assertEquals(53, cardCount);

        PokerCard cardJoker = new PokerCard(Rank.JOKER, null);
        boolean removeResult = deck2.removeCard(cardJoker);    // this deck should have a joker
        assertEquals(true, removeResult);
        cardCount = deck2.getCardCount();
        assertEquals(52, cardCount);
        removeResult = deck.removeCard(cardJoker); // this deck should not have a joker
        assertEquals(false, removeResult);
        cardCount = deck.getCardCount();
        assertEquals(51, cardCount);

    }

    private void testJacksOrBetterPokerHand() {
        JacksOrBetterPokerHand pokerHand = new JacksOrBetterPokerHand(getContext());
        testPokerHand(pokerHand);

        pokerHand.clearHand();
        makeFourOfAKind(pokerHand);
        HandType hand = pokerHand.getHandType();
        boolean isFourOfAKind = hand == HandType.HAND_FOUR_OF_A_KIND;
        assertEquals(true, isFourOfAKind);
    }

    private void testDoubleBonusPokerHand() {
        DoubleBonusPokerHand pokerHand = new DoubleBonusPokerHand(getContext());
        testPokerHand(pokerHand);
    }

    private void testDoubleDoubleBonusPokerHand() {
        DoubleDoubleBonusPokerHand pokerHand = new DoubleDoubleBonusPokerHand(getContext());
        testPokerHand(pokerHand);
    }

    private void testPokerHand(PokerHand p) {
        makeFullHouse(p);
        HandType hand = p.getHandType();
        boolean isFullHouse = hand == HandType.HAND_FULL_HOUSE;
        assertEquals(true, isFullHouse);

        p.clearHand();
        int size = p.size();
        assertEquals(0, size);

        makeTwoPairs(p);
        hand = p.getHandType();
        boolean isTwoPairs = hand == HandType.HAND_TWO_PAIRS;
        assertEquals(true, isTwoPairs);

        p.clearHand();
        makeNaturalRoyalFlush(p);
        hand = p.getHandType();
        boolean isSameSuit = p.allSameSuit();
        assertEquals(true, isSameSuit);
        boolean isStraight = p.isStraight();
        assertEquals(true, isStraight);
        boolean isRoyalFlush = hand == HandType.HAND_ROYAL_FLUSH;
        assertEquals(true, isRoyalFlush);

        p.clearHand();
        makeStraight(p);
        hand = p.getHandType();
        isStraight = hand == HandType.HAND_STRAIGHT;
        assertEquals(true, isStraight);

        p.clearHand();
        makeThreeOfAKind(p);
        hand = p.getHandType();
        boolean isThreeOfAKind = hand == HandType.HAND_THREE_OF_A_KIND;
        assertEquals(true, isThreeOfAKind);

        p.clearHand();
        makeJacksOrBetter(p);
        hand = p.getHandType();
        boolean isJacksOrBetter = hand == HandType.HAND_JACKS_OR_BETTER;
        assertEquals(true, isJacksOrBetter);
    }

    private void makeNaturalRoyalFlush(PokerHand p) {
        p.addCard(new PokerCard(Rank.JACK, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.ACE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.KING, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.QUEEN, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.TEN, Suit.DIAMONDS));
    }

    private void makeStraight(PokerHand p) {
        p.addCard(new PokerCard(Rank.TEN, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.EIGHT, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.NINE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.SEVEN, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.SIX, Suit.DIAMONDS));
    }

    private void makeFullHouse(PokerHand p) {
        p.addCard(new PokerCard(Rank.EIGHT, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.EIGHT, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.EIGHT, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.SPADES));
    }

    private void makeFourOfAKind(PokerHand p) {
        p.addCard(new PokerCard(Rank.FIVE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.SEVEN, Suit.SPADES));
        p.addCard(new PokerCard(Rank.FIVE, Suit.SPADES));
        p.addCard(new PokerCard(Rank.FIVE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.FIVE, Suit.HEARTS));
    }

    private void makeThreeOfAKind(PokerHand p) {
        p.addCard(new PokerCard(Rank.SIX, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.KING, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.SIX, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.SIX, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.DEUCE, Suit.SPADES));
    }

    private void makeTwoPairs(PokerHand p) {
        p.addCard(new PokerCard(Rank.DEUCE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.NINE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.SEVEN, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.DEUCE, Suit.SPADES));
        p.addCard(new PokerCard(Rank.SEVEN, Suit.HEARTS));
    }

    private void makeJacksOrBetter(PokerHand p) {
        p.addCard(new PokerCard(Rank.NINE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.JACK, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.JACK, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.SIX, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.DEUCE, Suit.SPADES));
    }
}