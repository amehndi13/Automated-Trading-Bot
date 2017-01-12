package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OwnTrade;

public class PositionHandler {
    private int positionTACO = 0;
    private int positionBEEF = 0;
    private int positionTORT = 0;
    private int offsetTACO = 0;
    private int offsetBEEF = 0;
    private int offsetTORT = 0;

    /*
        cproctor: If you create a PositionManager per book, you remove the filtering here too!
     */
    public void updatePosition(OwnTrade trade, String BOOK) {
        if (BOOK == "TACO") {
            if (trade.getSide() == Side.BUY) {
                positionTACO += trade.getVolume();
            } else {
                positionTACO -= trade.getVolume();
            }
        } else if (BOOK == "TORT") {
            if (trade.getSide() == Side.BUY) {
                positionTORT += trade.getVolume();
            } else {
                positionTORT -= trade.getVolume();
            }
        } else if (BOOK == "BEEF") {
            if (trade.getSide() == Side.BUY) {
                positionBEEF += trade.getVolume();
            } else {
                positionBEEF -= trade.getVolume();
            }
        }
        updateOffset(BOOK);
    }

    public void updateOffset(String BOOK) {
        if (BOOK == "TACO") {
            if (positionTACO < 0) {
                //should be negative once actually calculated
                offsetTACO = -1;
            } else if (positionTACO > 0) {
                //should be positive once actually calculated
                offsetTACO = 1;
            }
        } else if (BOOK == "TORT") {
            if (positionTORT < 0) {
                //should be negative once actually calculated
                offsetTORT = -1;
            } else if (positionTORT > 0) {
                //should be positive once actually calculated
                offsetTORT = 1;
            }
        } else if (BOOK == "BEEF") {
            if (positionBEEF < 0) {
                //should be negative once actually calculated
                offsetBEEF = -1;
            } else if (positionBEEF > 0) {
                //should be positive once actually calculated
                offsetBEEF = 1;
            }
        }
    }

    public int getPosition (String BOOK) {
        if (BOOK == "TACO") {
            return positionTACO;
        } else if (BOOK == "TORT") {
            return positionTORT;
        } else if (BOOK == "BEEF") {
            return positionBEEF;
        }
        return 0;
    }
}
