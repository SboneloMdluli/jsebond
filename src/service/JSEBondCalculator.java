package service;

import model.Bond;
import model.BondInformation;
import model.Spot;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class JSEBondCalculator extends BaseBond {
    private double ROUNDING_PRECISION = Math.pow(10, 5);

    /**
     * Calculate bond spot.
     *
     * @param bond the bond
     * @return the spot
     */
    public Spot getSpotMetrics(Bond bond) {
        Spot bondSpot = new Spot();
        double allInPrice = getAllInPrice(bond);
        double accruedInterest = getAccruedInterest(bond);
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

    private double couponForNCD(Bond bond) {
        if (isCumex(bond)) {
            return bond.getBondInformation().getCoupon() / 2;
        } else {
            return 0;
        }
    }

    /**
     * Calculate bond broken period.
     *
     * @param bond JSE bond
     * @return broken period
     */
    public double brokenPeriod(Bond bond) {
        final LocalDate nextCouponDate = bond.getBondInformation().getNextCouponDate();
        if (bond.getBondInformation().getMaturityDate().isEqual(nextCouponDate)) {
            return ChronoUnit.DAYS.between(bond.getSettlementDate(), nextCouponDate) / (365 / 2.0);
        } else {
            double nextCouponSettlementDiff = ChronoUnit.DAYS.between(bond.getSettlementDate(), nextCouponDate);
            double nextCouponLastCouponDiff = ChronoUnit.DAYS.between(bond.getBondInformation().getLastCouponDate(), nextCouponDate);
            return nextCouponSettlementDiff / nextCouponLastCouponDiff;
        }
    }

    /**
     * Calculate broken period factor.
     *
     * @param bond JSE bond
     * @return broken period factor
     */
    public double bpFactor(Bond bond) {
        double semiAnnualFactor = semiAnnualDiscountFactor(bond.getYieldToMaturity());
        double brokenPeriod = brokenPeriod(bond);
        if (bond.getBondInformation().getMaturityDate().isEqual(bond.getBondInformation().getNextCouponDate())) {
            return semiAnnualFactor / (semiAnnualFactor + brokenPeriod * (1 - semiAnnualFactor));
        } else {
            return Math.pow(semiAnnualFactor, brokenPeriod);
        }
    }

    /**
     * Get bond accrued interest.
     *
     * @param bond JSE bond
     * @return accrued interest
     */
    public double getAccruedInterest(Bond bond) {
        return Math.round(ROUNDING_PRECISION * daysAccInterest(bond) * bond.getBondInformation().getCoupon() / 365) / ROUNDING_PRECISION;
    }

    /**
     * Get bond all in price.
     *
     * @param bond JSE bond
     * @return all in price
     */
    public double getAllInPrice(Bond bond) {
        BondInformation bondInformation = bond.getBondInformation();
        double discountFactor = bpFactor(bond);
        double cpn = bondInformation.getCoupon() / 2;
        double cpnNCD = couponForNCD(bond);
        double semiAnnualDF = semiAnnualDiscountFactor(bond.getYieldToMaturity());
        double redemptionAmount = bond.getBondInformation().getRedemptionAmount();
        double daysToNextCoupon = numberOfDaysNCD(bondInformation.getMaturityDate(), bondInformation.getNextCouponDate());
        if (semiAnnualDF != 1.0) {
            double ratio = ((cpn * semiAnnualDF) * (1 - Math.pow(semiAnnualDF, daysToNextCoupon))) / (1 - semiAnnualDF);
            double allInPrice = discountFactor * (cpnNCD + ratio + (redemptionAmount * Math.pow(semiAnnualDF, daysToNextCoupon)));

            return Math.round(ROUNDING_PRECISION * allInPrice) / ROUNDING_PRECISION;
        } else {
            return Math.round(ROUNDING_PRECISION * (cpnNCD + cpn * daysToNextCoupon + redemptionAmount)) / ROUNDING_PRECISION;
        }
    }



    /**
     * Get bond clean price double.
     *
     * @param allInPrice      the all in price
     * @param accruedInterest the accrued interest
     * @return clean price
     */
    public double cleanPrice(double allInPrice, double accruedInterest) {
        return Math.round(ROUNDING_PRECISION * (allInPrice - accruedInterest)) / ROUNDING_PRECISION;
    }
}
