package com.piotr.stock.streaming.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockEntity {

  public static final String ID = "ID";
  public static final String TICKER_COLUMN = "TICKER";
  public static final String STOCK_TYPE_COLUMN = "STOCKTYPE";
  public static final String EXCHANGE_COLUMN = "EXCHANGE";
  public static final String PRICE_COLUMN = "PRICE";
  public static final String CURRENCY_COLUMN = "CURRENCY";
  public static final String VOLUME_COLUMN = "VOLUME";
  public static final String STOCK_TIMESTAMP_COLUMN = "STOCKTIMESTAMP";

  private Long id;
  private String ticker;
  private String stockType;
  private String exchange;
  private BigDecimal price;
  private String currency;
  private Long volume;
  @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss")
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  @JsonDeserialize(using = LocalDateTimeDeserializer.class)
  private LocalDateTime stockTimestamp;
}
