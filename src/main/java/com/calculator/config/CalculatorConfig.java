package com.calculator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Configuration
@Component
@ConfigurationProperties
@PropertySource(value = "classpath:application.properties")
public class CalculatorConfig {
  @Value("${preferences.pround:5}")
  public float pround;

  public float getPround() {
    return this.pround;
  }

  public void setPround(float pround) {
    this.pround = pround;
  }
}
