package com.piotr.stock.streaming.rest.handler;

import com.piotr.stock.streaming.model.ApiRequestParams;
import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.rest.model.Stock;
import com.piotr.stock.streaming.route.RetrieveStockApiRoute;
import com.piotr.stock.streaming.route.SaveStockApiRoute;
import com.piotr.stock.streaming.route.caller.RetrieveStockRouteCaller;
import com.piotr.stock.streaming.route.caller.SaveStockRouteCaller;
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

  @Produce(SaveStockApiRoute.ROUTE_FROM)
  protected SaveStockRouteCaller saveStockRouteCaller;

  @Override
  public ResponseEntity<Stock> addStock(Stock body) {

    logger.debug("Stock to save: {}", body);

    Stock savedStock = saveStockRouteCaller.callRoute(body);

    logger.debug("Saved stock: {}", savedStock);

    return ResponseEntity.ok(savedStock);
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
