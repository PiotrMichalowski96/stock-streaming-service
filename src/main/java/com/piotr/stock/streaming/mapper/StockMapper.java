package com.piotr.stock.streaming.mapper;

import com.piotr.stock.streaming.entity.StockEntity;
import com.piotr.stock.streaming.rest.model.Stock;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public abstract class StockMapper {

  @Mapping(target = "quoteType", source = "stockType")
  @Mapping(target = "stockMarket", source = "exchange")
  @Mapping(target = "stockTimestamp", source = "stockTimestamp", qualifiedByName = "localDateToOffsetDateMapper")
  public abstract Stock toStockDto(StockEntity stockEntity);

  public abstract List<Stock> toStockDtoList(List<StockEntity> stockEntityList);

  @Mapping(target = "stockType", source = "quoteType")
  @Mapping(target = "exchange", source = "stockMarket")
  @Mapping(target = "stockTimestamp", source = "stockTimestamp", qualifiedByName = "offsetDateToLocalDateMapper")
  public abstract StockEntity toStockEntity(Stock stock);

  @Named("localDateToOffsetDateMapper")
  protected OffsetDateTime localDateToOffsetDateMapper(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    ZoneId zone = ZoneId.of("Europe/Warsaw");
    return localDateTime.atZone(zone).toOffsetDateTime();
  }

  @Named("offsetDateToLocalDateMapper")
  protected LocalDateTime offsetDateToLocalDateMapper(OffsetDateTime offsetDateTime) {
    if (offsetDateTime == null) {
      return null;
    }
    return offsetDateTime.toLocalDateTime();
  }
}
