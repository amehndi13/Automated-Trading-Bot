package com.imc.intern.trading;

import com.imc.intern.exchange.client.RemoteExchangeView;
import com.imc.intern.exchange.datamodel.Side;
import com.imc.intern.exchange.datamodel.api.OrderType;
import com.imc.intern.exchange.datamodel.api.RetailState;
import com.imc.intern.exchange.datamodel.api.Symbol;

import java.util.List;

public class Opportunities {

    // NAJ: similar comments of fields as in Handler
    List<RetailState.Level> levels;
    String BOOK;
    RemoteExchangeView myClient;
    public Opportunities(List<RetailState.Level> levels, String BOOK, RemoteExchangeView myClient) {
        this.levels = levels;
        this.BOOK = BOOK;
        this.myClient = myClient;
    }

    // NAJ: I would pass in levels as a parameter rather than a field
    public void checkOpportunity(Side side) {
        double bestPrice;
        int bestVolume;
        // NAJ: using the implied iterator pattern simplify this code as an empty list would not go into the loop;
        // NAJ: for (RetailState.Level myLevel : levels){}
        if (!this.levels.isEmpty()) {
            if (side == Side.SELL) {
                for (RetailState.Level level : this.levels) { // NAJ: don't need to use the "this.*" notation.
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
