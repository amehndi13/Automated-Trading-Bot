package com.imc.intern.trading;

import com.imc.intern.exchange.client.ExchangeClient;
import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.api.*;

public class Main
{
    private static final String EXCHANGE_URL = "tcp://54.227.125.23:61616";
    private static final String USERNAME = "amehndir";
    private static final String PASSWORD = "winter straw merely club";
    private static final String TACO_BOOK = "TACO";
    private static final String TORT_BOOK = "TORT";
    private static final String BEEF_BOOK = "BEEF";

    public static void main(String[] args) throws Exception
    {
        ExchangeClient client = ExchangeClient.create(EXCHANGE_URL, Account.of(USERNAME), PASSWORD);
        client.start();
        RemoteExchangeView my_client = client.getExchangeView();
        ExchangeViewHandler ExchangeHandlerTACO = new ExchangeViewHandler(TACO_BOOK, my_client);
        ExchangeViewHandler ExchangeHandlerTORT = new ExchangeViewHandler(TORT_BOOK, my_client);
        ExchangeViewHandler ExchangeHandlerBEEF = new ExchangeViewHandler(BEEF_BOOK, my_client);
        my_client.subscribe(Symbol.of(BEEF_BOOK), ExchangeHandlerBEEF);
        my_client.subscribe(Symbol.of(TORT_BOOK), ExchangeHandlerTORT);
        my_client.subscribe(Symbol.of(TACO_BOOK), ExchangeHandlerTACO);
    }
}