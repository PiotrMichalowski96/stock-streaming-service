package com.piotr.stock.streaming.processor;

import com.piotr.stock.streaming.entity.StockEntity;
import java.util.Optional;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

@Component
public class KeySettingProcessor implements Processor {

  @Override
  public void process(Exchange exchange) {
    Optional.ofNullable(exchange.getIn().getBody(StockEntity.class))
        .map(StockEntity::getId)
        .map(Math::abs)
        .ifPresentOrElse(stockId -> exchange.getIn().setHeader(KafkaConstants.KEY, stockId),
            () -> {
              throw new RuntimeException("Cannot set Kafka key");
            });
  }
}
