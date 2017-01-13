package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OrderType;
import com.imc.intern.exchange.datamodel.api.Symbol;

public class Opportunities {
    private RemoteExchangeView myClient;
    private Book tacoBook;
    private Book beefBook;
    private Book tortBook;
    private long timeOfLastTrade;

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

        int sellTacoVolume = minOfThree(tacoBook.getBids().get(bestTacoBookBids), beefBook.getAsks().get(bestBeefBookAsks), tortBook.getAsks().get(bestTortBookAsks));
        int buyTacoVolume = minOfThree(tacoBook.getAsks().get(bestTacoBookAsks), beefBook.getBids().get(bestBeefBookBids),tortBook.getBids().get(bestTortBookBids));

        // Taco, Beef, Tort Arbitrage
        if (bestTacoBookBids > bestBeefBookAsks + bestTortBookAsks) {
            int volume = limitVolume(sellTacoVolume, 100);
            if (System.currentTimeMillis() - timeOfLastTrade < 10000) {
                return;
            }
            myClient.createOrder(Symbol.of("TACO"), bestTacoBookBids, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            myClient.createOrder(Symbol.of("TORT"), bestTortBookAsks, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            myClient.createOrder(Symbol.of("BEEF"), bestBeefBookAsks, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            timeOfLastTrade = System.currentTimeMillis();
        } else if (bestTacoBookAsks < bestBeefBookBids + bestTortBookBids) {
            int volume = limitVolume(buyTacoVolume, 100);
            if (System.currentTimeMillis() - timeOfLastTrade < 10000) {
                return;
            }
            myClient.createOrder(Symbol.of("TACO"), bestTacoBookAsks, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
            myClient.createOrder(Symbol.of("TORT"), bestTortBookBids, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            myClient.createOrder(Symbol.of("BEEF"), bestBeefBookBids, volume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
            timeOfLastTrade = System.currentTimeMillis();
        }
    }

    public int minOfThree(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    public int limitVolume(int volume, int limit) {
        return Math.min(volume, limit);
    }

    public void flattenOut(PositionTracker positionTracker, ValuationHandler valuationHandler, int beefOrTort) {
        if (beefOrTort == 0) {
            int position = positionTracker.getBeefPosition();
            Side side;
            if (position < 0) {
                side = Side.BUY;
            } else {
                side = Side.SELL;
            }
            System.out.println("sending good till cancel");
            double price = ((double) (long) (valuationHandler.getCurrentValue() * 20 + 0.5)) / 20;
            System.out.println("price of good till cancel is: " + price);
            myClient.createOrder(Symbol.of("BEEF"), price, position, OrderType.GOOD_TIL_CANCEL, side);
        } else {
            int position = positionTracker.getTortPosition();
            Side side;
            if (position < 0) {
                side = Side.BUY;
            } else {
                side = Side.SELL;
            }
            double price = ((double) (long) (valuationHandler.getCurrentValue() * 20 + 0.5)) / 20;
            System.out.println("price of good till cancel is: " + price);
            myClient.createOrder(Symbol.of("TORT"), price, position, OrderType.GOOD_TIL_CANCEL, side);
            System.out.println("sending good till cancel");
        }
    }
}