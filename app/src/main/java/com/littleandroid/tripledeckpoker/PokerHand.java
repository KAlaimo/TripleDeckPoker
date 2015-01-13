package com.littleandroid.tripledeckpoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Kristen on 1/12/2015.
 */
public abstract class PokerHand {

    public static final int PLAY_SIZE = 5;
    public static final int MAX_BET = 5;
    public static final int HAND_UNKNOWN = -1;
    public static final int HAND_NO_WIN = 0;

    private ArrayList<PokerCard> mHand;
    private ArrayList<PokerCard> mSortedHand;
    protected int mHandType;
    protected int mWildCount;

    public PokerHand() {
        mHand = new ArrayList<PokerCard>(PLAY_SIZE);
        mHandType = HAND_UNKNOWN;
        mWildCount = 0;
    }

    public void addCard(PokerCard c) {
        mHand.add(c);
        mHandType = HAND_UNKNOWN;
    }

    public void replaceCard(int cardNum, PokerCard c) {
        mHand.set(cardNum, c);
        mHandType = HAND_UNKNOWN;
    }

    public PokerCard getCard(int cardNum) {
        return mHand.get(cardNum);
    }

    public Rank getCardRank(int cardNum) {
        return getCard(cardNum).getRank();
    }

    public Suit getCardSuit(int cardNum) {
        return getCard(cardNum).getSuit();
    }

    public boolean isHeld(int cardNum) {
        return getCard(cardNum).isHeld();
    }

    public void toggleHold(int cardNum) {
        getCard(cardNum).toggleHold();
    }

    public boolean isWild(int cardNum) {
        return getCard(cardNum).isWild();
    }

    public int getWildCount() {
        mWildCount = 0;
        for(PokerCard c : mHand) {
            if(c.isWild()) {
                ++mWildCount;
            }
        }
        return mWildCount;
    }

    public boolean isInHand (PokerCard c) {
        return mHand.contains(c);
    }

    public boolean isInHand(Rank r) {
        boolean exists = false;
        for(int i = 0; i < mHand.size() && !exists; ++i) {
            if(getCardRank(i) == r) {
                exists = true;
            }
        }
        return exists;
    }

    public void clearHand() {
        mHand.clear();
        if(mSortedHand != null) {
            mSortedHand.clear();
            mSortedHand = null;
        }
        mWildCount = 0;
        mHandType = HAND_UNKNOWN;
    }

    public int size() {
        return mHand.size();
    }

    public void sort() {
        mSortedHand = new ArrayList<PokerCard>();
        mSortedHand.addAll(mHand);
        Collections.sort(mSortedHand, new Comparator<PokerCard>() {
            @Override public int compare(PokerCard lhs, PokerCard rhs) {
                return (lhs.getRank().ordinal() - rhs.getRank().ordinal());
            }

        });
    }

    public PokerCard getSortedCard(int cardNum) {
        if(mSortedHand == null) {
            sort();
        }
        return mSortedHand.get(cardNum);
    }

    public Rank getSortedCardRank(int cardNum) {
        return getSortedCard(cardNum).getRank();
    }

    public Suit getSortedCardSuit(int cardNum) {
        return getSortedCard(cardNum).getSuit();
    }

    public int getSortedSize() {
        if(mSortedHand == null) {
            sort();
        }
        return mSortedHand.size();
    }

    public boolean allSameSuit() {
        boolean same = false;
        Suit suit = getSortedCardSuit(0);
        for(int i = 1; i < getSortedSize() && same; ++i) {
            if(getSortedCardSuit(i) != suit) {
                same = false;
            }
        }
        return same;
    }

    public boolean isStraight() {
        boolean straight = true;
        for(int i = 0; (i < getSortedSize() - 1) && straight; ++i) {
            if(i == 0 && getSortedCardRank(i) == Rank.ACE) {
                /* Ace is a special case, can have A23456 or 10JQKA (sorted as A10JQK) as a straight */
                if(getSortedCardRank(i+1) != Rank.TEN && getSortedCardRank(i+1) != Rank.DEUCE) {
                    straight = false;
                }
            }
            else if (getSortedCardRank(i).ordinal() + 1 != getSortedCardRank(i+1).ordinal()) {
                    straight = false;
            }
        }
        return straight;
    }

    public boolean isFullHouse() {
        boolean fullHouse = false;
        if(getSortedSize() >= 5) {
            if(getSortedCardRank(0) == getSortedCardRank(1) && getSortedCardRank(1) == getSortedCardRank(2) &&
                    getSortedCardRank(3) == getSortedCardRank(4)) {
                fullHouse = true;
            }
            else if(getSortedCardRank(0) == getSortedCardRank(1) &&
                        getSortedCardRank(2) == getSortedCardRank(3) && getSortedCardRank(3) == getSortedCardRank(4)) {
                fullHouse = true;
            }
        }
        return fullHouse;
    }

    public boolean isFourOfAKind() {
        return isXOfAKind(4);
    }

    public Rank fourKinds() {
        return findXOfAKind(4);
    }

    public boolean isThreeOfAKind() {
        return isXOfAKind(3);
    }

    public Rank threeKinds() {
        return findXOfAKind(3);
    }

    public Rank findXOfAKind(int x) {
        Rank r = null;
        if(getSortedSize() >= x) {
            for(int i = 0; i <= getSortedSize() - x && (r == null); ++i) {
                boolean same = true;
                for(int j = i + 1; (j < i + x) && same; ++j) {
                    if(getSortedCardRank(i) != getSortedCardRank(j)) {
                        same = false;
                    }
                }
                if(same) {
                    r = getCardRank(i);
                }
            }
        }
        return r;
    }

    public boolean isXOfAKind(int x) {
        Rank r = findXOfAKind(x);
        return (r != null);
    }

    public boolean isTwoPairs() {
        int pairCount = 0;
        int i = 0;
        while((i < getSortedSize()-1) && (pairCount < 2)) {
            if(getSortedCardRank(i) == getSortedCardRank(i+1)) {
                pairCount = pairCount + 1;
                i = i + 2;
            }
            else {
                i = i + 1;
            }
        }
        return (pairCount == 2);
    }

    public boolean isJacksOrBetter() {
        boolean jacksOrBetter = false;
        for(int i = 0; (i < getSortedSize()-1) && !jacksOrBetter; ++i) {
            if(getSortedCard(i).isAce() || getSortedCard(i).isFace()) {
                if(getSortedCardRank(i) == getSortedCardRank(i+1)) {
                    jacksOrBetter = true;
                }
            }
        }
        return jacksOrBetter;
    }

    public void holdAll() {
        for(PokerCard c : mHand) {
            c.setHold(true);
        }
    }

    public void holdRank(Rank r) {
        for(PokerCard c : mHand) {
            if(c.getRank() == r) {
                c.setHold(true);
            }
        }
    }

    public void holdPairs() {
        for(int i = 0; i < getSortedSize() - 1; ++i) {
            if(getSortedCardRank(i) == getSortedCardRank(i+1)) {
                getSortedCard(i).setHold(true);
                getSortedCard(i+1).setHold(true);
            }
        }
    }

    public abstract int getHandType();
    public abstract int getPayout(int bet);
    public abstract String getGameName();
    public abstract void autoHold();

}
