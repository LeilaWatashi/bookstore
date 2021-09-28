package com.watashi.bookstore.config;

import com.watashi.bookstore.entity.shop.*;
import com.watashi.bookstore.entity.stock.Stock;
import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.entity.user.UserVoucher;
import com.watashi.bookstore.entity.user.UserVoucherId;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PrototypeBeansConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Sale createPrototypeForSale() {
        return new Sale();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SaleItem createPrototypeForSaleItem() {
        return new SaleItem();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SaleItemId createPrototypeForSaleItemId() {
        return new SaleItemId();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SaleAddress createPrototypeForSaleAddress() {
        return new SaleAddress();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SaleAddressId createPrototypeForSaleAddressId() {
        return new SaleAddressId();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SaleCreditCard createPrototypeForSaleCreditCard() {
        return new SaleCreditCard();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public SaleCreditCardId createPrototypeForSaleCreditCardId() {
        return new SaleCreditCardId();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Freight createPrototypeForFreight() {
        return new Freight();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Voucher createPrototypeForVoucher() {
        return new Voucher();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UserVoucher createPrototypeForCustomerVoucher() {
        return new UserVoucher();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public UserVoucherId createPrototypeForCustomerVoucherId() {
        return new UserVoucherId();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Stock createPrototypeForStock() {
        return new Stock();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public StockHistory createPrototypeForStockHistory() {
        return new StockHistory();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public Trade createPrototypeForTrade() {
        return new Trade();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public TradeItem createPrototypeForTradeItem() {
        return new TradeItem();
    }

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public TradeItemId createPrototypeForTradeItemId() {
        return new TradeItemId();
    }

}
