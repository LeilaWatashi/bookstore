package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.product.Product;
import com.watashi.bookstore.entity.shop.CartItem;
import com.watashi.bookstore.entity.shop.Sale;
import com.watashi.bookstore.entity.shop.SaleItem;
import com.watashi.bookstore.entity.shop.SaleItemId;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaleItemHelper {

    private final IDomainService<Product> productDomainService;
    private final Product mockProduct;

    private ObjectProvider<SaleItem> saleItemProvider;
    private ObjectProvider<SaleItemId> saleItemIdProvider;

    @Autowired
    public SaleItemHelper(@Qualifier("domainService") IDomainService<Product> productDomainService,
                          Product mockProduct,
                          ObjectProvider<SaleItem> saleItemProvider,
                          ObjectProvider<SaleItemId> saleItemIdProvider) {
        this.productDomainService = productDomainService;
        this.mockProduct = mockProduct;
        this.saleItemProvider = saleItemProvider;
        this.saleItemIdProvider = saleItemIdProvider;
    }

    public SaleItem adapt(CartItem cartItem, Sale sale) {
        Long productId = cartItem.getProduct().getId();
        Optional<Product> productOptional = productDomainService.findById(productId, mockProduct);
        Product product = productOptional.orElseThrow(NotFoundException::new);
        Integer amount = cartItem.getAmount();
        Double subtotal = cartItem.getSubtotal();

        SaleItem saleItem = saleItemProvider.getObject();
        SaleItemId saleItemId = saleItemIdProvider.getObject();
        saleItemId.setItemId(productId);

        saleItem.setId(saleItemId);
        saleItem.setSale(sale);
        saleItem.setProduct(product);
        saleItem.setQuantity(amount);
        saleItem.setSubtotal(subtotal);

        return saleItem;
    }
}
