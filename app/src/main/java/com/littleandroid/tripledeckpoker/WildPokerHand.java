package com.littleandroid.tripledeckpoker;

/**
 * Created by Kristen on 1/12/2015.
 */
public abstract class WildPokerHand extends PokerHand {
    public WildPokerHand() {

    }

    public boolean isWild(int cardNum) {
        return getCard(cardNum).isWild();
    }

    public int getWildCount() {
        int wildCount = 0;
        for(int i = 0; i < size(); ++i) {
            if(isWild(i)) {
                ++wildCount;
            }
        }
        return wildCount;
    }

    @Override
    public boolean allSameSuit() {
        boolean same = true;
        int firstNonWildIndex = -1;
        for(int i = 0; i < size() && same; ++i) {
            if(!isWild(i)) {
                if(firstNonWildIndex == -1) {
                    firstNonWildIndex = i;
                }
                else if(getCardSuit(i) != getCardSuit(firstNonWildIndex)) {
                    same = false;
                }
            }

        }
        return same;
    }

    public boolean isNaturalRoyalFlush() {
        boolean naturalRoyalFlush = false;
        if(getWildCount() == 0 && allSameSuit() &&
                isInHand(Rank.TEN) && isInHand(Rank.JACK) && isInHand(Rank.QUEEN) && isInHand(Rank.KING) && isInHand(Rank.ACE)) {
            naturalRoyalFlush = true;
        }
        return naturalRoyalFlush;
    }

    public boolean isRoyalFlush() {
        boolean royalFlush = false;
        int wilds = getWildCount();
        if(wilds > 0 && allSameSuit()) {
            if(!isInHand(Rank.TEN)) {
                --wilds;
            }
            if(!isInHand(Rank.JACK)) {
                --wilds;
            }
            if(!isInHand(Rank.QUEEN)) {
                --wilds;
            }
            if(!isInHand(Rank.KING)) {
                --wilds;
            }
            if(!isInHand(Rank.ACE)) {
                --wilds;
            }
            if(wilds == 0) {
                royalFlush = true;
            }
        }
        return royalFlush;
    }

    public boolean isFiveOfAKind() {
        boolean fiveOfAKind = false;
        int wilds = getWildCount();
        if(wilds > 0) {
            for (int i = 0; i < size() && !fiveOfAKind; ++i) {
                if (!isWild(i)) {
                    int count = getRankCount(getCardRank(i));
                    if (count + wilds == 5) {
                        fiveOfAKind = true;
                    }
                }
            }
        }
        return fiveOfAKind;
    }

    @Override
    public boolean isFourOfAKind() {
        boolean fourOfAKind = false;
        int wilds = getWildCount();
        for(int i = 0; i < size() && !fourOfAKind; ++i) {
            if(!isWild(i)) {
                int count = getRankCount(getCardRank(i));
                if (count + wilds == 4) {
                    fourOfAKind = true;
                }
            }
        }
        return fourOfAKind;
    }

    @Override
    public boolean isThreeOfAKind() {
        boolean threeOfAKind = false;
        int wilds = getWildCount();
        for(int i = 0; i < size() && !threeOfAKind; ++i) {
            if(!isWild(i)) {
                int count = getRankCount(getCardRank(i));
                if(count + wilds == 3) {
                    threeOfAKind = true;
                }
            }
        }
        return threeOfAKind;
    }

    @Override
    public boolean isTwoPairs() {
        boolean twoPairs = super.isTwoPairs();
        if(!twoPairs && getWildCount() == 1) {
            // If we have one wild card, then we only need to find 1 pair.
            for(int i = 0; (i < getSortedSize() - 1) && !twoPairs; ++i) {
                if(getSortedCardRank(i) == getSortedCardRank(i+1)) {
                    twoPairs = true;
                }
            }
        }
        return twoPairs;

    }

    @Override
    public boolean isStraight() {
        boolean straight = false;
        int lowIndex = -1;
        int highIndex = -1;

        for(int i = 0; i < size() && lowIndex < 0; ++i) {
            PokerCard c = getSortedCard(i);
            if(c.isWild() == false) {
                lowIndex = i;
            }
        }

        for(int j = size() - 1; j >= 0 && highIndex < 0; --j) {
            PokerCard c = getSortedCard(j);
            if(c.isWild() == false) {
                highIndex = j;
            }
        }

        if(lowIndex == highIndex) {
            if(getWildCount() == PLAY_SIZE - 1) {
                straight = true;
            }
        }
        else if(getSortedCardRank(lowIndex) != Rank.ACE) {
            /* assumes only wild cards will be deuces or jokers, which will be at either end of a sorted hand. */
            int wilds = getWildCount();
            int count = highIndex - lowIndex;
            for (int k = 1; k < count; ++k) {
                if (getSortedCardRank(lowIndex).ordinal() + k != getSortedCardRank(lowIndex + k).ordinal()) {
                    --wilds;
                }
            }
            if (wilds == 0) {
                straight = true;
            }
        }
        else {
            int wilds = getWildCount();
            if(getSortedCardRank(highIndex).ordinal() <= Rank.FIVE.ordinal()) {
                if(!isInHand(Rank.DEUCE)) {
                    --wilds;
                }
                if(!isInHand(Rank.THREE)) {
                    --wilds;
                }
                if(!isInHand(Rank.FOUR)) {
                    --wilds;
                }
                if(!isInHand(Rank.FIVE)) {
                    --wilds;
                }
            }
            else {
                if(!isInHand(Rank.TEN)) {
                    --wilds;
                }
                if(!isInHand(Rank.JACK)) {
                    --wilds;
                }
                if(!isInHand(Rank.QUEEN)) {
                    --wilds;
                }
                if(!isInHand(Rank.KING)) {
                    --wilds;
                }
            }
            if(wilds == 0) {
                straight = true;
            }

        }

        return straight;
    }

    @Override
    public boolean isFullHouse() {
        boolean fullHouse = false;
        int lowIndex = -1;
        int highIndex = -1;

        for(int i = 0; i < size() && lowIndex < 0; ++i) {
            PokerCard c = getSortedCard(i);
            if(c.isWild() == false) {
                lowIndex = i;
            }
        }

        for(int j = size() - 1; j >= 0 && highIndex < 0; --j) {
            PokerCard c = getSortedCard(j);
            if(c.isWild() == false) {
                highIndex = j;
            }
        }

        if(lowIndex != highIndex) {
            if(getRankCount(getSortedCardRank(lowIndex)) + getRankCount(getSortedCardRank(highIndex)) + getWildCount() == PLAY_SIZE) {
                fullHouse = true;
            }
        }

        return fullHouse;
    }

    public void holdWilds() {
        for(int i = 0; i < size(); ++i) {
            if(getCard(i).isWild()) {
                getCard(i).setHold(true);
            }
        }
    }

    @Override
    public void autoHold() {
        holdWilds();
        if(mHandType == HandType.HAND_WILD_ROYAL_FLUSH ||
                mHandType == HandType.HAND_FIVE_OF_A_KIND ||
                mHandType == HandType.HAND_FOUR_DEUCES) {
            holdAll();
        }
        else {
            super.autoHold();
        }
    }
}
