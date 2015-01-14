package com.littleandroid.tripledeckpoker;

import java.lang.Object;
/**
 * Created by Kristen on 1/13/2015.
 */
public enum HandType {
    HAND_LOSE (R.string.hand_lose),
    HAND_ROYAL_FLUSH (R.string.hand_royal_flush),
    HAND_FIVE_OF_A_KIND (R.string.hand_five_of_a_kind),
    HAND_WILD_ROYAL_FLUSH (R.string.hand_wild_royal_flush),
    HAND_STRAIGHT_FLUSH (R.string.hand_straight_flush),
    HAND_FOUR_OF_A_KIND (R.string.hand_four_of_a_kind),
    HAND_FULL_HOUSE (R.string.hand_full_house),
    HAND_FLUSH (R.string.hand_flush),
    HAND_STRAIGHT (R.string.hand_straight),
    HAND_THREE_OF_A_KIND (R.string.hand_three_of_a_kind),
    HAND_TWO_PAIRS (R.string.hand_two_pairs),
    HAND_KINGS_OR_BETTER (R.string.hand_kings_or_better),
    HAND_FOUR_ACES (R.string.hand_four_aces),
    HAND_FOUR_FACES (R.string.hand_four_faces),
    HAND_FOUR_DEUCES (R.string.hand_four_deuces),
    HAND_FOUR_2_TO_4 (R.string.hand_four_2_4),
    HAND_FOUR_5_TO_K (R.string.hand_four_5_k),
    HAND_FOUR_ACES_AND_2_TO_4 (R.string.hand_four_aces_2_4),
    HAND_FOUR_2_TO_4_AND_ACE (R.string.hand_four_2_4_ace),
    HAND_JACKS_OR_BETTER (R.string.hand_jacks_or_better),
    HAND_UNKNOWN (R.string.hand_unknown);

    private int mHandResourceID;

    HandType(int handResID) {
        mHandResourceID = handResID;
    }

    public int getHandResourceID() {
        return mHandResourceID;
    }
}
