package com.imc.intern.trading;

import com.imc.intern.exchange.client.ExchangeClient;
import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.api.*;

public class Main
{
    private static final String EXCHANGE_URL = "tcp://wintern.imc.com:61616";
    private static final String USERNAME = "amehndir";
    private static final String PASSWORD = "winter straw merely club";
    private static final String BOOK = "AME1";

    public static void main(String[] args) throws Exception
    {
        // NAJ: overall great improvement from 1on1, keep up the good work.
        // NAJ: focus on object-oriented design, and think about classes/objects in that frame of mind.
        // NAJ: e.g. how do I want to interact with positions, how to I want opportunities to be traded on, where should my valuation logic be contained?
        // NAJ: remember S.O.L.I.D, let me know if you're unfamiliar with this: "https://en.wikipedia.org/wiki/SOLID_(object-oriented_design)"
        ExchangeClient client = ExchangeClient.create(EXCHANGE_URL, Account.of(USERNAME), PASSWORD);
        RemoteExchangeView my_client = client.getExchangeView();
        client.start();
        ExchangeViewHandler ExchangeHandler = new ExchangeViewHandler(BOOK, my_client);
        my_client.subscribe(Symbol.of(BOOK), ExchangeHandler);
    }
}