# Loan rate calculation system

The loan rate calculation system calculates a monthly and total repayment based on the lenders rate.
The program takes two input parameters, the first is the market data file and the second is the loan amount.
The project was developed using TDD.

## Project dependencies
This project uses Kotlin version 1.2.20. Tests were written with JUnit4

## Running the application
To build the project, run:

```shell
gradlew clean build
```

There is a `market-data.csv` file included in the root of the project, edit this to change the output.

To run the application, from the root of the project run:
```shell
java -jar build/libs/loan-rate-service-1.0-SNAPSHOT.jar "market-data.csv" 1000
```

Inspect the console for the result

e.g
```shell
Requested amount: £1000
Rate: $7.1%
Monthly repayment: £29.82
Total repayment: £1073.51
```
