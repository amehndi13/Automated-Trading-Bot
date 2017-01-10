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
        ExchangeClient client = ExchangeClient.create(EXCHANGE_URL, Account.of(USERNAME), PASSWORD);
        RemoteExchangeView my_client = client.getExchangeView();
        client.start();
        ExchangeViewHandler ExchangeHandler = new ExchangeViewHandler(BOOK, my_client);
        my_client.subscribe(Symbol.of(BOOK), ExchangeHandler);
    }
}