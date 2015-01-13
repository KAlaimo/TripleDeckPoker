package com.littleandroid.tripledeckpoker;

/**
 * Created by Kristen on 1/12/2015.
 */
public enum DeckSize {
    DECK_52 (52),
    DECK_53 (53);

    private final int mSize;

    DeckSize(int size) {
        this.mSize = size;
    }

    public int size() {
        return mSize;
    }
}
