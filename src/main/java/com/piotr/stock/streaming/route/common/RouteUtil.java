package com.piotr.stock.streaming.route.common;

import java.util.function.Function;
import lombok.experimental.UtilityClass;
import org.apache.camel.Processor;

@UtilityClass
public class RouteUtil {

  public static <T, U> Processor wrapProcessing(Function<U, T> processFunction) {
    return exchange -> {
      U exchangeBody = (U) exchange.getIn().getBody();
      T result = processFunction.apply(exchangeBody);
      exchange.getIn().setBody(result);
    };
  }
}
