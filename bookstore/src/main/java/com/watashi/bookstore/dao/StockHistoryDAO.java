package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.stock.StockHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface StockHistoryDAO extends AlternativeDomainDAO<StockHistory>, DateFilter<StockHistory>{
}
