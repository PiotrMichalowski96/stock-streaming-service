package com.piotr.stock.streaming.route;

import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_MAPPING_DTO;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_MAPPING_SAVED_ENTITY;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_SAVE_STOCK;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_START_SAVE;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.stepDoneMessage;
import static com.piotr.stock.streaming.route.common.RouteUtil.wrapProcessing;

import com.piotr.stock.streaming.mapper.StockMapper;
import com.piotr.stock.streaming.repository.StockProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveStockApiRoute extends RouteBuilder {

  private static final String ROUTE_ID = "save-stock-api-route";
  public static final String ROUTE_FROM = "direct:" + ROUTE_ID;

  private final StockMapper stockMapper;
  private final StockProducer stockProducer;

  @Override
  public void configure() {
    from(ROUTE_FROM)
        .routeId(ROUTE_ID)
        .setExchangePattern(ExchangePattern.InOnly)
        .transacted()
        .id(STEP_START_SAVE)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_START_SAVE))

        .process(wrapProcessing(stockMapper::toStockEntity))
        .id(STEP_MAPPING_DTO)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_MAPPING_DTO))

        .process(wrapProcessing(stockProducer::save))
        .id(STEP_SAVE_STOCK)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_SAVE_STOCK))

        .process(wrapProcessing(stockMapper::toStockDto))
        .id(STEP_MAPPING_SAVED_ENTITY)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_MAPPING_SAVED_ENTITY));
  }
}
