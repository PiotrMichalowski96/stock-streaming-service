@Ksql
Feature: Testing a stock streaming REST API with Kafka. Users should be able to get stock from KSQL DB.

  Scenario Outline: Get correct stock from KSQL database filtering by ticker
    Given stocks are present in Kafka topic
    When user sends request to get stock with a ticker <Ticker>
    Then stock service return response with status <ResponseStatus>
    And returned stock is equal to expected <ExpectedStock>
    Examples:
      | Ticker    | ResponseStatus | ExpectedStock          |
      | "BTC/EUR" | 200            | "samples/stock_1.json" |
      | "IXIC"    | 200            | "samples/stock_2.json" |