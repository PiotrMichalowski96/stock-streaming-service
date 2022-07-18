package com.piotr.stock.streaming.route;

import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_MAPPING_DTO;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_MAPPING_SAVED_ENTITY;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_SENDING_TO_KAFKA;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.STEP_START_SAVE;
import static com.piotr.stock.streaming.route.common.RouteLogMessages.stepDoneMessage;
import static com.piotr.stock.streaming.route.common.RouteUtil.wrapProcessing;

import com.piotr.stock.streaming.mapper.StockMapper;
import com.piotr.stock.streaming.route.common.KafkaComponentUriBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SaveStockApiRoute extends RouteBuilder {

  private static final String ROUTE_ID = "save-stock-api-route";
  public static final String ROUTE_FROM = "direct:" + ROUTE_ID;

  private final String kafkaUri;
  private final StockMapper stockMapper;

  public SaveStockApiRoute(
      @Value("${kafka.server}") String server,
      @Value("${kafka.topic}") String topic,
      StockMapper stockMapper) {

    this.stockMapper = stockMapper;
    this.kafkaUri = new KafkaComponentUriBuilder(topic)
        .withBroker(server)
        .build();
  }

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

        .to(kafkaUri)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_SENDING_TO_KAFKA))

        .process(wrapProcessing(stockMapper::toStockDto))
        .id(STEP_MAPPING_SAVED_ENTITY)
        .log(LoggingLevel.INFO, logger, stepDoneMessage(STEP_MAPPING_SAVED_ENTITY));
  }
}
