package com.watashi.bookstore.service;

import com.watashi.bookstore.entity.sale.SaleProductCategory;
import com.watashi.bookstore.entity.shop.Sale;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface ISaleDashboardService extends IDashboardService<Sale> {

    Map<String, List<SaleProductCategory>> countSaleAndSumProductAmountByCategoriesBetweenInterval(
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
