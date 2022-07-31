Feature: Testing a stock streaming REST API with Kafka. Users should be able to send stock to Kafka topic.

  Scenario Outline: Send stock to Kafka topic
    Given stock <StockToSave> is not present in Kafka topic
    When user sends request to put stock <StockDto> to Kafka topic
    Then stock service return response with status <ResponseStatus>
    And returned stock is equal to expected <StockDto>
    And stock <StockToSave> is present in Kafka topic
    Examples:
      | StockToSave                               | StockDto                              | ResponseStatus |
      | "samples/scenario_2/stock_to_save_1.json" | "samples/scenario_2/stock_dto_1.json" | 200            |
