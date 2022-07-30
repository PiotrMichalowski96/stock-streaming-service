package com.piotr.stock.streaming.it.stepdefs;

import static com.piotr.stock.streaming.it.util.AwaitilityUtil.callUntil;
import static com.piotr.stock.streaming.it.util.JsonUtil.convertJsonArray;
import static com.piotr.stock.streaming.it.util.JsonUtil.readJsonArrayFile;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

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
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;


@Slf4j
@CucumberContextConfiguration
@ContextConfiguration(initializers = DockerContextInitializer.class)
public class StockStreamingServiceStepDefs extends KsqlDbIntegrationTest {

  private static final String STOCKS_PATH = "samples/stocks_to_populate.json";

  public StockStreamingServiceStepDefs(@Value("${kafka.server}") String bootstrap,
      @Value("${kafka.topic}") String topic) {
    super(bootstrap, topic);
  }

  //To keep response state between steps
  private Response response;

  @Before("@Ksql")
  public void sendStocksToTopic() throws IOException {
    List<StockEntity> stocksInTopic = readStocks();
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

  @When("user sends request to get stock with a ticker {string}")
  public void sendGetRequest(String ticker) {
    response = given()
        .queryParam("ticker", ticker)
        .when()
        .get("/stock");
  }

  @Then("stock service return response with status {int}")
  public void serviceReturnResponse(Integer expectedStatus) {
    assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
  }

  @Then("returned stock is equal to expected {string}")
  public void stockIsEqualToExpected(String expectedStockPath) throws IOException {
    List<Stock> expectedStocks = readJsonArrayFile(expectedStockPath, Stock.class);
    String stocksJson = response.getBody().asString();
    List<Stock> actualStocks = convertJsonArray(stocksJson, Stock.class);
    assertThat(actualStocks).hasSameSizeAs(expectedStocks);
    assertThat(actualStocks).hasSameElementsAs(expectedStocks);
  }
}
