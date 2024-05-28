package model;


import java.math.BigDecimal;
import java.time.LocalDate;

public class BondInformation {

    private BigDecimal coupon;
    private LocalDate maturityDate;
    private int redemptionAmount = 100;
    private LocalDate lastCouponDate;
    private LocalDate nextCouponDate;
    private LocalDate bookCloseDate1;
    private LocalDate bookCloseDate2;

    public BondInformation(BigDecimal coupon, LocalDate maturityDate, int redemptionAmount, LocalDate lastCouponDate, LocalDate nextCouponDate, LocalDate bookCloseDate1, LocalDate bookCloseDate2) {
        this.coupon = coupon;
        this.maturityDate = maturityDate;
        this.redemptionAmount = redemptionAmount;
        this.lastCouponDate = lastCouponDate;
        this.nextCouponDate = nextCouponDate;
        this.bookCloseDate1 = bookCloseDate1;
        this.bookCloseDate2 = bookCloseDate2;
    }

    public BondInformation() {
    }

    public static BondInformationBuilder builder() {
        return new BondInformationBuilder();
    }

    public void setCoupon(BigDecimal coupon) {
        if (coupon.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Coupon cannot be negative");
        }
        this.coupon = coupon;
    }

    public BigDecimal getCoupon() {
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

        if (!nextCouponDate.isAfter(getLastCouponDate()) ) {
            throw new IllegalArgumentException("Last and next coupon date are not ordered");
        }
        this.nextCouponDate = nextCouponDate;
    }

    public void setBookCloseDate1(LocalDate bookCloseDate1) {
        this.bookCloseDate1 = bookCloseDate1;
    }

    public void setBookCloseDate2(LocalDate bookCloseDate2) {
        if (!bookCloseDate2.isAfter(getBookCloseDate1())) {
            throw new IllegalArgumentException("Book close dates are not ordered");
        }
        this.bookCloseDate2 = bookCloseDate2;
    }

}
