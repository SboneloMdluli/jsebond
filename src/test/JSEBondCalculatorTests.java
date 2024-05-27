package test;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

import model.Bond;
import model.BondInformation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.JSEBondCalculator;

/** The JSE Bond calculator tests. */
class JSEBondCalculatorTests {

  // Testing with R186, Yield=5, Settlement date = 21/07/2025, Nominal = 100, Maturity = 21/12/2026
  static Bond bond;
  static BondInformation bondInformation;

  static JSEBondCalculator jSEBondService;

  @BeforeAll
  public static void setUp() {
    bond = new Bond();
    bondInformation = new BondInformation();
    jSEBondService = new JSEBondCalculator();
  }

  /** Test dividing by zero i.e yield = -200. */
  @Test
  @DisplayName("Test attempt to divide by zero")
  void testSetYieldToMaturity() {

    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bond.setYieldToMaturity(-200));
    assertEquals("Yield too small", exception.getMessage());
  }

  /** Test coupon initilisation. */
  @DisplayName("Test coupon value")
  @Test
  void testSetCoupon() {

    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bondInformation.setCoupon(-1));
    assertEquals("Coupon cannot be negative", exception.getMessage());
  }

  /** Test book close order. Book close date 1 is before book close 2 */
  @Test
  @DisplayName("Test book close date order")
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
  @DisplayName("Test clean price")
  void testCleanPrice() {
    assertEquals(0, jSEBondService.cleanPrice(1, 1));
  }

  /** Test if bond cumex. */
  @Test
  @DisplayName("Positive test for cum-ex interest i.e true is cum-interest")
  void testIsCumex() {
    assertTrue(jSEBondService.isCumex(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 5, 8)));
  }

  /** Test if bond is not cumex . */
  @Test
  @DisplayName("Negative test for cum-ex interest i.e false is ex-interest")
  void testIsCumexNegative() {
    assertFalse(jSEBondService.isCumex(LocalDate.of(2023, 1, 8), LocalDate.of(2020, 5, 8)));
  }

  /** Test days acc interest. */
  @Test
  @DisplayName("Test for number days bond accrued interest. S=LCD")
  void testDaysAccInterest() {
    bond.setBondInformation(bondInformation);

    bond.setSettlementDate(LocalDate.of(2025, 12, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2026, 6, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 6, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2026, 12, 11));
    //S=LCD therefore the bond did not accurue any interest
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }

  /**
   * Test days acc interest 2. If settlement date coincides LCD or NCD, days accrued interest is
   * zero. Settlement date is equal to LCD
   */
  @Test
  @DisplayName("Test accrued interest, ex-interest. S=LCD")
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
  @DisplayName("Test accrued interest, ex-interest. S=NCD")
  void testDaysAccInterestZeroNCD() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 12, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }


  @Test
  @DisplayName("Test accrued interest, ex-interest")
  void testDaysAccInterestNegative() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 6, 11));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));

    assertEquals(-10, jSEBondService.daysAccInterest(bond));
  }


  @Test
  @DisplayName("Test days accrued interest.cum-interest")
  void testDaysAccInterestPositive() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 7, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 6, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate1(LocalDate.of(2025, 06, 11));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(30, jSEBondService.daysAccInterest(bond));
  }


  @Test
  @DisplayName("Test broken period")
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

  @Test
  @DisplayName("Test accrued interest")
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


  @Test
  @DisplayName("Test for break point factor")
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


  @Test
  @DisplayName("Test semi annual discount factor")
  void testSemiAnnualDiscountFactor() {
    assertEquals(1, jSEBondService.semiAnnualDiscountFactor(0));
  }
}
