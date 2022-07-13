package com.piotr.stock.streaming.repository;

import com.piotr.stock.streaming.entity.StockEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockProducer extends CrudRepository<StockEntity, Long> {
  @Override
  StockEntity save(StockEntity stock);
}
