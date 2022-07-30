@Ksql
Feature: Testing a stock streaming REST API with Kafka. Users should be able to get stock from KSQL DB.

  Scenario Outline: Get correct stocks from KSQL database filtering by ticker
    Given stocks are present in Kafka topic
    When user sends request to get stocks with a ticker <Ticker>
    Then stock service return response with status <ResponseStatus>
    And returned stocks are equal to expected <ExpectedStock>
    Examples:
      | Ticker    | ResponseStatus | ExpectedStock          |
      | "BTC/EUR" | 200            | "samples/stock_1.json" |
      | "IXIC"    | 200            | "samples/stock_2.json" |

  Scenario Outline: Get correct stocks from KSQL database using all filters
    Given stocks are present in Kafka topic
    When user sends request to get stocks with a ticker <Ticker>, stockType <StockType> and exchange <Exchange>
    Then stock service return response with status <ResponseStatus>
    And returned stocks are equal to expected <ExpectedStock>
    Examples:
      | Ticker    | StockType           | Exchange | ResponseStatus | ExpectedStock          |
      | "BTC/EUR" | "Digital Currency"  | "NASDAQ" | 200            | "samples/stock_3.json" |
      | "USD/JPY" | "Physical Currency" | "NASDAQ" | 200            | "samples/stock_4.json" |

  Scenario Outline: Get stock list from KSQL database without any filters
    Given stocks are present in Kafka topic
    When user sends request to get stocks without filters
    Then stock service return response with status <ResponseStatus>
    And returned <ExpectedStockListSize> stocks
    Examples:
      | ResponseStatus | ExpectedStockListSize |
      | 200            | 10                    |

  Scenario Outline: Try to get stocks from KSQL database with non-existing ID
    When user sends request to get stocks with a ticker <Ticker>
    Then stock service return response with status <ResponseStatus>
    Examples:
      | Ticker    | ResponseStatus |
      | "BTC/PLN" | 204            |