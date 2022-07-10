package com.piotr.stock.streaming.processor;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.mapper.StockMapper;
import com.piotr.stock.streaming.rest.model.Stock;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Handler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EntityMappingProcessor {

  private final StockMapper stockMapper;

  @Handler
  public List<Stock> process(List<StockEntity> stockEntityList) {
    return stockMapper.toStockDtoList(stockEntityList);
  }
}
