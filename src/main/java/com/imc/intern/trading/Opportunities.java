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

        // msanders: it's probably easier to use a regular list here instead of an array
        int[] volumes1 = new int[3];
        int[] volumes2 = new int[3];

        volumes1[0] = tacoBook.getBids().get(bestTacoBookBids); // msanders: this naming is confusing -- it seems that you're
        volumes2[0] = tacoBook.getAsks().get(bestTacoBookAsks); // using this array to store something like a tuple of prices and
        volumes2[1] = beefBook.getBids().get(bestBeefBookBids); // volumes -- why not make a small class to hold the three values
        volumes1[1] = beefBook.getAsks().get(bestBeefBookAsks); // instead?
        volumes2[2] = tortBook.getBids().get(bestTortBookBids);
        volumes1[2] = tortBook.getAsks().get(bestTortBookAsks);

        Arrays.sort(volumes1);
        Arrays.sort(volumes2);
        // Taco, Beef, Tort Arbitrage
        if (bestTacoBookBids > bestBeefBookAsks + bestTortBookAsks) {
            // msanders: this logic is a bit confusing also -- let's chat a bit tomorrow about what's going on here
            int volume = volumes1[0]%10 == 0 ? 50 : volumes1[0]%100;
            myClient.createOrder(Symbol.of("TACO"), bestTacoBookBids, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            myClient.createOrder(Symbol.of("TORT"), bestTortBookAsks, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            myClient.createOrder(Symbol.of("BEEF"), bestBeefBookAsks, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            TimeUnit.SECONDS.sleep(35);
        } else if (bestTacoBookAsks < bestBeefBookBids + bestTortBookBids) {
            int volume = volumes2[0]%100 == 0 ? 50 : volumes2[0]%100;
            myClient.createOrder(Symbol.of("TACO"), bestTacoBookAsks, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            myClient.createOrder(Symbol.of("TORT"), bestTortBookBids, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            myClient.createOrder(Symbol.of("BEEF"), bestBeefBookBids, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            TimeUnit.SECONDS.sleep(35);
        }
    }

    public void flattenOut(PositionHandler positionHandler, ValuationHandler valuationHandler) {
        int position = positionHandler.getPosition();
        Side side;
        if (position < 0) {
            side = Side.BUY;
        } else {
            side = Side.SELL;
        }
        myClient.createOrder(Symbol.of(positionHandler.getBook()), valuationHandler.getCurrentValue(), position, OrderType.GOOD_TIL_CANCEL, side);
    }
}