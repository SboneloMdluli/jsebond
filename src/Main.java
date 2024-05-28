import model.Bond;
import model.BondInformation;
import model.BondInformationBuilder;
import model.Spot;
import service.JSEBondCalculator;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        // the precision can be changed for the JSEBondCalculator by using the setPrecision method, it's set to 5 by default
        // the Redemption amount is set to 100 by default but can be changed using setRedemptionAmount from BondInformation class


        /*************************** Example 1 *************************************/
        /* create bond information */
        final BondInformation bondInformation = new BondInformationBuilder()
                .withCoupon(BigDecimal.valueOf(10.5))
                .withMaturityDate(LocalDate.of(2026, 12, 21))
                .withLastCouponDate(LocalDate.of(2016, 12, 21))
                .withNextCouponDate(LocalDate.of(2017, 6, 21))
                .withBookCloseDate1(LocalDate.of(2016, 6, 11))
                .withBookCloseDate2(LocalDate.of(2017, 12, 11))
                .build();

        /*create bond with given bond information*/
        final Bond bond = new Bond(bondInformation);
        bond.setSettlementDate(LocalDate.of(2017, 2, 7));
        bond.setYieldToMaturity(8.75);
        JSEBondCalculator jseBondService = new JSEBondCalculator();
        Spot spot = jseBondService.getSpotMetrics(bond);

        System.out.println(spot.toString());
        /***************************************************************************/

        System.out.println("****************************************************************");

        /*************************** Example 2 *************************************/
        /* create bond information */
        final BondInformation bondInformation1 = new BondInformationBuilder()
                .withCoupon(BigDecimal.valueOf(8.25))
                .withMaturityDate(LocalDate.of(2032, 3, 31))
                .withLastCouponDate(LocalDate.of(2024, 3, 31))
                .withNextCouponDate(LocalDate.of(2024, 9, 30))
                .withBookCloseDate1(LocalDate.of(2024, 3, 21))
                .withBookCloseDate2(LocalDate.of(2024, 9, 20))
                .build();


        /*Bond with different bond information*/
        final Bond bond1 = new Bond(bondInformation1);
        bond1.setSettlementDate(LocalDate.of(2024, 5, 16));
        bond1.setYieldToMaturity(9.5);
        Spot spot1 = jseBondService.getSpotMetrics(bond1);

        System.out.println(spot1.toString());
        /**********************************************************************/

    }
}