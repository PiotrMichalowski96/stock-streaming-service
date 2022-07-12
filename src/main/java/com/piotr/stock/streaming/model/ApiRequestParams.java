package com.piotr.stock.streaming.model;

public record ApiRequestParams(String ticker, String stockType, String exchange) {
  public ApiRequestParams() {
    this(null, null, null);
  }
}
