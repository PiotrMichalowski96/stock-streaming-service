package com.piotr.stock.streaming.route.caller;

import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.rest.model.Stock;
import java.util.List;

public interface RetrieveStockRouteCaller {
  List<Stock> callRoute(StockQueryInfo stockQueryInfo);
}
