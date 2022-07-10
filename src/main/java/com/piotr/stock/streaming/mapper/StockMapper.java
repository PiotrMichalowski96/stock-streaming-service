package com.piotr.stock.streaming.mapper;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StockMapper {

  @Mapping(target = "quoteType", source = "stockType")
  @Mapping(target = "stockMarket", source = "exchange")
  @Mapping(target = "stockTimestamp", ignore = true)
  Stock toStockDto(StockEntity stockEntity);

  List<Stock> toStockDtoList(List<StockEntity> stockEntityList);
}
