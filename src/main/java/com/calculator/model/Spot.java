package com.calculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spot {
  private double allInPrice;
  private double cleanPrice;
  private double accruedInterest;
}
