package com.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

  public void setCoupon(double coupon) {
    if (coupon < 0) {
      throw new IllegalArgumentException("Coupon cannot be negative");
    }
    this.coupon = coupon;
  }

}
