package com.littleandroid.tripledeckpoker;

import android.content.Context;
import android.content.res.Resources;

import java.util.EnumMap;


/**
 * Created by Kristen on 1/13/2015.
 */
public class DeucesWildPokerHand extends WildPokerHand {

    private Context mContext;
    private EnumMap<HandType, int[]> mPayoutTable = new EnumMap<HandType,int[]>(HandType.class);

    public DeucesWildPokerHand(Context context) {
        mContext = context.getApplicationContext();  // exists for life of app
        Resources r = mContext.getResources();

        mGameName = r.getString(R.string.game_deuces_wild);

        mPayoutTable.put(HandType.HAND_LOSE, r.getIntArray(R.array.payout_lose));
        mPayoutTable.put(HandType.HAND_ROYAL_FLUSH, r.getIntArray(R.array.payout_deuces_royal_flush));
        mPayoutTable.put(HandType.HAND_FOUR_DEUCES, r.getIntArray(R.array.payout_deuces_four_deuces));
        mPayoutTable.put(HandType.HAND_WILD_ROYAL_FLUSH, r.getIntArray(R.array.payout_deuces_wild_royal_flush));
        mPayoutTable.put(HandType.HAND_FIVE_OF_A_KIND, r.getIntArray(R.array.payout_deuces_five_of_a_kind));
        mPayoutTable.put(HandType.HAND_STRAIGHT_FLUSH, r.getIntArray(R.array.payout_deuces_straight_flush));
        mPayoutTable.put(HandType.HAND_FOUR_OF_A_KIND, r.getIntArray(R.array.payout_deuces_four_of_a_kind));
        mPayoutTable.put(HandType.HAND_FULL_HOUSE, r.getIntArray(R.array.payout_deuces_full_house));
        mPayoutTable.put(HandType.HAND_FLUSH, r.getIntArray(R.array.payout_deuces_flush));
        mPayoutTable.put(HandType.HAND_STRAIGHT, r.getIntArray((R.array.payout_deuces_straight)));
        mPayoutTable.put(HandType.HAND_THREE_OF_A_KIND, r.getIntArray(R.array.payout_deuces_three_of_a_kind));

    }

    public HandType getHandType() {
        if (mHandType == HandType.HAND_UNKNOWN) {
            sort();
            if(isNaturalRoyalFlush()) {
                mHandType = HandType.HAND_ROYAL_FLUSH;
            }
            else if(getWildCount() == 4) {
                mHandType = HandType.HAND_FOUR_DEUCES;
            } else if(isRoyalFlush()) {
                mHandType = HandType.HAND_WILD_ROYAL_FLUSH;
            } else if(isFiveOfAKind()) {
                mHandType = HandType.HAND_FIVE_OF_A_KIND;
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
