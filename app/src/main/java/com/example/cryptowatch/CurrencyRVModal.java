package com.example.cryptowatch;

public class CurrencyRVModal {
    private String name;
    private String symbol;
    private double price;
    private double hourChange;
    private double dayChange;
    private double weekChange;


    public CurrencyRVModal(String name, String symbol, double price, double hourChange, double dayChange, double weekChange) {
        this.name = name;
        this.symbol = symbol;
        this.price = price;
        this.hourChange = hourChange;
        this.dayChange = dayChange;
        this.weekChange = weekChange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getHourChange() {return hourChange;}

    public void setHourChange(double hourChange) {this.hourChange = hourChange;}

    public double getDayChange() {return dayChange;}

    public void setDayChange(double dayChange) {this.dayChange = dayChange;}

    public double getWeekChange() {return weekChange;}

    public void setWeekChange(double weekChange) {this.weekChange = weekChange;}
}
