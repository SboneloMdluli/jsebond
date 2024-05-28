package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class BondInformationBuilder {
    private BigDecimal coupon;
    private LocalDate maturityDate;
    private int redemptionAmount = 100;
    private LocalDate lastCouponDate;
    private LocalDate nextCouponDate;
    private LocalDate bookCloseDate1;
    private LocalDate bookCloseDate2;

    public BondInformationBuilder withCoupon(BigDecimal coupon) {
        this.coupon = coupon;
        return this;
    }

    public BondInformationBuilder withMaturityDate(LocalDate maturityDate) {
        this.maturityDate = maturityDate;
        return this;
    }

    public BondInformationBuilder withRedemptionAmount(int redemptionAmount) {
        this.redemptionAmount = redemptionAmount;
        return this;
    }

    public BondInformationBuilder withLastCouponDate(LocalDate lastCouponDate) {
        this.lastCouponDate = lastCouponDate;
        return this;
    }

    public BondInformationBuilder withNextCouponDate(LocalDate nextCouponDate) {
        this.nextCouponDate = nextCouponDate;
        return this;
    }

    public BondInformationBuilder withBookCloseDate1(LocalDate bookCloseDate1) {
        this.bookCloseDate1 = bookCloseDate1;
        return this;
    }

    public BondInformationBuilder withBookCloseDate2(LocalDate bookCloseDate2) {
        this.bookCloseDate2 = bookCloseDate2;
        return this;
    }

    public BondInformation build() {
        BondInformation bondInformation = new BondInformation();
        bondInformation.setCoupon(coupon);
        bondInformation.setMaturityDate(maturityDate);
        bondInformation.setRedemptionAmount(redemptionAmount);
        bondInformation.setLastCouponDate(lastCouponDate);
        bondInformation.setNextCouponDate(nextCouponDate);
        bondInformation.setBookCloseDate1(bookCloseDate1);
        bondInformation.setBookCloseDate2(bookCloseDate2);
        return bondInformation;
    }
}
