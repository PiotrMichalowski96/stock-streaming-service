package com.piotr.stock.streaming.model;

public record ApiRequestParams(String ticker, String stockType, String exchange) {}
