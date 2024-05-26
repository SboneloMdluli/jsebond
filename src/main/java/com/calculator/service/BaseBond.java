package com.calculator.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

abstract class BaseBond {
  /**
   * Get number of remaining coupon dates.
   *
   * @param maturityDate the maturity date
   * @param nextCouponDate the next coupon date
   * @return remaining coupon dates
   */
  public double numberOfDaysNCD(LocalDate maturityDate, LocalDate nextCouponDate) {
    return Math.round(ChronoUnit.DAYS.between(nextCouponDate, maturityDate) / (365.25 / 2));
  }

  /**
   * Check if bond is cum-interest or ex-interest.
   *
   * @param settlementDate the settlement date
   * @param bookCloseDate2 the book close date 2
   * @return Returns true if the bond is cum-interest and false if ex-interest.
   */
  public boolean isCumex(LocalDate settlementDate, LocalDate bookCloseDate2) {
    return settlementDate.isBefore(bookCloseDate2);
  }

  /**
   * Determine the semi-annual discount factor, F for a given yield.
   *
   * @param maturity the maturity
   * @return semi annual discount factor
   */
  public double semiAnnualDiscountFactor(double maturity) {
    return (1 / (1 + maturity / 200));
  }
}
