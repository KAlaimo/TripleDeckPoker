package com.littleandroid.tripledeckpoker;

import android.content.Context;
import android.content.res.Resources;

import java.util.EnumMap;

/**
 * Created by Kristen on 1/15/2015.
 */
public class JacksOrBetterPokerHand extends PokerHand {
    private Context mContext;
    private EnumMap<HandType, int[]> mPayoutTable = new EnumMap<HandType,int[]>(HandType.class);

    public JacksOrBetterPokerHand(Context context) {
        mContext = context.getApplicationContext();  // exists for life of app
        Resources r = mContext.getResources();

        mGameName = r.getString(R.string.game_jacks_or_better);

        mPayoutTable.put(HandType.HAND_LOSE, r.getIntArray(R.array.payout_lose));
        mPayoutTable.put(HandType.HAND_ROYAL_FLUSH, r.getIntArray(R.array.payout_jacks_royal_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT_FLUSH, r.getIntArray(R.array.payout_jacks_straight_flush));
        mPayoutTable.put(HandType.HAND_FOUR_OF_A_KIND, r.getIntArray(R.array.payout_jacks_four_of_a_kind));
        mPayoutTable.put(HandType.HAND_FULL_HOUSE, r.getIntArray(R.array.payout_jacks_full_house));
        mPayoutTable.put(HandType.HAND_FLUSH, r.getIntArray(R.array.payout_jacks_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT, r.getIntArray(R.array.payout_jacks_straight));
        mPayoutTable.put(HandType.HAND_THREE_OF_A_KIND, r.getIntArray(R.array.payout_jacks_three_of_kind));
        mPayoutTable.put(HandType.HAND_TWO_PAIRS, r.getIntArray(R.array.payout_jacks_two_pairs));
        mPayoutTable.put(HandType.HAND_JACKS_OR_BETTER, r.getIntArray(R.array.payout_jacks_jacks_or_better));
    }

    @Override
    public HandType getHandType() {
        if(mHandType == HandType.HAND_UNKNOWN) {
            sort();
            if(allSameSuit()) {
                if(isStraight()) {
                    if(getSortedCardRank(4) == Rank.KING) {
                        mHandType = HandType.HAND_ROYAL_FLUSH;
                    }
                    else {
                        mHandType = HandType.HAND_STRAIGHT_FLUSH;
                    }
                }
                else {
                    mHandType = HandType.HAND_FLUSH;
                }
            }
            else if(isFourOfAKind()) {
                mHandType = HandType.HAND_FOUR_OF_A_KIND;
            }
            else if(isFullHouse()) {
                mHandType = HandType.HAND_FULL_HOUSE;
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
            else if(isJacksOrBetter()) {
                mHandType = HandType.HAND_JACKS_OR_BETTER;
            }
            else {
                mHandType = HandType.HAND_LOSE;
            }
        }
        return mHandType;
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
}
