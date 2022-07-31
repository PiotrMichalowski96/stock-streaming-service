package com.piotr.stock.streaming.it.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

  @Test
  void shouldConvertJson() throws IOException {
    //given
    String jsonPath = "samples/scenario_2/stock_to_save_1.json";

    //when
    StockEntity stock = JsonUtil.readJsonFile(jsonPath, StockEntity.class);

    //then
    assertThat(stock).isNotNull();
  }

  @Test
  void shouldConvertJsonArrayWithManyElements() throws IOException {
    //given
    String jsonArrayPath = "samples/scenario_1/stocks_to_populate.json";

    //when
    List<StockEntity> stocks = JsonUtil.readJsonArrayFile(jsonArrayPath, StockEntity.class);

    //then
    assertThat(stocks).hasSize(11);
    stocks.forEach(stock -> assertThat(stock).isNotNull());
  }

  @Test
  void shouldConvertJsonArrayWithOneElement() throws IOException {
    //given
    String jsonArrayPath = "samples/scenario_1/stock_2.json";

    //when
    List<Stock> stocks = JsonUtil.readJsonArrayFile(jsonArrayPath, Stock.class);

    //then
    assertThat(stocks).hasSize(1);
    assertThat(stocks.get(0)).isNotNull();
  }
}