package com.calculator.model;

public class Spot {
  private double allInPrice;
  private double cleanPrice;
  private double accruedInterest;

  public Spot(double allInPrice, double cleanPrice, double accruedInterest) {
    this.allInPrice = allInPrice;
    this.cleanPrice = cleanPrice;
    this.accruedInterest = accruedInterest;
  }

  public Spot() {}

  public double getAllInPrice() {
    return this.allInPrice;
  }

  public double getCleanPrice() {
    return this.cleanPrice;
  }

  public double getAccruedInterest() {
    return this.accruedInterest;
  }

  public void setAllInPrice(double allInPrice) {
    this.allInPrice = allInPrice;
  }

  public void setCleanPrice(double cleanPrice) {
    this.cleanPrice = cleanPrice;
  }

  public void setAccruedInterest(double accruedInterest) {
    this.accruedInterest = accruedInterest;
  }
}
