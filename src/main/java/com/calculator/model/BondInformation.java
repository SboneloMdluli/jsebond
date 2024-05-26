package com.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public class BondInformation {
  @Schema(name = "coupon", example = "8.25", requiredMode = Schema.RequiredMode.REQUIRED)
  private double coupon;

  @Schema(
      name = "maturityDate",
      example = "2032-03-31",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate maturityDate;

  @Schema(
      name = "redemptionAmount",
      example = "100",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private int redemptionAmount = 100;

  @Schema(
      name = "lastCouponDate",
      example = "2031-09-30",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate lastCouponDate;

  @Schema(
      name = "nextCouponDate",
      example = "2032-03-31",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate nextCouponDate;

  @Schema(
      name = "bookCloseDate1",
      example = "2031-03-21",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate bookCloseDate1;

  @Schema(
      name = "bookCloseDate2",
      example = "2032-09-20",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate bookCloseDate2;

  public BondInformation(
      double coupon,
      LocalDate maturityDate,
      int redemptionAmount,
      LocalDate lastCouponDate,
      LocalDate nextCouponDate,
      LocalDate bookCloseDate1,
      LocalDate bookCloseDate2) {
    this.coupon = coupon;
    this.maturityDate = maturityDate;
    this.redemptionAmount = redemptionAmount;
    this.lastCouponDate = lastCouponDate;
    this.nextCouponDate = nextCouponDate;
    this.bookCloseDate1 = bookCloseDate1;
    this.bookCloseDate2 = bookCloseDate2;
  }

  public BondInformation() {}

  public static BondInformationBuilder builder() {
    return new BondInformationBuilder();
  }

  public void setCoupon(double coupon) {
    if (coupon < 0) {
      throw new IllegalArgumentException("Coupon cannot be negative");
    }
    this.coupon = coupon;
  }

  public double getCoupon() {
    return this.coupon;
  }

  public LocalDate getMaturityDate() {
    return this.maturityDate;
  }

  public int getRedemptionAmount() {
    return this.redemptionAmount;
  }

  public LocalDate getLastCouponDate() {
    return this.lastCouponDate;
  }

  public LocalDate getNextCouponDate() {
    return this.nextCouponDate;
  }

  public LocalDate getBookCloseDate1() {
    return this.bookCloseDate1;
  }

  public LocalDate getBookCloseDate2() {
    return this.bookCloseDate2;
  }

  public void setMaturityDate(LocalDate maturityDate) {
    this.maturityDate = maturityDate;
  }

  public void setRedemptionAmount(int redemptionAmount) {
    this.redemptionAmount = redemptionAmount;
  }

  public void setLastCouponDate(LocalDate lastCouponDate) {
    this.lastCouponDate = lastCouponDate;
  }

  public void setNextCouponDate(LocalDate nextCouponDate) {
    this.nextCouponDate = nextCouponDate;
  }

  public void setBookCloseDate1(LocalDate bookCloseDate1) {
    this.bookCloseDate1 = bookCloseDate1;
  }

  public void setBookCloseDate2(LocalDate bookCloseDate2) {
    this.bookCloseDate2 = bookCloseDate2;
  }

  public static class BondInformationBuilder {
    private double coupon;
    private LocalDate maturityDate;
    private int redemptionAmount;
    private LocalDate lastCouponDate;
    private LocalDate nextCouponDate;
    private LocalDate bookCloseDate1;
    private LocalDate bookCloseDate2;

    BondInformationBuilder() {}

    public BondInformationBuilder coupon(double coupon) {
      this.coupon = coupon;
      return this;
    }

    public BondInformationBuilder maturityDate(LocalDate maturityDate) {
      this.maturityDate = maturityDate;
      return this;
    }

    public BondInformationBuilder redemptionAmount(int redemptionAmount) {
      this.redemptionAmount = redemptionAmount;
      return this;
    }

    public BondInformationBuilder lastCouponDate(LocalDate lastCouponDate) {
      this.lastCouponDate = lastCouponDate;
      return this;
    }

    public BondInformationBuilder nextCouponDate(LocalDate nextCouponDate) {
      this.nextCouponDate = nextCouponDate;
      return this;
    }

    public BondInformationBuilder bookCloseDate1(LocalDate bookCloseDate1) {
      this.bookCloseDate1 = bookCloseDate1;
      return this;
    }

    public BondInformationBuilder bookCloseDate2(LocalDate bookCloseDate2) {
      this.bookCloseDate2 = bookCloseDate2;
      return this;
    }

    public BondInformation build() {
      return new BondInformation(
          coupon,
          maturityDate,
          redemptionAmount,
          lastCouponDate,
          nextCouponDate,
          bookCloseDate1,
          bookCloseDate2);
    }
  }
}
