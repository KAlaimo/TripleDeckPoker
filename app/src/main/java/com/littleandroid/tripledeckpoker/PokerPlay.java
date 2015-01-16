package com.littleandroid.tripledeckpoker;

import android.content.Context;

/**
 * Created by Kristen on 1/16/2015.
 */
public class PokerPlay {
    private Context mContext;
    private GameType mGameType;
    private String mGameName;
    private PokerDeck mDeck;
    private PokerHand mHand;

    public PokerPlay(Context context, GameType gameType) {
        mContext = context.getApplicationContext();
        mGameType = gameType;

        switch(mGameType) {
            case GAME_JACKS_OR_BETTER:
                mDeck = new PokerDeck(DeckSize.DECK_52);
                mHand = new JacksOrBetterPokerHand(mContext);
                mGameName = mContext.getResources().getString(R.string.game_jacks_or_better);
                break;
            case GAME_DEUCES_WILD:
                mDeck = new PokerDeck(DeckSize.DECK_52);
                mDeck.makeDeucesWild();
                mHand = new DeucesWildPokerHand(mContext);
                mGameName = mContext.getResources().getString(R.string.game_deuces_wild);
                break;
            case GAME_DOUBLE_BONUS:
                mDeck = new PokerDeck(DeckSize.DECK_52);
                mHand = new DoubleBonusPokerHand(mContext);
                mGameName = mContext.getResources().getString(R.string.game_double_bonus);
                break;
            case GAME_DOUBLE_DOUBLE_BONUS:
                mDeck = new PokerDeck(DeckSize.DECK_52);
                mHand = new DoubleDoubleBonusPokerHand(mContext);
                mGameName = mContext.getResources().getString(R.string.game_double_double_bonus);
                break;
            case GAME_ACES_FACES:
                mDeck = new PokerDeck(DeckSize.DECK_52);
                mHand = new AcesFacesPokerHand(mContext);
                mGameName = mContext.getResources().getString(R.string.game_aces_face);
                break;
            case GAME_JOKER_POKER:
                mDeck = new PokerDeck(DeckSize.DECK_53);
                mDeck.makeJokerWild();
                mHand = new JokerPokerPokerHand(mContext);
                mGameName = mContext.getResources().getString(R.string.game_joker_poker);
                break;
        }
        mDeck.shuffle();
    }

    public String getGameName() {
        return mGameName;
    }

    public PokerCard getCard(int cardNum) {
        return mHand.getCard(cardNum);
    }

    public void toggleHold(int cardNum) {
        mHand.toggleHold(cardNum);
    }

    public boolean isHeld(int cardNum) {
        return mHand.isHeld(cardNum);
    }

    public boolean isInHand(PokerCard c) {
        return mHand.isInHand(c);
    }

    public void replaceCard(int cardNum, PokerCard c) {
        mHand.replaceCard(cardNum, c);
    }

    public int getHandSize() {
        return mHand.size();
    }

    public HandType getHandType() {
        return mHand.getHandType();
    }

    /** Remove cards from deck and put in hand. */
    public void deal() {
        if(mDeck != null) {
            mHand.clearHand();
            for(int i = 0; i < PokerHand.PLAY_SIZE; ++i) {
                mHand.addCard(mDeck.dealCard());
            }
        }
    }

    /** Remove the cards that are in pp's hand from our deck, and then deal */
    public void deal(PokerPlay pp) {
        if(mDeck != null) {
            for(int i = 0; i < pp.getHandSize(); ++i) {
                mDeck.removeCard(pp.getCard(i));
            }
            deal();
        }
    }

    /** Remove cards from deck and replace the cards that are not held */
    public void draw() {
        if(mDeck != null) {
            for(int i = 0; i < mHand.size(); ++i) {
                PokerCard c = mDeck.dealCard(); // draw card for this position even if we wont use it because that position is held.
                if(mHand.isHeld(i) == false) {
                    mHand.replaceCard(i, c);
                }
                else {
                    mHand.toggleHold(i);
                }
            }
        }
    }

    /** Replace cards in our hand if they are being held in pp */
    public void replaceCards(PokerPlay pp) {
        for(int i = 0; i < getHandSize() && i < pp.getHandSize(); ++i) {
            if(pp.getCard(i).isHeld()) {
                mHand.replaceCard(i, pp.getCard(i));
            }
        }
    }

    /** Get rid of this deck if we won't be dealing from it again */
    public void discardDeck() {
       mDeck = null;
    }

    /** Call hand's autoHold, return number of cards being held. */
    public int autoHold() {
        int count = 0;

        if(getHandType() != HandType.HAND_LOSE) {
            autoHold();
            for(int i = 0; i < getHandSize(); ++ i) {
                if(getCard(i).isHeld()) {
                    ++count;
                }
            }
        }

        return count;
    }

    /** Get payout for this hand for this bet (bet >= 1 && bet <= PokerHand.MAX_BET) */
    public int getPayout(int bet) {
        return mHand.getPayout(bet);
    }
}

