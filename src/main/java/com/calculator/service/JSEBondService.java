package com.calculator.service;

import com.calculator.config.CalculatorConfig;
import com.calculator.model.Bond;
import com.calculator.model.BondInformation;
import com.calculator.model.Spot;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JSEBondService extends BaseBond {
  @Autowired CalculatorConfig calculatorConfig = new CalculatorConfig();
  private static final Logger log = LoggerFactory.getLogger(JSEBondService.class);
  private double ROUNDING_PRECISION = Math.pow(10, 5);

  public Spot calculateSpot(Bond bond) {
    init();
    Spot bondSpot = new Spot();
    double allInPrice = getAllInPrice(bond);
    double accruedInterest = getAccruedInterest(bond);
    bondSpot.setAccruedInterest(accruedInterest);
    bondSpot.setAllInPrice(allInPrice);
    bondSpot.setCleanPrice(cleanPrice(allInPrice, accruedInterest));
    return bondSpot;
  }

  public void init() {
    ROUNDING_PRECISION = Math.pow(10, calculatorConfig.getPround());
  }

  private boolean isCumex(Bond bond) {
    return isCumex(bond.getSettlementDate(), bond.getBondInformation().getBookCloseDate2())
        || daysAccInterest(bond) == 0;
  }

  public long daysAccInterest(Bond bond) {
    if (isCumex(bond.getSettlementDate(), bond.getBondInformation().getBookCloseDate2())) {
      return ChronoUnit.DAYS.between(
          bond.getBondInformation().getLastCouponDate(), bond.getSettlementDate());
    } else {
      return ChronoUnit.DAYS.between(
          bond.getBondInformation().getNextCouponDate(), bond.getSettlementDate());
    }
  }

  private double couponForNCD(Bond bond) {
    if (isCumex(bond)) {
      return bond.getBondInformation().getCoupon() / 2;
    } else {
      return 0;
    }
  }

  public double brokenPeriod(Bond bond) {
    final LocalDate nextCouponDate = bond.getBondInformation().getNextCouponDate();
    if (bond.getBondInformation().getMaturityDate().isEqual(nextCouponDate)) {
      return ChronoUnit.DAYS.between(bond.getSettlementDate(), nextCouponDate) / (365 / 2.0);
    } else {
      double nextCouponSettlementDiff =
          ChronoUnit.DAYS.between(bond.getSettlementDate(), nextCouponDate);
      double nextCouponLastCouponDiff =
          ChronoUnit.DAYS.between(bond.getBondInformation().getLastCouponDate(), nextCouponDate);
      return nextCouponSettlementDiff / nextCouponLastCouponDiff;
    }
  }

  public double bpFactor(Bond bond) {
    double semiAnnualFactor = semiAnnualDiscountFactor(bond.getYieldToMaturity());
    double brokenPeriod = brokenPeriod(bond);
    log.info("BP {}", brokenPeriod);
    if (bond.getBondInformation()
        .getMaturityDate()
        .isEqual(bond.getBondInformation().getNextCouponDate())) {
      return semiAnnualFactor / (semiAnnualFactor + brokenPeriod * (1 - semiAnnualFactor));
    } else {
      return Math.pow(semiAnnualFactor, brokenPeriod);
    }
  }

  public double getAccruedInterest(Bond bond) {
    return Math.round(
            ROUNDING_PRECISION
                * daysAccInterest(bond)
                * bond.getBondInformation().getCoupon()
                / 365)
        / ROUNDING_PRECISION;
  }

  public double getAllInPrice(Bond bond) {
    BondInformation bondInformation = bond.getBondInformation();
    double discountFactor = bpFactor(bond);
    double cpn = bondInformation.getCoupon() / 2;
    double cpnNCD = couponForNCD(bond);
    double semiAnnualDF = semiAnnualDiscountFactor(bond.getYieldToMaturity());
    double redemptionAmount = bond.getBondInformation().getRedemptionAmount();
    double daysToNextCoupon =
        numberOfDaysNCD(bondInformation.getMaturityDate(), bondInformation.getNextCouponDate());
    log.info("F {}", semiAnnualDF);
    log.info("BPF {}", discountFactor);
    log.info("N {}", daysToNextCoupon);
    if (semiAnnualDF != 1.0) {
      double ratio =
          ((cpn * semiAnnualDF) * (1 - Math.pow(semiAnnualDF, daysToNextCoupon)))
              / (1 - semiAnnualDF);
      double allInPrice =
          discountFactor
              * (cpnNCD + ratio + redemptionAmount * Math.pow(semiAnnualDF, daysToNextCoupon));

      return Math.round(ROUNDING_PRECISION * allInPrice) / ROUNDING_PRECISION;
    } else {
      return Math.round(ROUNDING_PRECISION * (cpnNCD + cpn * daysToNextCoupon + redemptionAmount))
          / ROUNDING_PRECISION;
    }
  }

  public void isSettlementDateValid(LocalDate settlementDate, BondInformation bondInformation) {
    if (!bondInformation.getMaturityDate().isEqual(settlementDate)) {
      if (bondInformation.getNextCouponDate().isEqual(bondInformation.getLastCouponDate())
          || bondInformation.getNextCouponDate().isBefore(bondInformation.getLastCouponDate())) {
        throw new IllegalArgumentException("Last and next coupon date are not ordered");
      }
    } else {
      if (!(settlementDate.isEqual(bondInformation.getLastCouponDate())
          && settlementDate.isEqual(bondInformation.getNextCouponDate()))) {
        throw new IllegalArgumentException(
            "Last and next coupon dates are not equal to maturity date");
      }
    }
  }

  public double cleanPrice(double allInPrice, double accruedInterest) {
    return Math.round(ROUNDING_PRECISION * (allInPrice - accruedInterest)) / ROUNDING_PRECISION;
  }
}
