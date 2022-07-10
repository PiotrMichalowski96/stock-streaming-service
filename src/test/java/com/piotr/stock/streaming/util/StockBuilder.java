package com.piotr.stock.streaming.util;

import com.piotr.stock.streaming.rest.model.Stock;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class StockBuilder {

  private final Stock stock;

  public StockBuilder() {
    stock = new Stock();
  }

  public StockBuilder withTicker(String ticker) {
    stock.setTicker(ticker);
    return this;
  }

  public StockBuilder withQuoteType(String quoteType) {
    stock.setQuoteType(quoteType);
    return this;
  }

  public StockBuilder withStockMarket(String stockMarket) {
    stock.setStockMarket(stockMarket);
    return this;
  }

  public StockBuilder withPrice(BigDecimal price) {
    stock.setPrice(price);
    return this;
  }

  public StockBuilder withCurrency(String currency) {
    stock.setCurrency(currency);
    return this;
  }

  public StockBuilder withVolume(Long volume) {
    stock.setVolume(volume);
    return this;
  }

  public StockBuilder withStockTimestamp(OffsetDateTime stockTimestamp) {
    stock.setStockTimestamp(stockTimestamp);
    return this;
  }

  public Stock build() {
    return stock;
  }
}
