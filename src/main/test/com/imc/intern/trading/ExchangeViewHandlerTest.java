package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.api.Trade;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExchangeViewHandlerTest {

    ExchangeViewHandler testHandler = new ExchangeViewHandler(null, null);
    Trade trade1 = new Trade(null, 10, 10, 0);
    Trade trade2 = new Trade(null, 100, 10, 0);

    /*
        cproctor: Let me know if you'd like to get a test set up today and we can work on it together!
     */
    @Test
    public void handleTradeTest() {
        assert true;
    }
}