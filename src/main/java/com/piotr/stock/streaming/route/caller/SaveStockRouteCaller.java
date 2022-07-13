package com.piotr.stock.streaming.route.caller;

import com.piotr.stock.streaming.rest.model.Stock;

public interface SaveStockRouteCaller {
  Stock callRoute(Stock stock);
}
