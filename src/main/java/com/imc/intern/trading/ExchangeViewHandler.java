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
    // NAJ: typically, you want to keep members of a class private, and if you need access, to define getters/setters.
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

    // NAJ: typically would like to keep all members of a class defined in the same location. So LOGGER would be on the top of the class.
    // NAJ: Usually organized from top of file down as: Static Final / Static / final / others / public methods / private methods
    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @Override
    public void handleRetailState(RetailState retailState) {
        LOGGER.info(retailState.toString());

        // NAJ: It seems like a cleaner line of thought here would be to extract opportunities through some type of opportunity checker,
        // NAJ: and then place orders based on opportunities through some sort of order placer.
        // NAJ: This makes it cleaner then to pass in this checker and placer as separate members of this handler/
        // NAJ: Makes testing easier as you can mock interactions with checker/placer.
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
        // NAJ: This seems like a nice single responsibility for its own class to handle average prices and volume ticker
        LOGGER.info(trade.toString());
        double price = trade.getPrice();
        int volume = trade.getVolume();
        averagePrice = ((averagePrice * totalVolumeMoved) + (price * volume)) / (volume + totalVolumeMoved);
        totalVolumeMoved += volume;
        LOGGER.info("Average Price:" + (averagePrice));
    }

    @Override
    public void handleOwnTrade(OwnTrade trade) {
        // NAJ: Ditto on another class here to handle/track positions
        LOGGER.info(trade.toString());
        if (trade.getSide() == Side.BUY) {
            position += trade.getVolume();
        } else {
            position -= trade.getVolume();
        }
        LOGGER.info("Position:" + position);
        myTrades.add(trade);
    }

    // NAJ: Errors get no love?
}
