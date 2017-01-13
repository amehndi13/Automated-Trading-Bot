package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OwnTrade;

public class PositionHandler {
    private int position = 0;
    private int offset = 0;
    private String BOOK;

    public PositionHandler(String BOOK) {
        this.BOOK = BOOK;
    }

    public void updatePosition(OwnTrade trade) {
        if (trade.getSide() == Side.BUY) {
            position += trade.getVolume();
        } else {
            position -= trade.getVolume();
        }
        updateOffset();
    }

    // msanders: let's turn this magic number into a constant
    public void updateOffset() {
        if (position < 30) {
            //should be negative once actually calculated
            offset = -1;
        } else if (position > 30) {
            //should be positive once actually calculated
            offset = 1;
        }
    }

    public int getPosition() {
        return position;
    }

    public int getOffset() {
        return offset;
    }

    public String getBook() {
        return BOOK;
    }
}
