package com.calculator.jsebond;

import static org.junit.jupiter.api.Assertions.*;

import com.calculator.model.Bond;
import com.calculator.model.BondInformation;
import com.calculator.service.JSEBondService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BondCalculatorTests {
  // Testing with R186, Yield=5, Settlement dat = 21/07/2025, Nominal = 100
  static Bond bond;
  static BondInformation bondInformation;
  static JSEBondService jSEBondService;

  @BeforeAll
  public static void setUp() {
    bond = new Bond();
    bondInformation = new BondInformation();
    jSEBondService = new JSEBondService();
  }

  @Test
  void testSetYieldToMaturity() {

    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bond.setYieldToMaturity(-200));
    assertEquals("Yield too small", exception.getMessage());
  }

  @Test
  void testSetCoupon() {

    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bondInformation.setCoupon(-1));
    assertEquals("Coupon cannot be negative", exception.getMessage());
  }

  @Test
  void testCleanPrice() {
    assertEquals(0, jSEBondService.cleanPrice(1, 1));
  }

  @Test
  void testIsCumex() {
    assertTrue(jSEBondService.isCumex(LocalDate.of(2020, 10, 8), LocalDate.of(2021, 5, 8)));
  }

  @Test
  void testIsCumexNegative() {
    assertFalse(jSEBondService.isCumex(LocalDate.of(2023, 1, 8), LocalDate.of(2020, 5, 8)));
  }

  @Test
  void testDaysAccInterest() {
    bond.setBondInformation(bondInformation);

    bond.setSettlementDate(LocalDate.of(2025, 12, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2026, 06, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2026, 12, 11));
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }

  @Test
  void testDaysAccInterest2() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 06, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 06, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(0, jSEBondService.daysAccInterest(bond));
  }

  @Test
  void testDaysAccInterest3() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 06, 11));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 06, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(-10, jSEBondService.daysAccInterest(bond));
  }

  @Test
  void testDaysAccInterest4() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 07, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 06, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    assertEquals(30, jSEBondService.daysAccInterest(bond));
  }

  @Test
  void testBrokenPeriod() {
    bond.setBondInformation(bondInformation);
    bond.setSettlementDate(LocalDate.of(2025, 07, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 06, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    bondInformation.setMaturityDate(LocalDate.of(2027, 12, 21));
    assertEquals(0.8360655737704918, jSEBondService.brokenPeriod(bond));
  }

  @Test
  void testAccruedInterest() {
    bond.setBondInformation(bondInformation);
    bondInformation.setCoupon(10.5);
    bond.setSettlementDate(LocalDate.of(2025, 07, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 06, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    bondInformation.setMaturityDate(LocalDate.of(2027, 12, 21));
    assertEquals(0.86301, jSEBondService.getAccruedInterest(bond));
  }

  @Test
  void testBpFactor() {
    bond.setBondInformation(bondInformation);
    bondInformation.setCoupon(10.5);
    bond.setYieldToMaturity(5);
    bond.setSettlementDate(LocalDate.of(2025, 07, 21));
    bondInformation.setLastCouponDate(LocalDate.of(2025, 06, 21));
    bondInformation.setNextCouponDate(LocalDate.of(2025, 12, 21));
    bondInformation.setBookCloseDate2(LocalDate.of(2025, 12, 11));
    bondInformation.setMaturityDate(LocalDate.of(2027, 12, 21));
    assertEquals(0.9795669984107146, jSEBondService.bpFactor(bond));
  }

  @Test
  void testSemiAnnualDiscountFactor() {
    assertEquals(1, jSEBondService.semiAnnualDiscountFactor(0));
  }
}
