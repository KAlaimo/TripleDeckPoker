package com.littleandroid.tripledeckpoker;

import android.content.Context;
import android.content.res.Resources;

import java.util.EnumMap;

/**
 * Created by Kristen on 1/15/2015.
 */
public class DoubleDoubleBonusPokerHand extends PokerHand {
    private Context mContext;
    private EnumMap<HandType, int[]> mPayoutTable = new EnumMap<HandType,int[]>(HandType.class);

    public DoubleDoubleBonusPokerHand (Context context) {
        mContext = context.getApplicationContext();  // exists for life of app
        Resources r = mContext.getResources();

        mGameName = r.getString(R.string.game_double_double_bonus);

        mPayoutTable.put(HandType.HAND_LOSE, r.getIntArray(R.array.payout_lose));
        mPayoutTable.put(HandType.HAND_ROYAL_FLUSH, r.getIntArray(R.array.payout_ddb_royal_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT_FLUSH, r.getIntArray(R.array.payout_ddb_straight_flush));
        mPayoutTable.put(HandType.HAND_FOUR_ACES_AND_2_TO_4, r.getIntArray(R.array.payout_ddb_four_aces_234));
        mPayoutTable.put(HandType.HAND_FOUR_2_TO_4_AND_ACE, r.getIntArray(R.array.payout_ddb_four_234_ace));
        mPayoutTable.put(HandType.HAND_FOUR_ACES, r.getIntArray(R.array.payout_ddb_four_aces));
        mPayoutTable.put(HandType.HAND_FOUR_2_TO_4, r.getIntArray(R.array.payout_ddb_four_2_to_4));
        mPayoutTable.put(HandType.HAND_FOUR_5_TO_K, r.getIntArray(R.array.payout_ddb_four_5_to_k));
        mPayoutTable.put(HandType.HAND_FULL_HOUSE, r.getIntArray(R.array.payout_ddb_full_house));
        mPayoutTable.put(HandType.HAND_FLUSH, r.getIntArray(R.array.payout_ddb_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT, r.getIntArray(R.array.payout_ddb_straight));
        mPayoutTable.put(HandType.HAND_THREE_OF_A_KIND, r.getIntArray(R.array.payout_ddb_three_of_a_kind));
        mPayoutTable.put(HandType.HAND_TWO_PAIRS, r.getIntArray(R.array.payout_ddb_two_pairs));
        mPayoutTable.put(HandType.HAND_JACKS_OR_BETTER, r.getIntArray(R.array.payout_ddb_jacks_or_better));

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
                switch(fourKinds()) {
                    case ACE:
                        Rank r = getSortedCardRank(4);
                        if(r == Rank.DEUCE || r == Rank.THREE || r == Rank.FOUR) {
                            mHandType = HandType.HAND_FOUR_ACES_AND_2_TO_4;
                        }
                        else {
                            mHandType = HandType.HAND_FOUR_ACES;
                        }
                        break;
                    case DEUCE:
                    case THREE:
                    case FOUR:
                        if(getSortedCardRank(0) == Rank.ACE) {
                            mHandType = HandType.HAND_FOUR_2_TO_4_AND_ACE;
                        }
                        else {
                            mHandType = HandType.HAND_FOUR_2_TO_4;
                        }
                        break;
                    default:
                        mHandType = HandType.HAND_FOUR_5_TO_K;
                        break;

                }
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
