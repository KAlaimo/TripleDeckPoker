package com.littleandroid.tripledeckpoker;

import android.content.Context;
import android.content.res.Resources;

import java.util.EnumMap;

/**
 * Created by Kristen on 1/15/2015.
 */
public class JokerPokerPokerHand extends WildPokerHand {
    private Context mContext;
    private EnumMap<HandType, int[]> mPayoutTable = new EnumMap<HandType,int[]>(HandType.class);

    public JokerPokerPokerHand(Context context) {
        mContext = context.getApplicationContext();  // exists for life of app
        Resources r = mContext.getResources();

        mGameName = r.getString(R.string.game_joker_poker);

        mPayoutTable.put(HandType.HAND_LOSE, r.getIntArray(R.array.payout_lose));
        mPayoutTable.put(HandType.HAND_ROYAL_FLUSH, r.getIntArray(R.array.payout_joker_royal_flush));
        mPayoutTable.put(HandType.HAND_FIVE_OF_A_KIND, r.getIntArray(R.array.payout_joker_five_of_a_kind));
        mPayoutTable.put(HandType.HAND_WILD_ROYAL_FLUSH, r.getIntArray(R.array.payout_joker_wild_royal_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT_FLUSH, r.getIntArray(R.array.payout_joker_straight_flush));
        mPayoutTable.put(HandType.HAND_FOUR_OF_A_KIND, r.getIntArray(R.array.payout_joker_four_of_a_kind));
        mPayoutTable.put(HandType.HAND_FULL_HOUSE, r.getIntArray(R.array.payout_joker_full_house));
        mPayoutTable.put(HandType.HAND_FLUSH, r.getIntArray(R.array.payout_joker_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT, r.getIntArray(R.array.payout_joker_straight));
        mPayoutTable.put(HandType.HAND_THREE_OF_A_KIND, r.getIntArray(R.array.payout_joker_three_of_a_kind));
        mPayoutTable.put(HandType.HAND_TWO_PAIRS, r.getIntArray(R.array.payout_joker_two_pairs));
        mPayoutTable.put(HandType.HAND_KINGS_OR_BETTER, r.getIntArray(R.array.payout_joker_kings_or_better));
    }


    public boolean isKingsOrBetter() {
        int k = getRankCount(Rank.KING);
        int a = getRankCount(Rank.ACE);
        int w = getWildCount();

        return (k+a+w == size());

    }

    private void holdKingsOrBetter() {
        holdRank(Rank.KING);
        holdRank(Rank.ACE);
    }

    @Override
    public void autoHold() {
        super.autoHold();
        if(mHandType == HandType.HAND_KINGS_OR_BETTER) {
            holdKingsOrBetter();
        }
    }

    @Override
    public HandType getHandType() {
        if(mHandType == HandType.HAND_UNKNOWN) {
            sort();
            if(isNaturalRoyalFlush()) {
                mHandType = HandType.HAND_ROYAL_FLUSH;
            }
            else if(isFiveOfAKind()) {
                mHandType = HandType.HAND_FIVE_OF_A_KIND;
            }
            else if(isRoyalFlush()) {
                mHandType = HandType.HAND_WILD_ROYAL_FLUSH;
            }
            else if(allSameSuit() && isStraight()) {
                mHandType = HandType.HAND_STRAIGHT_FLUSH;
            }
            else if(isFourOfAKind()) {
                mHandType = HandType.HAND_FOUR_OF_A_KIND;
            }
            else if(isFullHouse()) {
                mHandType = HandType.HAND_FULL_HOUSE;
            }
            else if(allSameSuit()) {
                mHandType = HandType.HAND_FLUSH;
            }
            else if(isStraight()) {
                mHandType = HandType.HAND_STRAIGHT;
            }
            else if(isThreeOfAKind()) {
                mHandType = HandType.HAND_THREE_OF_A_KIND;
            }
            else if(isTwoPairs()) {
                mHandType = HandType.HAND_TWO_PAIRS;
            }
            else if(isKingsOrBetter()) {
                mHandType = HandType.HAND_KINGS_OR_BETTER;
            }
            else {
                mHandType = HandType.HAND_LOSE;
            }
        }
        return mHandType;
    }

    @Override
    public String getHandTypeString() {
        return mContext.getResources().getString(mHandType.getHandResourceID());
    }

    @Override
    public int getPayout(int bet) {
        int pay = 0;
        if(bet >= 1 && bet <= MAX_BET) {
            int[] arr = mPayoutTable.get(getHandType());
            pay = arr[bet-1];
        }
        return pay;
    }

    @Override
    public boolean isValidHandType(HandType hand) {
        return mPayoutTable.keySet().contains(hand);
    }
}
