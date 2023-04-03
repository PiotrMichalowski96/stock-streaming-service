package com.piotr.stock.streaming.repository;

import static com.piotr.stock.streaming.entity.StockEntity.EXCHANGE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.STOCK_TYPE_COLUMN;
import static com.piotr.stock.streaming.entity.StockEntity.TICKER_COLUMN;

import com.piotr.stock.streaming.model.ApiRequestParams;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import lombok.Value;

@Value
public class StockQueryInfo {

  Map<String, String> queryParams;

  public StockQueryInfo(@NonNull ApiRequestParams apiRequestParams) {
    queryParams = new HashMap<>();
    addQueryInfo(queryParams, TICKER_COLUMN, apiRequestParams.ticker());
    addQueryInfo(queryParams, STOCK_TYPE_COLUMN, apiRequestParams.stockType());
    addQueryInfo(queryParams, EXCHANGE_COLUMN, apiRequestParams.exchange());
  }

  private void addQueryInfo(Map<String, String> queryParams, String column, String value) {
    if (value == null) {
      return;
    }
    queryParams.put(column, value);
  }
}
