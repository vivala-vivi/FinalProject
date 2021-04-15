package com.example.vtradeversion3;

public class Stock {

    private String symbol;
    private String longName;
  //  private double c;

    public Stock(){}

    public String getSymbol(){
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

  // public double getCurrentPrice(){
  //      return c;
   // }

  //  public void setCurrentPrice(double c) {
   //     this.c = c;
   //}
}
