package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OrderBookHandler;
import com.imc.intern.exchange.datamodel.api.OwnTrade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.logging.Handler;

public class PositionTracker implements OrderBookHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);
    private int beefPosition = 0;
    private int tortPosition = 0;
    private int tacoPosition = 0;

    @Override
    public void handleOwnTrade(OwnTrade trade) {
        LOGGER.info("This is the position tracker retail state " + trade.toString());
        this.updatePositions(trade);
    }

    public void updatePositions(OwnTrade trade) {
        LOGGER.info(trade.toString());
        if (trade.getBook().toString().equals("TACO")) {
            if (trade.getSide() == Side.BUY) {
                beefPosition += trade.getVolume();
                tortPosition += trade.getVolume();
                tacoPosition += trade.getVolume();
            } else if (trade.getSide() == Side.SELL) {
                beefPosition -= trade.getVolume();
                tortPosition -= trade.getVolume();
                tacoPosition -= trade.getVolume();
            }
        } else if (trade.getBook().toString().equals("TORT")) {
            if (trade.getSide() == Side.BUY) {
                tortPosition += trade.getVolume();
            } else if (trade.getSide() == Side.SELL) {
                tortPosition -= trade.getVolume();
            }
        } else if (trade.getBook().toString().equals("BEEF")) {
            if (trade.getSide() == Side.BUY) {
                beefPosition += trade.getVolume();
            } else if (trade.getSide() == Side.SELL) {
                beefPosition -= trade.getVolume();
            }
        }
        LOGGER.info("Positions:\nTaco: " + tacoPosition + "\nBeef: " + beefPosition + "\nTort: " + tortPosition);
    }

    public int getBeefPosition() {
        return beefPosition;
    }

    public int getTortPosition() {
        return tortPosition;
    }
}
