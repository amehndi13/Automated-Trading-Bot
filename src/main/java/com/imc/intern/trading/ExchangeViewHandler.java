package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.*;
import com.imc.intern.exchange.datamodel.jms.ExposureUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class ExchangeViewHandler implements OrderBookHandler {
    double averagePrice = 20;
    int totalVolumeMoved = 0;
    int position = 0;
    List<OwnTrade> myTrades = new ArrayList<>();
    String BOOK;
    RemoteExchangeView myClient;

    public ExchangeViewHandler(String BOOK, RemoteExchangeView client) {
        this.BOOK = BOOK;
        this.myClient = client;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @Override
    public void handleRetailState(RetailState retailState) {
        LOGGER.info(retailState.toString());

        List<RetailState.Level> bids = retailState.getBids();
        Opportunities buys = new Opportunities(bids, BOOK, myClient);
        buys.checkOpportunity(Side.SELL);

        List<RetailState.Level> asks = retailState.getAsks();
        Opportunities sells = new Opportunities(asks, BOOK, myClient);
        sells.checkOpportunity(Side.BUY);
    }

    @Override
    public void handleExposures(ExposureUpdate exposureUpdate) {
        LOGGER.info(exposureUpdate.toString());
    }

    @Override
    public void handleTrade(Trade trade) {
        LOGGER.info(trade.toString());
        double price = trade.getPrice();
        int volume = trade.getVolume();
        averagePrice = ((averagePrice * totalVolumeMoved) + price) / (volume + totalVolumeMoved);
        totalVolumeMoved += volume;
        LOGGER.info("Average Price:" + (averagePrice));
    }

    @Override
    public void handleOwnTrade(OwnTrade trade) {
        LOGGER.info(trade.toString());
        if (trade.getSide() == Side.BUY) {
            position += trade.getVolume();
        } else {
            position -= trade.getVolume();
        }
        LOGGER.info("Position:" + position);
        myTrades.add(trade);
    }
}
