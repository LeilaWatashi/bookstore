package com.watashi.bookstore.controller;

import com.watashi.bookstore.entity.product.Product;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(path = ProductAdminController.BASE_PRODUCT_ADMIN_URL)
public class ProductAdminController {

    public static final String BASE_PRODUCT_ADMIN_URL = "/admin/products";

    private final IDomainService<Product> productDomainService;

    private final Product productMock;

    @Autowired
    public ProductAdminController(@Qualifier("domainService")
                                  IDomainService<Product> productDomainService,
                                  Product productMock) {
        this.productDomainService = productDomainService;
        this.productMock = productMock;
    }

    @GetMapping
    public ModelAndView findAll() {
        return ModelAndViewHelper.configure(
                ViewType.PRODUCT_ADMIN,
                View.LIST);
    }

    @ModelAttribute("products")
    public List<Product> products() {
        return productDomainService.findAll(productMock);
    }
}
