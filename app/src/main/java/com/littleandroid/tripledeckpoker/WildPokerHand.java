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

    private PokerCard getLowCard() {
        PokerCard lowCard = null;
        for(int i = 0; i < size() && lowCard == null; ++i) {
            PokerCard c = getSortedCard(i);
            if(c.isWild() == false) {
                lowCard = c;
            }
        }
        return lowCard;
    }

    private PokerCard getHighCard() {
        PokerCard highCard = null;
        for(int j = size() - 1; j >= 0 && highCard == null; --j) {
            PokerCard c = getSortedCard(j);
            if(c.isWild() == false) {
                highCard = c;
            }
        }
        return highCard;
    }

    @Override
    public boolean isStraight() {
        boolean straight = true;
        int wildCount = getWildCount();

        if(wildCount == 0) {
            straight = super.isStraight();
        }
        else {
            PokerCard lowCard = getLowCard();
            PokerCard highCard = getHighCard();

            if (lowCard != null && highCard != null) {

                boolean aceIsHigh = (lowCard.getRank() == Rank.ACE && highCard.getRank().ordinal() >= Rank.TEN.ordinal());
                if(aceIsHigh) {
                    // if aceIsHigh, then we need 10JQKA
                    int a = getRankCount(Rank.ACE);
                    int t = getRankCount(Rank.TEN);
                    int j = getRankCount(Rank.JACK);
                    int q = getRankCount(Rank.QUEEN);
                    int k = getRankCount(Rank.KING);
                    if(a > 1 || t > 1 || j > 1 || q > 1 || k > 1) {
                        straight = false;
                    }
                    else if(a + t + j + q + k +wildCount == PLAY_SIZE) {
                        straight = true;
                    }
                }
                else if(highCard.getRank().ordinal() - lowCard.getRank().ordinal() >= PLAY_SIZE) {
                    straight = false;
                }
                else {
                    int count = 0;
                    for(int i = 0; i < size() && straight; ++i) {
                        PokerCard c = getSortedCard(i);
                        if(c.isWild() == false) {
                            int rankCount = getRankCount(c.getRank());
                            if(rankCount > 1) {
                                straight = false;
                            }
                            else if(rankCount == 1) {
                                ++count;
                            }
                        }
                    }

                    if(count + wildCount != PLAY_SIZE) {
                        straight = false;
                    }
                }
            }
            else {
                straight = false;
            }
        }

        return straight;
    }

    @Override
    public boolean isFullHouse() {
        boolean fullHouse = false;
        PokerCard lowCard = getLowCard();
        PokerCard highCard = getHighCard();

        if(lowCard != null && highCard != null) {
            if (lowCard.getRank() != highCard.getRank()) {
                if (getRankCount(lowCard.getRank()) + getRankCount(highCard.getRank()) + getWildCount() == PLAY_SIZE) {
                    fullHouse = true;
                }
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
