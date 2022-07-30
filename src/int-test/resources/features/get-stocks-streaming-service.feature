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

  Scenario Outline: Get correct stock from KSQL database using all filters
    Given stocks are present in Kafka topic
    When user sends request to get stock with a ticker <Ticker>, stockType <StockType> and exchange <Exchange>
    Then stock service return response with status <ResponseStatus>
    And returned stock is equal to expected <ExpectedStock>
    Examples:
      | Ticker    | StockType           | Exchange | ResponseStatus | ExpectedStock          |
      | "BTC/EUR" | "Digital Currency"  | "NASDAQ" | 200            | "samples/stock_3.json" |
      | "USD/JPY" | "Physical Currency" | "NASDAQ" | 200            | "samples/stock_4.json" |

  Scenario Outline: Try to get stock from KSQL database with non-existing ID
    When user sends request to get stock with a ticker <Ticker>
    Then stock service return response with status <ResponseStatus>
    Examples:
      | Ticker    | ResponseStatus |
      | "BTC/PLN" | 204            |