package com.piotr.stock.streaming.processor;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.repository.StockQueryInfo;
import com.piotr.stock.streaming.repository.StockRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RetrieveStockProcessor {

  private final StockRepository stockRepository;

  @Handler
  public List<StockEntity> process(StockQueryInfo stockQueryInfo) {
    if(stockQueryInfo.getQueryParams().isEmpty()) {
      return stockRepository.findStockLimited();
    }
    return stockRepository.findStockList(stockQueryInfo);
  }
}
