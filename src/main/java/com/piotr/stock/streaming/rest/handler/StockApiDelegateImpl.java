package com.piotr.stock.streaming.rest.handler;

import com.piotr.stock.streaming.model.ApiRequestParams;
import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.route.RetrieveStockApiRoute;
import com.piotr.stock.streaming.route.caller.RetrieveStockRouteCaller;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Produce;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockApiDelegateImpl implements StockApiDelegate {

  @Produce(RetrieveStockApiRoute.ROUTE_FROM)
  protected RetrieveStockRouteCaller retrieveStockRouteCaller;

  @Override
  public ResponseEntity<Void> addStock(Stock body) {
    //TODO: support POST method
    return StockApiDelegate.super.addStock(body);
  }

  @Override
  public ResponseEntity<List<Stock>> stock(String ticker, String stockType, String exchange) {
    ApiRequestParams apiRequestParams = new ApiRequestParams(ticker, stockType, exchange);

    logger.debug("API request params: {}", apiRequestParams);

    StockQueryInfo stockQueryInfo = new StockQueryInfo(apiRequestParams);

    List<Stock> stocks = retrieveStockRouteCaller.callRoute(stockQueryInfo);

    stocks.forEach(stock -> logger.debug("Found stock: {}", stock));

    if(stocks.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return ResponseEntity.ok(stocks);
  }
}
