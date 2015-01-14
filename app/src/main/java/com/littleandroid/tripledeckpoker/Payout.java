package com.littleandroid.tripledeckpoker;

import android.content.Context;

/**
 * Created by Kristen on 1/13/2015.
 */
public class Payout {
    public static final int MAX_BET = 5;
    private HandType mHandType;
    private String mHandName;
    private int[] mPayouts;


    public Payout(Context context, HandType handType, int payoutResourceID) {

        mHandType = handType;
        mPayouts = context.getResources().getIntArray(payoutResourceID);
        mHandName = context.getResources().getString(mHandType.getHandResourceID());
    }

    public HandType getHandType() {
        return mHandType;
    }

    public String getHandName() {
        return mHandName;
    }

    public int getPayout (int bet) {
        int pay = 0;
        if(bet >= 1 && bet <= MAX_BET) {
            pay = mPayouts[bet-1];
        }
        return pay;
    }
}
