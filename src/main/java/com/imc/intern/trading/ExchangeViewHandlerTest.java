package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.api.Trade;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExchangeViewHandlerTest {

    ExchangeViewHandler testHandler = new ExchangeViewHandler(null, null);
    Trade trade = new Trade(null, 10, 10, 0);

    @Test
    public void handleTradeTest() {
        testHandler.handleTrade(trade);
        assert testHandler.averagePrice == 1.0;
        assert testHandler.totalVolumeMoved == 10;
    }
}