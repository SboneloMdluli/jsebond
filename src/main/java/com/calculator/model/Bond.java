package com.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public class Bond {

  @Schema(
      name = "settlementDate",
      example = "2031-12-27",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate settlementDate;

  @Schema(name = "yieldToMaturity", example = "3.1", requiredMode = Schema.RequiredMode.REQUIRED)
  private double yieldToMaturity;

  public Bond(LocalDate settlementDate, double yieldToMaturity, BondInformation bondInformation) {
    this.settlementDate = settlementDate;
    this.yieldToMaturity = yieldToMaturity;
    this.bondInformation = bondInformation;
  }

  public Bond() {}

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
