package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.api.Trade;

public class ValuationHandler {
    private double movingAverage = 0;
    private int totalVolumeMoved = 0;
    private double currentValue = 0;

    public void updateMovingAverage(Trade trade) {
        double price = trade.getPrice();
        int volume = trade.getVolume();
        movingAverage = ((movingAverage * totalVolumeMoved) + (price * volume)) / (volume + totalVolumeMoved);
        totalVolumeMoved += volume;
    }

    public double getMovingAverage() {
        return movingAverage;
    }

    public void updateCurrentValue(Book Book) {
        double bestAsks = Book.getAsks().firstKey();
        double bestBids = Book.getBids().lastKey();
        int volumeBestAsks = Book.getAsks().get(bestAsks);
        int volumeBestBids = Book.getBids().get(bestBids);

        currentValue = ((bestAsks*volumeBestAsks) + (bestBids*volumeBestBids))/(volumeBestAsks + volumeBestBids);
    }

    public double getCurrentValue() {
        return currentValue;
    }
}
