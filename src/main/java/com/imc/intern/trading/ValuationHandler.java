package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.api.Trade;

public class ValuationHandler {
    private double movingAverageBEEF = 0;
    private double movingAverageTORT = 0;
    private double movingAverageTACO = 0;
    private int totalVolumeBEEFMoved = 0;
    private int totalVolumeTACOMoved = 0;
    private int totalVolumeTORTMoved = 0;

    public void updateMovingAverage(Trade trade, String BOOK) {
        double price = trade.getPrice();
        int volume = trade.getVolume();
        if (BOOK == "TACO") {
            movingAverageTACO = ((movingAverageTACO * totalVolumeTACOMoved) + (price * volume)) / (volume + totalVolumeTACOMoved);
            totalVolumeTACOMoved += volume;
        } else if (BOOK == "TORT") {
            movingAverageTORT = ((movingAverageTORT * totalVolumeTORTMoved) + (price * volume)) / (volume + totalVolumeTORTMoved);
            totalVolumeTORTMoved += volume;
        } else if (BOOK == "BEEF") {
            movingAverageBEEF = ((movingAverageBEEF * totalVolumeBEEFMoved) + (price * volume)) / (volume + totalVolumeBEEFMoved);
            totalVolumeBEEFMoved += volume;
        }
    }

    public double getMovingAverage(String BOOK) {
        if (BOOK == "TACO") {
            return movingAverageTACO;
        } else if (BOOK == "TORT") {
            return movingAverageTORT;
        } else if (BOOK == "BEEF") {
            return movingAverageBEEF;
        }
        return 0;
    }
}
