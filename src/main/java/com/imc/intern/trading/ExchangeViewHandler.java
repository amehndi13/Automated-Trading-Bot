package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.*;
import com.imc.intern.exchange.datamodel.api.Error;
import com.imc.intern.exchange.datamodel.jms.ExposureUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class ExchangeViewHandler implements OrderBookHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);
    private List<OwnTrade> myTrades = new ArrayList<>();
    private String BOOK;
    private RemoteExchangeView myClient;
    private ValuationHandler valuationHandler = new ValuationHandler();


    public ExchangeViewHandler(String BOOK, RemoteExchangeView client) {
        this.BOOK = BOOK;
        this.myClient = client;
    }

    @Override
    public void handleRetailState(RetailState retailState) {
        LOGGER.info(retailState.toString());
        /*
            cproctor: Do you really want to create a new Opportunities for each retail state update that you get?
             In doing so, you create a new object, which creates new Books inside it. These books don't have the state.
             Instead, you probably want to create the Books in your main and pass each book to its respective
             ExchangeViewHandler. You can then create one Opportunities in your main and pass those same books to it as
             parameters.
         */
        Opportunities opportunities = new Opportunities(myClient);
        opportunities.checkOpportunities(retailState, BOOK);
    }

    @Override
    public void handleExposures(ExposureUpdate exposureUpdate) {
        LOGGER.info(exposureUpdate.toString());
    }

    @Override
    public void handleTrade(Trade trade) {
        LOGGER.info(trade.toString());
        valuationHandler.updateMovingAverage(trade, BOOK);
        double currentMovingAverage = valuationHandler.getMovingAverage(BOOK);
        LOGGER.info("Current Moving Average for " + BOOK + ": " + (currentMovingAverage));
    }

    @Override
    public void handleOwnTrade(OwnTrade trade) {
        LOGGER.info(trade.toString());
        PositionHandler positionHandler = new PositionHandler();
        positionHandler.updatePosition(trade, BOOK);
        LOGGER.info("Position for " + BOOK + ": " + positionHandler.getPosition(BOOK));
        myTrades.add(trade);
    }

    @Override
    public void handleError(Error error) {
        LOGGER.error(error.toString());
    }
}
