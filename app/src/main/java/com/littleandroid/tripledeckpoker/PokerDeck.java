package com.littleandroid.tripledeckpoker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Kristen on 1/12/2015.
 */
public class PokerDeck {

    private DeckSize mDeckSize;
    private ArrayList<PokerCard> mDeck;

    private static Random numGen = new Random();

    public PokerDeck(DeckSize deckSize) {
        mDeckSize = deckSize;
        mDeck = new ArrayList<PokerCard>(mDeckSize.size());

        for(Suit s : Suit.values()) {
            for(Rank r : Rank.values()) {
                if(r != Rank.JOKER) {
                    mDeck.add(new PokerCard(r, s));
                }
            }
        }

        if(mDeckSize == DeckSize.DECK_53) {
            mDeck.add(new PokerCard(Rank.JOKER, null));  /* Suit is not relevant here */
        }
    }

    public void shuffle() {
        Collections.shuffle(mDeck, numGen);
    }

    public void shuffle(int swaps) {

        if(getCardCount() > 1) {
            for(int swapCount = 0; swapCount < swaps; ++swapCount) {
                int i = numGen.nextInt(getCardCount());
                int j = numGen.nextInt(getCardCount());
                if (i != j) {
                    Collections.swap(mDeck, i, j);
                }
            }
        }
    }

    public void makeDeucesWild() {
        for(PokerCard c : mDeck) {
            if(c.isDeuce()) {
                c.setWild(true);
            }
        }
    }

    public void makeJokerWild() {
        for(PokerCard c : mDeck) {
            if(c.isJoker()) {
                c.setWild(true);
            }
        }
    }

    public PokerCard dealCard()
    {
        PokerCard c = mDeck.get(0);
        mDeck.remove(0);
        return c;
    }

    public boolean removeCard(PokerCard p2) {
        return mDeck.remove(p2);
    }

    public int getCardCount() {
        return mDeck.size();
    }

    public DeckSize getDeckSize() {
        return mDeckSize;
    }
}
