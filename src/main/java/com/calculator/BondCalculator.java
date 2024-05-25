package com.calculator;

import com.calculator.config.CalculatorConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CalculatorConfig.class)
public class BondCalculator {

  public static void main(String[] args) {
    SpringApplication.run(BondCalculator.class, args);
  }
}
