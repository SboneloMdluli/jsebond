package model;

import java.time.LocalDate;

public class Bond {


    private LocalDate settlementDate;


    private double yieldToMaturity;

    public Bond(LocalDate settlementDate, double yieldToMaturity, BondInformation bondInformation) {
        this.settlementDate = settlementDate;
        this.yieldToMaturity = yieldToMaturity;
        this.bondInformation = bondInformation;
    }

    public Bond() {
    }

    public Bond(BondInformation bondInformation) {
        this.bondInformation = bondInformation;
    }

    public void setYieldToMaturity(double yieldToMaturity) {
        if (yieldToMaturity <= -200) {
            throw new IllegalArgumentException("Yield too small");
        }
        this.yieldToMaturity = yieldToMaturity;
    }

    private BondInformation bondInformation;

    public LocalDate getSettlementDate() {
        return this.settlementDate;
    }

    public double getYieldToMaturity() {
        return this.yieldToMaturity;
    }

    public BondInformation getBondInformation() {
        return this.bondInformation;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public void setBondInformation(BondInformation bondInformation) {
        this.bondInformation = bondInformation;
    }
}
