# JSE BOND CALCULATOR
## Efficiency Mechanisms

Due to the nature of the problem being a computing one, there were no data structures used to store date for manipulation such as list, set, etc. The instructions in the calculations are represented as a sequence of conditions which cabe be executed in constant time.
Effort is made to simply the logic by not chaining if-statements which reduces the time the CPU processes instructions this is also know as [branch prediction](https://blog.cloudflare.com/branch-predictor).

By using the `BondInformationBuilder` class to build the `BondInformation` this ensures that the object constructed is immutable and as such will not require invocations of the garbage collector and addictionaly is [thread safe](https://www.leadingagile.com/2018/03/immutable-in-java/). Immutable objects are also cache efficient due to immutability.

The algorithm is constant in time but most of the overhead comes from object construction and access.

## Solution approach

The algorithm for the calculation has been developed and was made available as part of the assessment. Most effort was spent on design and robustness.
### Design
* The `BaseBond` class provides base implementation of common functionality which can be extended to the types of bonds reducing code duplication.
*  The `BondInformationBuilder` is provided to make object construction much easier and transparent. Which is preferred when constructing objects with optional parameters in this case `prround` and `RedemptionAmount` are optional parameters. The builder can be further extended to a fluent interface to enforce a construction sequence should need be.

### Robustness
* Attempt to divide by zero: The code throws and exception when the yield is set a number less than or equals to -200. This is consistent with the behavior of the online calculator.
*  Date validation : Book close date 1 is always less than Book close date 2. Coupon date 1 is always less than Coupon date 2. An exception is thrown when these conditions are not met.
*  An exception is thrown when a negative coupon is set. The minimum coupon we can have is zero which will give us a ZCB(zero coupon bond). Depending on the value of the yield to maturity we can have a bond trading par value i.e $yield-to-maturity=0$ and $all in price = clean price = 100$. Trading below par i.e $yield-to-maturity > 0$ and $all in price = clean price < 100$. Trading at a premium $yield-to-maturity < 0$ and $all in price = clean price > 100$. 
*  All these throw an `IllegalArgumentException` with a custom message.

## Data Structures

The Algorithm is executed in constant time as such no data structures were utilised in the development. Only custom ones were made to represent the object as per OOP design strategies.
