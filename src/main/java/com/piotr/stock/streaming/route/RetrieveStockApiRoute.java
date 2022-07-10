package com.piotr.stock.streaming.route;

import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_FIND_STOCK;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_MAPPING_ENTITY;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_START_RETRIEVE;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.stepDoneMessage;

import com.piotr.stock.streaming.processor.EntityMappingProcessor;
import com.piotr.stock.streaming.processor.RetrieveStockProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RetrieveStockApiRoute extends RouteBuilder {

  private static final String ROUTE_ID = "retrieve-stock-api-route";
  public static final String ROUTE_FROM = "direct:" + ROUTE_ID;

  private final RetrieveStockProcessor retrieveStockProcessor;
  private final EntityMappingProcessor entityMappingProcessor;

  @Override
  public void configure() {

//    TODO: error handling
//    errorHandler(deadLetterChannel(ErrorHandlingRoute.ERROR_DIRECT_URI));

    from(ROUTE_FROM)
        .routeId(ROUTE_ID)
        .setExchangePattern(ExchangePattern.InOnly)
        .id(STEP_START_RETRIEVE)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_START_RETRIEVE))

        .bean(retrieveStockProcessor)
        .id(STEP_FIND_STOCK)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_FIND_STOCK))

        .bean(entityMappingProcessor)
        .id(STEP_MAPPING_ENTITY)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_MAPPING_ENTITY));

  }
}
