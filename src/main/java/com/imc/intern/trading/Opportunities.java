package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OrderType;
import com.imc.intern.exchange.datamodel.api.RetailState;
import com.imc.intern.exchange.datamodel.api.Symbol;

import java.util.List;

public class Opportunities {

    List<RetailState.Level> levels;
    String BOOK;
    RemoteExchangeView myClient;
    public Opportunities(List<RetailState.Level> levels, String BOOK, RemoteExchangeView myClient) {
        this.levels = levels;
        this.BOOK = BOOK;
        this.myClient = myClient;
    }

    public void checkOpportunity(Side side) {
        double bestPrice;
        int bestVolume;
        if (!this.levels.isEmpty()) {
            if (side == Side.SELL) {
                for (RetailState.Level level : this.levels) {
                    bestPrice = level.getPrice();
                    bestVolume = level.getVolume();
                    double pricePerShare = bestPrice / bestVolume;
                    //if (pricePerShare > (value + 0.10) &&
                    //bestVolume > 0 &&
                    //totalVolumeMoved[0] != 0) {
                    //my_client.createOrder(Symbol.of(BOOK), bestPrice, bestVolume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
                    //}

                    //This is my code for the first exercise
                    if (bestPrice > 20.10 && bestVolume > 0) {
                        myClient.createOrder(Symbol.of(BOOK), bestPrice, bestVolume, OrderType.IMMEDIATE_OR_CANCEL, Side.SELL);
                    }
                }
            } else if (side == Side.BUY) {
                for (RetailState.Level level : this.levels) {
                    bestPrice = level.getPrice();
                    bestVolume = level.getVolume();
                    double pricePerShare = bestPrice / bestVolume;
                    //if (pricePerShare > (value + 0.10) &&
                    //bestVolume > 0 &&
                    //totalVolumeMoved[0] != 0) {
                    //my_client.createOrder(Symbol.of(BOOK), bestPrice, bestVolume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
                    //}

                    //This is my code for the first exercise
                    if (bestPrice < 19.90 && bestVolume > 0) {
                        myClient.createOrder(Symbol.of(BOOK), bestPrice, bestVolume, OrderType.IMMEDIATE_OR_CANCEL, Side.BUY);
                    }
                }
            }
        }
    }
}
