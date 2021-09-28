package com.watashi.bookstore.dao;

import com.watashi.bookstore.entity.stock.Stock;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDAO extends AlternativeDomainDAO<Stock>{
}
