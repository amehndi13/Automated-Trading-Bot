package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OrderType;
import com.imc.intern.exchange.datamodel.api.RetailState;
import com.imc.intern.exchange.datamodel.api.Symbol;

public class Opportunities {
    private RemoteExchangeView myClient;
    private Book tacoBook = new Book();
    private Book beefBook = new Book();
    private Book tortBook = new Book();
    private boolean tacoBookUpdated = false;
    private boolean beefBookUpdated = false;
    private boolean tortBookUpdated = false;

    public Opportunities(RemoteExchangeView myClient) {
        this.myClient = myClient;
    }

    public void checkOpportunities(RetailState retailState, String BOOK) {
        if (tacoBookUpdated && beefBookUpdated && tortBookUpdated) {
            updateBooks(retailState, BOOK);
            double bestTacoBookAsks = tacoBook.getAsks().firstKey();
            double bestTacoBookBids = tacoBook.getBids().lastKey();
            double bestBeefBookAsks = beefBook.getAsks().firstKey();
            double bestBeefBookBids = beefBook.getBids().lastKey();
            double bestTortBookAsks = tortBook.getAsks().firstKey();
            double bestTortBookBids = tortBook.getBids().lastKey();

            if (bestTacoBookBids > bestBeefBookAsks + bestTortBookAsks) {
                myClient.createOrder(Symbol.of("TACO"), bestTacoBookBids, tacoBook.getBids().get(bestTacoBookBids), OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            } else if (bestTacoBookAsks < bestBeefBookBids + bestTortBookBids) {
                myClient.createOrder(Symbol.of("TACO"), bestTacoBookAsks, tacoBook.getAsks().get(bestTacoBookAsks), OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            }
        }
    }

    public void updateBooks(RetailState retailState, String BOOK) {
        if (BOOK.equals("TACO")) {
            tacoBook.updateBook(retailState);
            tacoBookUpdated = true;
        } else if (BOOK.equals("BEEF")) {
            beefBook.updateBook(retailState);
            beefBookUpdated = true;
        } else if (BOOK.equals("TORT")) {
            tortBook.updateBook(retailState);
            tortBookUpdated = true;
        }
    }
}