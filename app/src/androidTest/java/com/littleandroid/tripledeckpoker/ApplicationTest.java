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
        testAcesFacesPokerHand();
        testDeucesWildPokerHand();
        testJokerPokerPokerHand();
   }

    private void testPokerCard() {
        PokerCard card1 = new PokerCard(Rank.EIGHT, Suit.HEARTS);
        PokerCard card2 = new PokerCard(Rank.EIGHT, Suit.HEARTS);
        PokerCard card3 = new PokerCard(Rank.EIGHT, Suit.CLUBS);
        PokerCard card4 = new PokerCard(Rank.JACK, Suit.HEARTS);
        PokerCard card5 = new PokerCard(Rank.JOKER, null);

        boolean result = card1.equals(card1);
        assertTrue("card1 card1", result);

        result = card1.equals(card2);
        assertTrue("card1 card2", result);

        result = card1.equals(card3);
        assertFalse("card1 card3", result);

        result = card1.equals(card4);
        assertFalse("card1 card4", result);

        result = card1.equals(card5);
        assertFalse("card1 card5", result);

        result = card1.isFace();
        assertFalse("card1 face", result);

        result = card4.isFace();
        assertTrue("card4 face", result);

        result = card5.isFace();
        assertFalse("card5 face", result);
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
        int testCount = testPokerHand(pokerHand);
        assertEquals(pokerHand.getGameName() + " failed test count", 9, testCount);

        // check payout of previous hand
        //int payout = pokerHand.getPayout(2);
        //assertEquals(pokerHand.getGameName() + " failed payout test", 50, payout);
    }

    private void testDoubleBonusPokerHand() {
        DoubleBonusPokerHand pokerHand = new DoubleBonusPokerHand(getContext());
        int testCount = testPokerHand(pokerHand);
        assertEquals(pokerHand.getGameName() + " failed test count", 11, testCount);

        // check payout of previous hand
        //int payout = pokerHand.getPayout(3);
        //assertEquals(pokerHand.getGameName() + " failed payout test", 150, payout);
    }

    private void testDoubleDoubleBonusPokerHand() {
        DoubleDoubleBonusPokerHand pokerHand = new DoubleDoubleBonusPokerHand(getContext());
        int testCount = testPokerHand(pokerHand);
        assertEquals(pokerHand.getGameName() + " failed test count", 13, testCount);

        // check payout of previous hand
        //int payout = pokerHand.getPayout(5);
        //assertEquals(pokerHand.getGameName() + " failed payout test", 800, payout);
    }

    private void testAcesFacesPokerHand() {
        AcesFacesPokerHand pokerHand = new AcesFacesPokerHand(getContext());
        int testCount = testPokerHand(pokerHand);
        assertEquals(pokerHand.getGameName() + " failed test count", 11, testCount);

        // check payout of previous hand
        //int payout = pokerHand.getPayout(1);
        //assertEquals(pokerHand.getGameName() + " failed payout test", 40, payout);
    }

    private void testDeucesWildPokerHand() {
        DeucesWildPokerHand pokerHand = new DeucesWildPokerHand(getContext());
        int testCount = testPokerHand(pokerHand); // test standard hands without wild cards
        assertEquals(pokerHand.getGameName() + " failed test count", 7, testCount);

        PokerCard deuce1 = new PokerCard(Rank.DEUCE, Suit.HEARTS);
        deuce1.setWild(true);
        PokerCard deuce2 = new PokerCard(Rank.DEUCE, Suit.DIAMONDS);
        deuce2.setWild(true);
        PokerCard deuce3 = new PokerCard(Rank.DEUCE, Suit.SPADES);
        deuce3.setWild(true);
        PokerCard deuce4 = new PokerCard(Rank.DEUCE, Suit.CLUBS);
        deuce4.setWild(true);

        makeNaturalRoyalFlush(pokerHand);
        pokerHand.replaceCard(2, deuce1);
        pokerHand.replaceCard(3, deuce2);
        HandType hand = pokerHand.getHandType();
        boolean isWildRoyalFlush = hand == HandType.HAND_WILD_ROYAL_FLUSH;
        assertTrue(pokerHand.getGameName() + " failed Wild Royal Flush", isWildRoyalFlush);

        pokerHand.clearHand();
        makeFullHouse(pokerHand);
        pokerHand.replaceCard(0, deuce3);
        hand = pokerHand.getHandType();
        boolean isFullHouse = hand == HandType.HAND_FULL_HOUSE;
        assertTrue(pokerHand.getGameName() + " failed Full House with Wilds: " + getContext().getResources().getString(hand.getHandResourceID()), isFullHouse);

        pokerHand.clearHand();
        makeStraight(pokerHand);
        pokerHand.replaceCard(0, deuce4);
        pokerHand.replaceCard(3, deuce1);
        hand = pokerHand.getHandType();
        boolean isStraight = hand == HandType.HAND_STRAIGHT;
        assertTrue(pokerHand.getGameName() + " failed Straight with Wilds: " + getContext().getResources().getString(hand.getHandResourceID()), isStraight);

        pokerHand.clearHand();
        makeStraightWithAce(pokerHand);
        pokerHand.replaceCard(0, deuce3);
        hand = pokerHand.getHandType();
        isStraight = hand == HandType.HAND_STRAIGHT;
        assertTrue(pokerHand.getGameName() + " failed Straight with Ace and Wilds: " + getContext().getResources().getString(hand.getHandResourceID()), isStraight);

        pokerHand.clearHand();
        makeFlush(pokerHand);
        pokerHand.replaceCard(2, deuce2);
        pokerHand.replaceCard(3, deuce3);
        hand = pokerHand.getHandType();
        boolean isFlush = hand == HandType.HAND_FLUSH;
        assertTrue(pokerHand.getGameName() + " failed Flush with Wilds: " + getContext().getResources().getString(hand.getHandResourceID()), isFlush);

        pokerHand.clearHand();
        pokerHand.addCard(deuce1);
        pokerHand.addCard(deuce2);
        pokerHand.addCard(deuce3);
        pokerHand.addCard(deuce4);
        pokerHand.addCard(new PokerCard(Rank.THREE, Suit.SPADES));
        hand = pokerHand.getHandType();
        boolean isFourDeuces = hand == HandType.HAND_FOUR_DEUCES;
        assertTrue(pokerHand.getGameName() + " failed Four Deuces", isFourDeuces);

        pokerHand.clearHand();
        pokerHand.addCard(new PokerCard(Rank.SIX, Suit.CLUBS));
        pokerHand.addCard(new PokerCard(Rank.SIX, Suit.DIAMONDS));
        pokerHand.addCard(deuce1);
        pokerHand.addCard(deuce2);
        pokerHand.addCard(deuce3);
        hand = pokerHand.getHandType();
        boolean isFiveOfAKind = hand == HandType.HAND_FIVE_OF_A_KIND;
        assertTrue(pokerHand.getGameName() + " failed Five of a Kind", isFiveOfAKind);

        pokerHand.clearHand();
        makeThreeOfAKind(pokerHand);
        pokerHand.replaceCard(0, deuce1);
        hand = pokerHand.getHandType();
        boolean isThreeOfAKind = hand == HandType.HAND_THREE_OF_A_KIND;
        assertTrue(pokerHand.getGameName() + " failed Three of a Kind with Wilds", isThreeOfAKind);

        // check payout of previous hand
        //int payout = pokerHand.getPayout(1);
        //assertEquals(pokerHand.getGameName() + " failed payout test", 1, payout);
    }

    private void testJokerPokerPokerHand() {
        JokerPokerPokerHand pokerHand = new JokerPokerPokerHand(getContext());
        int testCount = testPokerHand(pokerHand); // test standard hands without wild cards
        assertEquals(pokerHand.getGameName() + " failed test count", 8, testCount);

        PokerCard joker = new PokerCard(Rank.JOKER, null);
        joker.setWild(true);

        makeKingsOrBetter(pokerHand);
        pokerHand.replaceCard(4, joker);
        HandType hand = pokerHand.getHandType();
        boolean isKingsOrBetter = hand == HandType.HAND_KINGS_OR_BETTER;
        assertTrue(pokerHand.getGameName() + " failed Kings or Better", isKingsOrBetter);

        pokerHand.clearHand();
        makeNaturalRoyalFlush(pokerHand);
        pokerHand.replaceCard(1, joker);
        hand = pokerHand.getHandType();
        boolean isWildRoyalFlush = hand == HandType.HAND_WILD_ROYAL_FLUSH;
        assertTrue(pokerHand.getGameName() + " failed Joker Royal Flush", isWildRoyalFlush);

        pokerHand.clearHand();
        makeFullHouse(pokerHand);
        pokerHand.replaceCard(2, joker);
        hand = pokerHand.getHandType();
        boolean isFullHouse = hand == HandType.HAND_FULL_HOUSE;
        assertTrue(pokerHand.getGameName() + " failed Full House with Joker: " + getContext().getResources().getString(hand.getHandResourceID()), isFullHouse);

        pokerHand.clearHand();
        makeStraight(pokerHand);
        pokerHand.replaceCard(4, joker);
        hand = pokerHand.getHandType();
        boolean isStraight = hand == HandType.HAND_STRAIGHT;
        assertTrue(pokerHand.getGameName() + " failed Straight with Joker: " + getContext().getResources().getString(hand.getHandResourceID()), isStraight);

        pokerHand.clearHand();
        makeFlush(pokerHand);
        pokerHand.replaceCard(4, joker);
        hand = pokerHand.getHandType();
        boolean isFlush = hand == HandType.HAND_FLUSH;
        assertTrue(pokerHand.getGameName() + " failed Flush with Joker: " + getContext().getResources().getString(hand.getHandResourceID()), isFlush);

        pokerHand.clearHand();
        makeThreeOfAKind(pokerHand);
        pokerHand.replaceCard(3, joker);
        hand = pokerHand.getHandType();
        boolean isThreeOfAKind = hand == HandType.HAND_THREE_OF_A_KIND;
        assertTrue(pokerHand.getGameName() + " failed Three of a Kind with Joker", isThreeOfAKind);

        pokerHand.clearHand();
        makeFourOfAKind(pokerHand);
        pokerHand.replaceCard(1, joker);
        hand = pokerHand.getHandType();
        boolean isFiveOfAKind = hand == HandType.HAND_FIVE_OF_A_KIND;
        assertTrue(pokerHand.getGameName() + " failed Five of a Kind with Joker", isFiveOfAKind);
    }

    /** Tests some standard handTypes if they are in p's payout table.
     * Returns number of tests performed. */
    private int testPokerHand(PokerHand p) {
        int testCount = 0; // Count hand types tested

        if(p.isValidHandType(HandType.HAND_FULL_HOUSE)) {
            makeFullHouse(p);
            HandType hand = p.getHandType();
            boolean isFullHouse = hand == HandType.HAND_FULL_HOUSE;
            assertTrue(p.getGameName() + " failed Full House", isFullHouse);
            ++testCount;
        }

        if (p.isValidHandType(HandType.HAND_TWO_PAIRS)) {
            p.clearHand();
            makeTwoPairs(p);
            HandType hand = p.getHandType();
            boolean isTwoPairs = hand == HandType.HAND_TWO_PAIRS;
            assertTrue(p.getGameName() + " failed Two Pairs", isTwoPairs);
            ++testCount;
        }

        if (p.isValidHandType(HandType.HAND_ROYAL_FLUSH)) {
            p.clearHand();
            makeNaturalRoyalFlush(p);
            HandType hand = p.getHandType();
            boolean isSameSuit = p.allSameSuit();
            assertTrue(p.getGameName() + " failed allSameSuit test", isSameSuit);
            boolean isStraight = p.isStraight();
            assertTrue(p.getGameName() + " failed isStraight test", isStraight);
            boolean isRoyalFlush = hand == HandType.HAND_ROYAL_FLUSH;
            assertTrue(p.getGameName() + " failed Royal Flush", isRoyalFlush);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_STRAIGHT_FLUSH)) {
            p.clearHand();
            makeStraightFlush(p);
            HandType hand = p.getHandType();
            boolean isStraightFlush = hand == HandType.HAND_STRAIGHT_FLUSH;
            assertTrue(p.getGameName() + " failed Straight Flush", isStraightFlush);
            ++testCount;
        }

        /** Tests a straight with two different hands... one with an Ace and one without an Ace. */
        if (p.isValidHandType(HandType.HAND_STRAIGHT)) {
            p.clearHand();
            makeStraight(p);
            HandType hand = p.getHandType();
            boolean isStraight = hand == HandType.HAND_STRAIGHT;
            assertTrue(p.getGameName() + " failed Straight", isStraight);

            p.clearHand();
            makeStraightWithAce(p);
            hand = p.getHandType();
            isStraight = hand == HandType.HAND_STRAIGHT;
            assertTrue(p.getGameName() + " failed Straight (with Ace)", isStraight);

            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_FLUSH)) {
            p.clearHand();
            makeFlush(p);
            HandType hand = p.getHandType();
            boolean isFlush = hand == HandType.HAND_FLUSH;
            assertTrue(p.getGameName() + " failed Flush", isFlush);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_THREE_OF_A_KIND)) {
            p.clearHand();
            makeThreeOfAKind(p);
            HandType hand = p.getHandType();
            boolean isThreeOfAKind = hand == HandType.HAND_THREE_OF_A_KIND;
            assertTrue(p.getGameName() + " failed Three of a Kind", isThreeOfAKind);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_FOUR_OF_A_KIND)) {
            p.clearHand();
            makeFourOfAKind(p);
            HandType hand = p.getHandType();
            boolean isFourOfAKind = hand == HandType.HAND_FOUR_OF_A_KIND;
            assertTrue(p.getGameName() + " failed Four of a Kind", isFourOfAKind);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_JACKS_OR_BETTER)) {
            p.clearHand();
            makeJacksOrBetter(p);
            HandType hand = p.getHandType();
            boolean isJacksOrBetter = hand == HandType.HAND_JACKS_OR_BETTER;
            assertTrue(p.getGameName() + " failed Jacks or Better", isJacksOrBetter);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_FOUR_ACES)) {
            p.clearHand();
            makeFourAces(p);
            HandType hand = p.getHandType();
            boolean isFourAces = hand == HandType.HAND_FOUR_ACES;
            assertTrue(p.getGameName() + " failed Four Aces", isFourAces);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_FOUR_FACES)) {
            p.clearHand();
            makeFourFaces(p);
            HandType hand = p.getHandType();
            boolean isFourFaces = hand == HandType.HAND_FOUR_FACES;
            assertTrue(p.getGameName() + " failed Four Faces", isFourFaces);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_FOUR_2_TO_4)) {
            p.clearHand();
            makeFour2to4(p);
            HandType hand = p.getHandType();
            boolean isFour2to4 = hand == HandType.HAND_FOUR_2_TO_4;
            assertTrue(p.getGameName() + " failed Four 2 to 4", isFour2to4);
            ++testCount;

            p.clearHand();
            makeFour5toK(p);
            hand = p.getHandType();
            boolean isFour5toK = hand == HandType.HAND_FOUR_5_TO_K;
            assertTrue(p.getGameName() + " failed Four 5 to K", isFour5toK);
            ++testCount;
        }

        if(p.isValidHandType(HandType.HAND_FOUR_ACES_AND_2_TO_4)) {
            p.clearHand();
            makeFourAcesAnd2to4(p);
            HandType hand = p.getHandType();
            boolean isFourAcesAnd2to4 = hand == HandType.HAND_FOUR_ACES_AND_2_TO_4;
            assertTrue(p.getGameName() + " failed Four Aces and 2 to 4", isFourAcesAnd2to4);
            ++testCount;

            p.clearHand();
            makeFour2to4AndAce(p);
            hand = p.getHandType();
            boolean isFour2to4AndAce = hand == HandType.HAND_FOUR_2_TO_4_AND_ACE;
            assertTrue(p.getGameName() + " failed Four 2 to 4 and Ace", isFour2to4AndAce);
            ++testCount;
        }

        p.clearHand();
        int size = p.size();
        assertEquals(0, size);

        return testCount;
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

    private void makeStraightWithAce(PokerHand p) {
        p.addCard(new PokerCard(Rank.DEUCE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.THREE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.FIVE, Suit.SPADES));
        p.addCard(new PokerCard(Rank.ACE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.DIAMONDS));
    }

    private void makeFlush(PokerHand p) {
        p.addCard(new PokerCard(Rank.THREE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.QUEEN, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.FIVE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.SIX, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.NINE, Suit.CLUBS));
    }

    private void makeStraightFlush(PokerHand p) {
        p.addCard(new PokerCard(Rank.SIX, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.EIGHT, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.SEVEN, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.TEN, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.NINE, Suit.HEARTS));
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

    private void makeKingsOrBetter(PokerHand p) {
        p.addCard(new PokerCard(Rank.SEVEN, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.QUEEN, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.KING, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.KING, Suit.DIAMONDS));
    }

    private void makeFourAces(PokerHand p) {
        p.addCard(new PokerCard(Rank.ACE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.SIX, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.ACE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.ACE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.ACE, Suit.SPADES));
    }

    private void makeFourAcesAnd2to4(PokerHand p) {
        p.addCard(new PokerCard(Rank.ACE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.ACE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.ACE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.ACE, Suit.SPADES));
    }

    private void makeFour2to4(PokerHand p) {
        p.addCard(new PokerCard(Rank.THREE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.THREE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.THREE, Suit.SPADES));
        p.addCard(new PokerCard(Rank.SEVEN, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.THREE, Suit.CLUBS));
    }

    private void makeFour2to4AndAce(PokerHand p) {
        p.addCard(new PokerCard(Rank.FOUR, Suit.SPADES));
        p.addCard(new PokerCard(Rank.FOUR, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.ACE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.FOUR, Suit.CLUBS));
    }

    private void makeFour5toK(PokerHand p) {
        p.addCard(new PokerCard(Rank.NINE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.THREE, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.NINE, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.NINE, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.NINE, Suit.SPADES));
    }

    private void makeFourFaces(PokerHand p) {
        p.addCard(new PokerCard(Rank.KING, Suit.HEARTS));
        p.addCard(new PokerCard(Rank.KING, Suit.DIAMONDS));
        p.addCard(new PokerCard(Rank.KING, Suit.CLUBS));
        p.addCard(new PokerCard(Rank.KING, Suit.SPADES));
        p.addCard(new PokerCard(Rank.FIVE, Suit.SPADES));
    }
}