package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OrderType;
import com.imc.intern.exchange.datamodel.api.Symbol;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Opportunities {
    private RemoteExchangeView myClient;
    private Book tacoBook;
    private Book beefBook;
    private Book tortBook;
    private int counter = 0;

    public Opportunities(RemoteExchangeView myClient, Book tacoBook, Book beefBook, Book tortBook) {
        this.myClient = myClient;
        this.tortBook = tortBook;
        this.beefBook = beefBook;
        this.tacoBook = tacoBook;
    }

    public void checkOpportunities() throws InterruptedException {
        double bestTacoBookAsks = tacoBook.getAsks().firstKey();
        double bestTacoBookBids = tacoBook.getBids().lastKey();
        double bestBeefBookAsks = beefBook.getAsks().firstKey();
        double bestBeefBookBids = beefBook.getBids().lastKey();
        double bestTortBookAsks = tortBook.getAsks().firstKey();
        double bestTortBookBids = tortBook.getBids().lastKey();
        int[] volumes1 = new int[3];
        int[] volumes2 = new int[3];

        volumes1[0] = tacoBook.getBids().get(bestTacoBookBids);
        volumes2[0] = tacoBook.getAsks().get(bestTacoBookAsks);
        volumes2[1] = beefBook.getBids().get(bestBeefBookBids);
        volumes1[1] = beefBook.getAsks().get(bestBeefBookAsks);
        volumes2[2] = tortBook.getBids().get(bestTortBookBids);
        volumes1[2] = tortBook.getAsks().get(bestTortBookAsks);

        Arrays.sort(volumes1);
        Arrays.sort(volumes2);
        // Taco, Beef, Tort Arbitrage
        if (bestTacoBookBids > bestBeefBookAsks + bestTortBookAsks && counter%2 == 0) {
            myClient.createOrder(Symbol.of("TACO"), bestTacoBookBids, (volumes1[0] % 10), OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            myClient.createOrder(Symbol.of("TORT"), bestTortBookAsks, (volumes1[0] % 10), OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            myClient.createOrder(Symbol.of("BEEF"), bestBeefBookAsks, (volumes1[0] % 10), OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            TimeUnit.SECONDS.sleep(1);
        } else if (bestTacoBookAsks < bestBeefBookBids + bestTortBookBids && counter%2 == 1) {
            myClient.createOrder(Symbol.of("TACO"), bestTacoBookAsks, (volumes2[0] % 10), OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            myClient.createOrder(Symbol.of("TORT"), bestTortBookBids, (volumes2[0] % 10), OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            myClient.createOrder(Symbol.of("BEEF"), bestBeefBookBids, (volumes2[0] % 10), OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            TimeUnit.SECONDS.sleep(1);
        }
    }
}