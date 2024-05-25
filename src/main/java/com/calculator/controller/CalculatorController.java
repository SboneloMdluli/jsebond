package com.calculator.controller;

import com.calculator.model.Bond;
import com.calculator.model.Spot;
import com.calculator.service.JSEBondService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculatorController {
  @Autowired private JSEBondService userService;

  @Operation(summary = "Compute JSE Bond spot metrics")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successful operation",
            content = @Content(schema = @Schema(implementation = Spot.class)))
      })
  @PostMapping("/spot")
  public ResponseEntity<Spot> computeSpot(@RequestBody Bond bond) {

    userService.isSettlementDateValid(bond.getSettlementDate(),bond.getBondInformation());
    return new ResponseEntity<>(userService.calculateSpot(bond), HttpStatus.CREATED);
  }
}
