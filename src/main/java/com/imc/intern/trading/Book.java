package com.imc.intern.trading;

import com.imc.intern.exchange.datamodel.api.RetailState;

import java.util.TreeMap;

public class Book {
    private TreeMap<Double, Integer> bids = new TreeMap<>();
    private TreeMap<Double, Integer> asks = new TreeMap<>();
    private String BOOK;

    public Book(String BOOK) {
        this.BOOK = BOOK;
    }

    public void updateBook(RetailState retailState) {
        for (RetailState.Level level : retailState.getBids()) {
            if (level.getVolume() != 0) {
                bids.put(level.getPrice(), level.getVolume());
            } else {
                bids.remove(level.getPrice());
            }
        }

        for (RetailState.Level level : retailState.getAsks()) {
            if (level.getVolume() != 0) {
                asks.put(level.getPrice(), level.getVolume());
            } else {
                bids.remove(level.getPrice());
            }
        }
        System.out.println(bids.lastKey());
        System.out.println(asks.firstKey());
    }

    public TreeMap<Double, Integer> getBids() {
        return bids;
    }

    public TreeMap<Double, Integer> getAsks() {
        return asks;
    }
}
