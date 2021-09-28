package com.watashi.bookstore.controller;

import com.watashi.bookstore.entity.shop.Sale;
import com.watashi.bookstore.entity.shop.SalesStatusType;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = SaleAdminController.BASE_SALE_ADMIN_URL)
public class SaleAdminController {

    public static final String BASE_SALE_ADMIN_URL = "/admin/sales";
    public static final String UPDATE_STATUS_URL = "/{id}/update-status";

    private final IDomainService<Sale> saleDomainService;
    private final Sale mockSale;

    @Autowired
    public SaleAdminController(@Qualifier("domainService")
                               IDomainService<Sale> saleDomainService,
                               Sale mockSale) {
        this.saleDomainService = saleDomainService;
        this.mockSale = mockSale;
    }

    @GetMapping
    public ModelAndView findAll() {
        List<Sale> sales = saleDomainService.findAll(mockSale);

        return ModelAndViewHelper.configure(
                ViewType.SALE_ADMIN,
                View.LIST,
                sales,
                ModelAttributeType.SALES);
    }

    @PostMapping(path = UPDATE_STATUS_URL)
    public ModelAndView updateStatus(@PathVariable Long id,
                                     SalesStatusType newStatus,
                                     RedirectAttributes redirectAttributes) {
        Optional<Sale> foundSaleOptional = saleDomainService.findById(id, mockSale);
        Sale sale = foundSaleOptional.orElseThrow(NotFoundException::new);
        SalesStatusType oldStatus = sale.getStatus();

        sale.setStatus(newStatus);
        Sale updatedSale = saleDomainService.save(sale);

        boolean isSuccess = updatedSale.getStatus() == newStatus;

        String successMessage = "O status da venda foi alterado com sucesso de \""
                + oldStatus.getDisplayName() + "\" para \"" + newStatus.getDisplayName() + "\".";
        String errorMessage = "Ocorreu um erro ao tentar alterar o status da venda \""
                + oldStatus.getDisplayName() + "\" para \"" + newStatus.getDisplayName() + "\".";

        String message = isSuccess ? successMessage : errorMessage;
        redirectAttributes.addFlashAttribute(ModelAttributeType.MESSAGE.getName(), message);
        redirectAttributes.addFlashAttribute(ModelAttributeType.IS_SUCCESS_MESSAGE.getName(), isSuccess);

        return ModelAndViewHelper.configure(ViewType.REDIRECT_SALES_ADMIN);
    }

}
