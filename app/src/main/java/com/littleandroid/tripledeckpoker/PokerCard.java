package com.littleandroid.tripledeckpoker;

/**
 * Created by Kristen on 1/12/2015.
 */
public class PokerCard {
    private Rank mRank;
    private Suit mSuit;

    private boolean mHold;
    private boolean mWild;

    private int mIndex;

    public static final int RANK_COUNT = Rank.values().length - 1;     /* Do not count Joker in RankCount */
    public static final int SUIT_COUNT = Suit.values().length;

    public PokerCard(Rank rank, Suit suit) {
        mRank = rank;
        mSuit = suit;
        mHold = false;
        mWild = false;

        if(mRank == Rank.JOKER || mSuit == null) {
            mIndex = 1 + (RANK_COUNT * SUIT_COUNT);
        }
        else {
            mIndex = 1 + (mSuit.ordinal() * RANK_COUNT) + mRank.ordinal();
        }
    }

    public Rank getRank() {
        return mRank;
    }

    public Suit getSuit() {
        return mSuit;
    }

    public boolean isHeld() {
        return mHold;
    }

    public void setHold(boolean hold) {
        mHold = hold;
    }

    public void toggleHold() {
        mHold = !mHold;
    }

    public boolean isWild() {
        return mWild;
    }

    public void toggleWild() {
        mWild = !mWild;
    }

    public void setWild(boolean wild) {
        mWild = wild;
    }

    public boolean isDeuce() {
        return (mRank == Rank.DEUCE);
    }

    public boolean isJoker() {
        return (mRank == Rank.JOKER);
    }

    public boolean isAce() {
        return (mRank == Rank.ACE);
    }

    public boolean isFace() {
        return (mRank == Rank.JACK || mRank == Rank.QUEEN || mRank == Rank.KING);
    }

    public int getIndex() {
        return mIndex;
    }

    @Override
    public String toString() {
        return mRank + " of " + mSuit;
    }

    @Override
    public boolean equals (Object o) {

        if(this == o) {
            return true;
        }

        if(!(o instanceof PokerCard)) {
            return false;
        }

        PokerCard c = (PokerCard) o;
        return (this.getRank() == c.getRank() && this.getSuit() == c.getSuit());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = (31 * result) + getIndex();
        return result;
    }


}
