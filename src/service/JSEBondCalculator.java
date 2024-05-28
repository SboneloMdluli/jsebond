package service;

import model.Bond;
import model.BondInformation;
import model.Spot;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class JSEBondCalculator extends BaseBond {
    private double ROUNDING_PRECISION = Math.pow(10, 5);
    MathContext m = new MathContext(20);

    /**
     * Calculate bond spot.
     *
     * @param bond the bond
     * @return the spot
     */
    public Spot getSpotMetrics(Bond bond) {
        Spot bondSpot = new Spot();
        BigDecimal allInPrice = getAllInPrice(bond);
        BigDecimal accruedInterest = getAccruedInterest(bond);
        bondSpot.setAccruedInterest(accruedInterest);
        bondSpot.setAllInPrice(allInPrice);
        bondSpot.setCleanPrice(cleanPrice(allInPrice, accruedInterest));
        return bondSpot;
    }

    /**
     * Set the number of decimal places for the calculator
     *
     * @param digits
     */
    public void setPrecision(int digits) {
        ROUNDING_PRECISION = Math.pow(10, digits);
    }

    /**
     * Check if bond is cum-interest or ex-interest. <br>
     * The bond is cum-interest if the settlement date coincides with one of the bond's coupon payment
     * dates
     *
     * @param bond the bond
     * @return Returns true if the bond is cum-interest and false if ex-interest.
     */
    private boolean isCumex(Bond bond) {
        return isCumex(bond.getSettlementDate(), bond.getBondInformation().getBookCloseDate2()) || daysAccInterest(bond) == 0;
    }

    /**
     * Days with accrued interest.
     *
     * @param bond the bond
     * @return The number of days accrued interest
     */
    public long daysAccInterest(Bond bond) {
        if (isCumex(bond.getSettlementDate(), bond.getBondInformation().getBookCloseDate2())) {
            return ChronoUnit.DAYS.between(bond.getBondInformation().getLastCouponDate(), bond.getSettlementDate());
        } else {
            return ChronoUnit.DAYS.between(bond.getBondInformation().getNextCouponDate(), bond.getSettlementDate());
        }
    }

    private BigDecimal couponForNCD(Bond bond) {
        if (isCumex(bond)) {
            return bond.getBondInformation().getCoupon().divide(BigDecimal.valueOf(2),m);
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Calculate bond broken period.
     *
     * @param bond JSE bond
     * @return broken period
     */
    public BigDecimal brokenPeriod(Bond bond) {
        final LocalDate nextCouponDate = bond.getBondInformation().getNextCouponDate();
        if (bond.getBondInformation().getMaturityDate().isEqual(nextCouponDate)) {
            return BigDecimal.valueOf(ChronoUnit.DAYS.between(bond.getSettlementDate(), nextCouponDate) / (365 / 2.0));
        } else {
            double nextCouponSettlementDiff = ChronoUnit.DAYS.between(bond.getSettlementDate(), nextCouponDate);
            double nextCouponLastCouponDiff = ChronoUnit.DAYS.between(bond.getBondInformation().getLastCouponDate(), nextCouponDate);
            return BigDecimal.valueOf(nextCouponSettlementDiff).divide(BigDecimal.valueOf(nextCouponLastCouponDiff),m) ;
        }
    }

    /**
     * Calculate broken period factor.
     *
     * @param bond JSE bond
     * @return broken period factor
     */
    public BigDecimal bpFactor(Bond bond) {
        BigDecimal semiAnnualFactor = semiAnnualDiscountFactor(bond.getYieldToMaturity());
        BigDecimal brokenPeriod = brokenPeriod(bond);
        if (bond.getBondInformation().getMaturityDate().isEqual(bond.getBondInformation().getNextCouponDate())) {
            return semiAnnualFactor.divide(semiAnnualFactor.add(brokenPeriod.multiply((BigDecimal.valueOf(1).subtract(semiAnnualFactor)))),m);
        } else {
            return BigDecimal.valueOf((Math.pow(semiAnnualFactor.doubleValue(),brokenPeriod.doubleValue())));
        }
    }

    /**
     * Get bond accrued interest.
     *
     * @param bond JSE bond
     * @return accrued interest
     */
    public BigDecimal getAccruedInterest(Bond bond) {
       return BigDecimal.valueOf(daysAccInterest(bond)).multiply(bond.getBondInformation().getCoupon()).divide(BigDecimal.valueOf(365),m);
    }

    /**
     * Get bond all in price.
     *
     * @param bond JSE bond
     * @return all in price
     */
    public BigDecimal getAllInPrice(Bond bond) {
        BondInformation bondInformation = bond.getBondInformation();
        BigDecimal discountFactor = bpFactor(bond);
        BigDecimal cpn = bondInformation.getCoupon().divide(    BigDecimal.valueOf(2),m);
        BigDecimal cpnNCD = couponForNCD(bond);
        BigDecimal semiAnnualDF = semiAnnualDiscountFactor(bond.getYieldToMaturity());
        double redemptionAmount = bond.getBondInformation().getRedemptionAmount();
        double daysToNextCoupon = numberOfDaysNCD(bondInformation.getMaturityDate(), bondInformation.getNextCouponDate());
        if (!semiAnnualDF.equals(BigDecimal.ONE)) {
            BigDecimal ratio = cpn.multiply(semiAnnualDF).multiply((BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(Math.pow(semiAnnualDF.doubleValue(), daysToNextCoupon)))).divide(BigDecimal.valueOf(1).subtract(semiAnnualDF),m));
            BigDecimal allInPrice = discountFactor.multiply((cpnNCD.add(ratio).add(BigDecimal.valueOf(redemptionAmount * Math.pow(semiAnnualDF.doubleValue(), daysToNextCoupon)))));
            return allInPrice;
        } else {
            return cpnNCD.add(cpn.multiply(BigDecimal.valueOf(daysToNextCoupon))).add(BigDecimal.valueOf(redemptionAmount));
        }
    }



    /**
     * Get bond clean price.
     *
     * @param allInPrice the all in price
     * @param accruedInterest the accrued interest
     * @return clean price
     */
    public BigDecimal cleanPrice(BigDecimal allInPrice, BigDecimal accruedInterest) {
        return allInPrice.subtract(accruedInterest);
    }
}
