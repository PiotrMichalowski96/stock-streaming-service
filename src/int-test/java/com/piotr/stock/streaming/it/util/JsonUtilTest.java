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
    String jsonPath = "samples/saved_stock.json";

    //when
    StockEntity stock = JsonUtil.readJsonFile(jsonPath, StockEntity.class);

    //then
    assertThat(stock).isNotNull();
  }

  @Test
  void shouldConvertJsonArrayWithManyElements() throws IOException {
    //given
    String jsonArrayPath = "samples/stocks_to_populate.json";

    //when
    List<StockEntity> stocks = JsonUtil.readJsonArrayFile(jsonArrayPath, StockEntity.class);

    //then
    assertThat(stocks).hasSize(2);
    stocks.forEach(stock -> assertThat(stock).isNotNull());
  }

  @Test
  void shouldConvertJsonArrayWithOneElement() throws IOException {
    //given
    String jsonArrayPath = "samples/stock_2.json";

    //when
    List<Stock> stocks = JsonUtil.readJsonArrayFile(jsonArrayPath, Stock.class);

    //then
    assertThat(stocks).hasSize(1);
    assertThat(stocks.get(0)).isNotNull();
  }
}