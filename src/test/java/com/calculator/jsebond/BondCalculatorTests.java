package com.calculator.jsebond;

import static org.junit.jupiter.api.Assertions.*;

import com.calculator.model.Bond;
import com.calculator.model.BondInformation;
import com.calculator.service.JSEBondService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** The JSE Bond calculator tests. */
class BondCalculatorTests {

  // Testing with R186, Yield=5, Settlement date = 21/07/2025, Nominal = 100, Maturity = 21/12/2026
  static Bond bond;
  static BondInformation bondInformation;

  static JSEBondService jSEBondService;

  @BeforeAll
  public static void setUp() {
    bond = new Bond();
    bondInformation = new BondInformation();
    jSEBondService = new JSEBondService();
  }

  /** Test dividing by zero i.e yield = -200. */
  @Test
  void testSetYieldToMaturity() {

    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bond.setYieldToMaturity(-200));
    assertEquals("Yield too small", exception.getMessage());
  }

  /** Test coupon initilisation. */
  @Test
  void testSetCoupon() {

    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bondInformation.setCoupon(-1));
    assertEquals("Coupon cannot be negative", exception.getMessage());
  }

  /** Test book close order. Book close date 1 is before book close 2 */
  @Test
  void testBookCloseInitilisation() {
    bond.setBondInformation(bondInformation);

    bond.setSettlementDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2026, 12, 21));

    Throwable exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> bondInformation.setBookCloseDate2(LocalDate.of(2026, 12, 11)));
    assertEquals("Book close dates are not ordered", exception.getMessage());
  }

  /** Test clean price. Clean price is the difference between all in price and accrued interest. */
  @Test
  void testCleanPrice() {
    assertEquals(0, jSEBondService.cleanPrice(1, 1));
  }

  /** Test if bond cumex. */
  @Test
  void testIsCumex() {
    assertTrue(jSEBondService.isCumex(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 5, 8)));
  }

  /** Test if bond is not cumex . */
  @Test
  void testIsCumexNegative() {
    assertFalse(jSEBondService.isCumex(LocalDate.of(2023, 1, 8), LocalDate.of(2020, 5, 8)));
  }

  /** Test days acc interest. */
  @Test
  void testDaysAccInterest() {
    bond.setBondInformation(bondInformation);

    bond.setSettlementDate(LocalDate.of(2025, 12, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2026, 6, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2026, 12, 11));
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }

  /**
   * Test days acc interest 2. If settlement date coincides LCD or NCD, days accrued interest is
   * zero. Settlement date is equal to LCD
   */
  @Test
  void testDaysAccInterestZeroLCD() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 6, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }

  /**
   * Test days acc interest 2. If settlement date coincides LCD or NCD, days accrued interest is
   * zero. Settlement date is equal to NCD
   */
  @Test
  void testDaysAccInterestZeroNCD() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 12, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }

  /** Test accrued interest, ex-interest */
  @Test
  void testDaysAccInterestNegative() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 6, 11));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));

    assertEquals(-10, jSEBondService.daysAccInterest(bond));
  }

  /** Test days accrued interest.cum-interest */
  @Test
  void testDaysAccInterestPositive() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 7, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(30, jSEBondService.daysAccInterest(bond));
  }

  /** Test broken period. */
  @Test
  void testBrokenPeriod() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 7, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    bondInformation.setMaturityDate(LocalDate.of(2027, 12, 21));
    assertEquals(0.8360655737704918, jSEBondService.brokenPeriod(bond));
  }

  /** Test accrued interest. */
  @Test
  void testAccruedInterest() {
    bond.setBondInformation(bondInformation);
    bondInformation.setCoupon(10.5);
    bond.setSettlementDate(LocalDate.of(2025, 7, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    bondInformation.setMaturityDate(LocalDate.of(2026, 12, 21));
    assertEquals(0.86301, jSEBondService.getAccruedInterest(bond));
  }

  /** Test bp factor. */
  @Test
  void testBpFactor() {
    bond.setBondInformation(bondInformation);
    bondInformation.setCoupon(10.5);
    bond.setYieldToMaturity(5);
    bond.setSettlementDate(LocalDate.of(2025, 7, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    bondInformation.setMaturityDate(LocalDate.of(2026, 12, 21));
    assertEquals(0.9795669984107146, jSEBondService.bpFactor(bond));
  }

  /** Test semi annual discount factor. */
  @Test
  void testSemiAnnualDiscountFactor() {
    assertEquals(1, jSEBondService.semiAnnualDiscountFactor(0));
  }
}
