DROP TABLE IF EXISTS stock CASCADE;

CREATE TABLE stock
(
    id identity constraint STOCK_PK primary key,
    ticker varchar(30) not null,
    stock_type varchar(50) not null,
    exchange varchar(50) not null,
    price numeric not null,
    currency varchar(20) not null,
    volume int not null,
    stock_timestamp datetime not null
);

CREATE INDEX stock_id_index ON stock(id);
CREATE INDEX stock_ticker_index ON stock(ticker);
CREATE INDEX stock_stock_type_index ON stock(stock_type);
CREATE INDEX stock_exchange_index ON stock(exchange);

INSERT INTO "PUBLIC"."STOCK"("ID", "TICKER", "STOCK_TYPE", "EXCHANGE", "PRICE", "CURRENCY", "VOLUME", "STOCK_TIMESTAMP") VALUES (1, 'USD/JPY', 'Physical Currency', 'NASDAQ', 114.8490, 'Japanese Yen', 768, TIMESTAMP '2022-02-17 22:12:32');
INSERT INTO "PUBLIC"."STOCK"("ID", "TICKER", "STOCK_TYPE", "EXCHANGE", "PRICE", "CURRENCY", "VOLUME", "STOCK_TIMESTAMP") VALUES (2, 'IXIC', 'Index', 'NASDAQ', 13532.4590, 'USD', 369, TIMESTAMP '2022-02-17 22:14:32');
INSERT INTO "PUBLIC"."STOCK"("ID", "TICKER", "STOCK_TYPE", "EXCHANGE", "PRICE", "CURRENCY", "VOLUME", "STOCK_TIMESTAMP") VALUES (3, 'AAPL', 'Common Stock', 'NASDAQ', 165.7820, 'USD', 1000, TIMESTAMP '2022-02-11 20:49:48');
INSERT INTO "PUBLIC"."STOCK"("ID", "TICKER", "STOCK_TYPE", "EXCHANGE", "PRICE", "CURRENCY", "VOLUME", "STOCK_TIMESTAMP") VALUES (4, 'EUR/USD', 'Physical Currency', 'WWA', 1.1092, 'US Dollar', 935, TIMESTAMP '2022-02-11 20:50:48');
INSERT INTO "PUBLIC"."STOCK"("ID", "TICKER", "STOCK_TYPE", "EXCHANGE", "PRICE", "CURRENCY", "VOLUME", "STOCK_TIMESTAMP") VALUES (5, 'AAPL', 'Common Stock', 'NASDAQ', 165.7600, 'USD', 981, TIMESTAMP '2022-02-11 20:51:48');
INSERT INTO "PUBLIC"."STOCK"("ID", "TICKER", "STOCK_TYPE", "EXCHANGE", "PRICE", "CURRENCY", "VOLUME", "STOCK_TIMESTAMP") VALUES (6, 'BTC/USD', 'Digital Currency', 'Binance', 43993.0000, 'US Dollar', 72110, TIMESTAMP '2022-02-11 20:52:48');
