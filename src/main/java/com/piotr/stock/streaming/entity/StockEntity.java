package com.piotr.stock.streaming.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(StockEntity.TABLE_NAME)
public class StockEntity {

  public static final String TABLE_NAME = "STOCK";
  public static final String TICKER_COLUMN = "TICKER";
  public static final String STOCK_TYPE_COLUMN = "STOCK_TYPE";
  public static final String EXCHANGE_COLUMN = "EXCHANGE";
  public static final String PRICE_COLUMN = "PRICE";
  public static final String CURRENCY_COLUMN = "CURRENCY";
  public static final String VOLUME_COLUMN = "VOLUME";
  public static final String STOCK_TIMESTAMP_COLUMN = "STOCK_TIMESTAMP";

  @Id
  private Long id;

  @Column(StockEntity.TICKER_COLUMN)
  private String ticker;

  @Column(StockEntity.STOCK_TYPE_COLUMN)
  private String stockType;

  @Column(StockEntity.EXCHANGE_COLUMN)
  private String exchange;

  @Column(StockEntity.PRICE_COLUMN)
  private BigDecimal price;

  @Column(StockEntity.CURRENCY_COLUMN)
  private String currency;

  @Column(StockEntity.VOLUME_COLUMN)
  private BigInteger volume;

  @Column(StockEntity.STOCK_TIMESTAMP_COLUMN)
  private LocalDateTime stockTimestamp;
}
