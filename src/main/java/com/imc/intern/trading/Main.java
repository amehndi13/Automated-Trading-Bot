package com.imc.intern.trading;

import com.imc.intern.exchange.client.ExchangeClient;
import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.*;

public class Main {
    private static final String EXCHANGE_URL = "tcp://54.227.125.23:61616";
    private static final String USERNAME = "amehndir";
    private static final String PASSWORD = "winter straw merely club";
    private static final String TACO_BOOK = "TACO";
    private static final String TORT_BOOK = "TORT";
    private static final String BEEF_BOOK = "BEEF";

    public static void main(String[] args) throws Exception
    {
        Book tacoBook = new Book(TACO_BOOK);
        Book beefBook = new Book(BEEF_BOOK);
        Book tortBook = new Book(TORT_BOOK);
        ExchangeClient client = ExchangeClient.create(EXCHANGE_URL, Account.of(USERNAME), PASSWORD);
        client.start();
        RemoteExchangeView my_client = client.getExchangeView();
        Opportunities opportunities = new Opportunities(my_client, tacoBook, beefBook, tortBook);
        ExchangeViewHandler ExchangeHandlerTACO = new ExchangeViewHandler(TACO_BOOK, tacoBook, opportunities);
        ExchangeViewHandler ExchangeHandlerTORT = new ExchangeViewHandler(TORT_BOOK, tortBook, opportunities);
        ExchangeViewHandler ExchangeHandlerBEEF = new ExchangeViewHandler(BEEF_BOOK, beefBook, opportunities);
        my_client.subscribe(Symbol.of(BEEF_BOOK), ExchangeHandlerBEEF);
        my_client.subscribe(Symbol.of(TORT_BOOK), ExchangeHandlerTORT);
        my_client.subscribe(Symbol.of(TACO_BOOK), ExchangeHandlerTACO);
    }
}