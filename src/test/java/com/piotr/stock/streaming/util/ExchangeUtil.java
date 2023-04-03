package com.piotr.stock.streaming.util;

import lombok.experimental.UtilityClass;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;

@UtilityClass
public class ExchangeUtil {

  public static <T> Exchange createExchange(T body) {
    CamelContext camelContext = new DefaultCamelContext();
    Exchange exchange = new DefaultExchange(camelContext);
    exchange.getIn().setBody(body);
    return exchange;
  }
}
