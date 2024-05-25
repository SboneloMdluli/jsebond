package com.calculator.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bond {

  @Schema(
      name = "settlementDate",
      example = "2031-12-27",
      requiredMode = Schema.RequiredMode.REQUIRED)
  private LocalDate settlementDate;

  @Schema(name = "yieldToMaturity", example = "3.1", requiredMode = Schema.RequiredMode.REQUIRED)
  private double yieldToMaturity;

  public void setYieldToMaturity(double yieldToMaturity) {
    if (yieldToMaturity <= -200) {
      throw new IllegalArgumentException("Yield too small");
    }
    this.yieldToMaturity = yieldToMaturity;
  }

  @Autowired private BondInformation bondInformation;

}
