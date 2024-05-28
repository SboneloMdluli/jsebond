package model;

import java.math.BigDecimal;

public class Spot {
    private BigDecimal allInPrice;
    private BigDecimal cleanPrice;
    private BigDecimal accruedInterest;

    public Spot(BigDecimal allInPrice, BigDecimal cleanPrice, BigDecimal accruedInterest) {
        this.allInPrice = allInPrice;
        this.cleanPrice = cleanPrice;
        this.accruedInterest = accruedInterest;
    }

    public Spot() {
    }

    public BigDecimal getAllInPrice() {
        return this.allInPrice;
    }

    public BigDecimal getCleanPrice() {
        return this.cleanPrice;
    }

    public BigDecimal getAccruedInterest() {
        return this.accruedInterest;
    }

    public void setAllInPrice(BigDecimal allInPrice) {
        this.allInPrice = allInPrice;
    }

    public void setCleanPrice(BigDecimal cleanPrice) {
        this.cleanPrice = cleanPrice;
    }

    public void setAccruedInterest(BigDecimal accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    @Override
    public String toString() {
        return "Spot{" +
                "allInPrice=" + allInPrice +
                ", cleanPrice=" + cleanPrice +
                ", accruedInterest=" + accruedInterest +
                '}';
    }
}
