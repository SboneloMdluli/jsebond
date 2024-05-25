package com.calculator.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

abstract class BaseBond {
  public double numberOfDaysNCD(LocalDate maturityDate, LocalDate nextCouponDate) {
    return ChronoUnit.DAYS.between(nextCouponDate, maturityDate) / (365.25 / 2);
  }
  public boolean isCumex(LocalDate settlementDate, LocalDate bookCloseDate2) {
    return settlementDate.isBefore(bookCloseDate2);
  }
  public double semiAnnualDiscountFactor(double maturity) {
    return (1 / (1 + maturity / 200));
  }
}
