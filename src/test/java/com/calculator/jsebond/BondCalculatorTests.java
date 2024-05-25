package com.calculator.jsebond;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.calculator.model.Bond;
import com.calculator.model.BondInformation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BondCalculatorTests {

  @Test
  public void testSetYieldToMaturity() {
    Bond bond = new Bond();
    Throwable exception =
        assertThrows(IllegalArgumentException.class, () -> bond.setYieldToMaturity(-200));
    assertEquals("Yield too small", exception.getMessage());
  }

  @Test
  public void testSetCoupon() {
    BondInformation bondInformation = new BondInformation();
    Throwable exception =
            assertThrows(IllegalArgumentException.class, () -> bondInformation.setCoupon(-1));
    assertEquals("Coupon cannot be negative", exception.getMessage());
  }
}
