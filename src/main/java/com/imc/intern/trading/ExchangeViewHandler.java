package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.api.*;
import com.imc.intern.exchange.datamodel.api.Error;
import com.imc.intern.exchange.datamodel.jms.ExposureUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.logging.Handler;


public class ExchangeViewHandler implements OrderBookHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);
    private Book Book;
    private String BOOK;
    private Opportunities opportunities;
    private ValuationHandler valuationHandler = new ValuationHandler();
    private PositionTracker positionTracker;
    private boolean needToFlatten = false;

    public ExchangeViewHandler(String BOOK, Book Book, Opportunities opportunities, PositionTracker positionTracker) {
        this.Book = Book;
        this.BOOK = BOOK;
        this.opportunities = opportunities;
        this.positionTracker = positionTracker;
    }

    @Override
    public void handleRetailState(RetailState retailState) {
        LOGGER.info(retailState.toString());
        Book.updateBook(retailState);
        try {
            if (!needToFlatten) {
                opportunities.checkOpportunities();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        valuationHandler.updateCurrentValue(Book);
    }

    @Override
    public void handleExposures(ExposureUpdate exposureUpdate) {
        LOGGER.info(exposureUpdate.toString());
    }

    @Override
    public void handleTrade(Trade trade) {
        LOGGER.info(trade.toString());
        valuationHandler.updateMovingAverage(trade);
        double currentMovingAverage = valuationHandler.getMovingAverage();
        double currentValue = valuationHandler.getCurrentValue();
        LOGGER.info("Current Moving Average for " + BOOK + ": " + (currentMovingAverage));
        LOGGER.info("Current Value for " + BOOK + ": " + (currentValue));
    }

    @Override
    public void handleOwnTrade(OwnTrade trade) {
        LOGGER.info(trade.toString());
        int beef = 0;
        int tort = 1;
        if (positionTracker.getBeefPosition() != 0) {
            System.out.println("FLATTENING!!!!");
            opportunities.flattenOut(positionTracker, valuationHandler, beef);
            needToFlatten = true;
        } else if (positionTracker.getTortPosition() != 0) {
            System.out.println("FLATTENING!!!!");
            opportunities.flattenOut(positionTracker, valuationHandler, tort);
            needToFlatten = true;
        } else if (positionTracker.getBeefPosition() == 0 && positionTracker.getBeefPosition() == 0) {
            System.out.println("DONE FLATTENING!!!!");
            needToFlatten = false;
        }
    }

    @Override
    public void handleError(Error error) {
        LOGGER.error(error.toString());
    }
}