package com.piotr.stock.streaming.it.stepdefs;

import static com.piotr.stock.streaming.it.util.AwaitilityUtil.callUntil;
import static com.piotr.stock.streaming.it.util.JsonUtil.convertJson;
import static com.piotr.stock.streaming.it.util.JsonUtil.convertJsonArray;
import static com.piotr.stock.streaming.it.util.JsonUtil.readFileAsString;
import static com.piotr.stock.streaming.it.util.JsonUtil.readJsonArrayFile;
import static com.piotr.stock.streaming.it.util.JsonUtil.readJsonFile;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.it.common.KsqlDbIntegrationTest;
import com.piotr.stock.streaming.it.config.DockerContextInitializer;
import com.piotr.stock.streaming.rest.model.Stock;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.core.ConditionTimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;


@Slf4j
@CucumberContextConfiguration
@ContextConfiguration(initializers = DockerContextInitializer.class)
public class StockStreamingServiceStepDefs extends KsqlDbIntegrationTest {

  private static final String STOCKS_PATH = "samples/scenario_1/stocks_to_populate.json";

  public StockStreamingServiceStepDefs(@Value("${kafka.server}") String bootstrap,
      @Value("${kafka.topic}") String topic) {
    super(bootstrap, topic);
  }

  //To keep response state between steps
  private Response response;

  @Before("@Ksql")
  public void sendStocksToTopic() throws IOException {
    List<StockEntity> stocksInTopic = readStocksInTopic();
    if (stocksInTopic.isEmpty()) {
      List<StockEntity> stocksToSave = readJsonArrayFile(STOCKS_PATH, StockEntity.class);
      stocksToSave.forEach(this::sendStock);
    }
  }

  @Before
  public void initRestService() {
    RestAssured.baseURI = "http://localhost";
    RestAssured.basePath = "/v1/";
    RestAssured.port = 8080;
  }

  @Given("stocks are present in Kafka topic")
  public void stocksArePresentInKafkaTopic() {
    callUntil(this::readStocks, stocks -> !stocks.isEmpty());
  }

  @Given("stock {string} is not present in Kafka topic")
  public void stockIsNotPresentInTopic(String stockEntityPath) throws IOException {
    StockEntity stockToSave = readJsonFile(stockEntityPath, StockEntity.class);
    readStocksInTopic().forEach(stock -> assertThat(stock).usingRecursiveComparison().isNotEqualTo(stockToSave));
  }

  @When("user sends request to get stocks with a ticker {string}")
  public void sendGetRequest(String ticker) {
    response = given()
        .queryParam("ticker", ticker)
        .when()
        .get("/stock");
  }

  @When("user sends request to get stocks with a ticker {string}, stockType {string} and exchange {string}")
  public void sendGetRequest(String ticker, String stockType, String exchange) {
    response = given()
        .queryParam("ticker", ticker)
        .queryParam("stockType", stockType)
        .queryParam("exchange", exchange)
        .when()
        .get("/stock");
  }

  @When("user sends request to get stocks without filters")
  public void sendGetRequest() {
    response = given()
        .when()
        .get("/stock");
  }

  @When("user sends request to put stock {string} to Kafka topic")
  public void sendPostRequest(String stockDtoPath) throws IOException {
    String stockRequestBody = readFileAsString(stockDtoPath);
    response = given()
        .header("Content-type", "application/json")
        .and()
        .body(stockRequestBody)
        .when()
        .post("/stock");
  }

  @Then("stock service return response with status {int}")
  public void serviceReturnResponse(Integer expectedStatus) {
    assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
  }

  @Then("returned stocks are equal to expected {string}")
  public void stocksAreEqualToExpected(String expectedStockPath) throws IOException {
    List<Stock> expectedStocks = readJsonArrayFile(expectedStockPath, Stock.class);
    String stocksJson = response.getBody().asString();
    List<Stock> actualStocks = convertJsonArray(stocksJson, Stock.class);
    assertThat(actualStocks).hasSameSizeAs(expectedStocks);
    assertThat(actualStocks).hasSameElementsAs(expectedStocks);
  }

  @Then("returned {int} stocks")
  public void stockListHasExpectedSize(Integer stockListSize) throws JsonProcessingException {
    String stocksJson = response.getBody().asString();
    List<Stock> actualStocks = convertJsonArray(stocksJson, Stock.class);
    assertThat(actualStocks).hasSize(stockListSize);
  }

  @Then("returned stock is equal to expected {string}")
  public void stockIsEqualToExpected(String expectedStockDtoPath) throws IOException {
    Stock expectedStockDto = readJsonFile(expectedStockDtoPath, Stock.class);
    String stockJson = response.getBody().asString();
    Stock actualStockDto = convertJson(stockJson, Stock.class);
    assertThat(actualStockDto)
        .usingRecursiveComparison()
        .ignoringFields("stockTimestamp")
        .isEqualTo(expectedStockDto);
  }

  @Then("stock {string} is present in Kafka topic")
  public void stockIsPresentInTopic(String expectedStockEntityPath) throws IOException {
    StockEntity expectedStockEntity = readJsonFile(expectedStockEntityPath, StockEntity.class);
    List<StockEntity> stocksInTopic = readStocksInTopic();
    assertThat(stocksInTopic)
        .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "stockTimestamp")
        .contains(expectedStockEntity);
  }

  private List<StockEntity> readStocksInTopic() {
    try {
      return callUntil(this::readStocks, stocks -> !stocks.isEmpty());
    } catch (ConditionTimeoutException e) {
      return Collections.emptyList();
    }
  }
}
