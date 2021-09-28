package com.watashi.bookstore.controller;

import com.watashi.bookstore.dto.StockManageDTO;
import com.watashi.bookstore.dto.StockNewDTO;
import com.watashi.bookstore.entity.product.Product;
import com.watashi.bookstore.entity.stock.Stock;
import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.exception.helper.*;
import com.watashi.bookstore.service.IAlternativeDomainService;
import com.watashi.bookstore.service.IDomainService;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = StockAdminController.BASE_STOCKS_ADMIN_URL)
public class StockAdminController {

    public static final String BASE_STOCKS_ADMIN_URL = "/admin/stocks";
    private static final String STOCK_ID_PATH_URL = "/{id}";
    private static final String NEW_STOCK_URL = "/new";
    private static final String MANAGE_STOCK_URL = "/{stockId}/manage";

    public static final String STOCK_MODEL_ATTRIBUTE = "stock";
    public static final String PRODUCTS_MODEL_ATTRIBUTE = "products";

    private final IAlternativeDomainService<Stock> stockDomainService;
    private final IDomainService<Product> productDomainService;

    private Stock stockMock;
    private Product productMock;

    private StockNewDTO newStockDTOmock;
    private StockManageDTO stockManageDTOmock;

    private StockHelper stockHelper;
    private StockHistoryHelper stockHistoryHelper;

    @Autowired
    public StockAdminController(@Qualifier("alternativeDomainService")
                                IAlternativeDomainService<Stock> stockDomainService,
                                @Qualifier("domainService")
                                IDomainService<Product> productDomainService) {
        this.stockDomainService = stockDomainService;
        this.productDomainService = productDomainService;
    }

    @Autowired
    private void setDependencyEntities(Stock stockMock,
                                       Product productMock) {
        this.stockMock = stockMock;
        this.productMock = productMock;
    }

    @Autowired
    private void setDependencyDTOs(StockNewDTO newStockDTOmock,
                                   StockManageDTO stockManageDTOmock) {
        this.newStockDTOmock = newStockDTOmock;
        this.stockManageDTOmock = stockManageDTOmock;
    }

    @Autowired
    private void setDependencyHelpers(StockHelper stockHelper,
                                      StockHistoryHelper stockHistoryHelper) {
        this.stockHelper = stockHelper;
        this.stockHistoryHelper = stockHistoryHelper;
    }

    @GetMapping
    public ModelAndView findAll() {
        List<Stock> foundStocks = stockDomainService.findAll(stockMock);

        return ModelAndViewHelper.configure(
                ViewType.STOCK_ADMIN,
                View.LIST,
                foundStocks,
                ModelAttributeType.STOCKS);
    }

    @GetMapping(path = STOCK_ID_PATH_URL)
    public ModelAndView findById(@PathVariable Long id) {
        Optional<Stock> stockOptional = stockDomainService.findById(id, stockMock);
        Stock foundStock = stockOptional.orElseThrow(NotFoundException::new);

        return ModelAndViewHelper.configure(
                ViewType.STOCK_ADMIN,
                View.NEW,
                foundStock,
                ModelAttributeType.STOCK);
    }

    @GetMapping(path = NEW_STOCK_URL)
    public ModelAndView newStock() {
        return ModelAndViewHelper.configure(
                ViewType.STOCK_ADMIN,
                View.NEW,
                newStockDTOmock,
                ModelAttributeType.STOCK);
    }

    @PostMapping
    public ModelAndView save(@Valid StockNewDTO newStockDTO,
                             Errors errors,
                             RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            ViewMessageHelper.configureRedirectMessageWith(errors,false, redirectAttributes);

            return ModelAndViewHelper.configure(ViewType.REDIRECT_NEW_STOCK_ADMIN);
        }

        Stock stock = ModelMapperHelper.fromDTOToEntity(newStockDTO, Stock.class);

        Product foundProduct = productDomainService.findById(stock.getProduct().getId(), productMock)
                .orElseThrow(NotFoundException::new);

        stock.setProduct(foundProduct);

        Stock createdStock = stockDomainService.save(stock);

        if (createdStock.getId() != null) {
            stockHistoryHelper.registerStockOperationOnHistory(stock);

            String message = "Estoque com a quantidade inicial de " + createdStock.getAmount()
                    + " unidades criado com sucesso para o produto \""
                    + createdStock.getProduct().getTitle()
                    + "\".";

            ViewMessageHelper.configureRedirectMessageWith(message, true, redirectAttributes);
        }

        return ModelAndViewHelper.configure(ViewType.REDIRECT_STOCKS_ADMIN);
    }

    @GetMapping(path = MANAGE_STOCK_URL)
    public ModelAndView manageStock(@PathVariable Long stockId) {
        Optional<Stock> stockOptional = stockDomainService.findById(stockId, stockMock);
        Stock foundStock = stockOptional.orElseThrow(NotFoundException::new);

        PropertyMap<Stock, StockManageDTO> skipDTOFieldsMap = new PropertyMap<Stock, StockManageDTO>() {
            @Override
            protected void configure() {
                skip().setOperationAmount(null);
            }
        };

        ModelMapperHelper.addDTOFieldsToSkip(skipDTOFieldsMap);

        StockManageDTO stockManageDTO = ModelMapperHelper
                .fromEntityToDTO(foundStock, StockManageDTO.class);

        return ModelAndViewHelper.configure(
                ViewType.STOCK_ADMIN,
                View.MANAGE,
                stockManageDTO,
                ModelAttributeType.STOCK_MANAGE);
    }

    @PostMapping(path = MANAGE_STOCK_URL)
    public ModelAndView handlerManageStock(@PathVariable Long stockId,
                                           @Valid StockManageDTO stockManageDTOmock,
                                           Errors errors,
                                           RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            ViewMessageHelper.configureRedirectMessageWith(errors,false, redirectAttributes);

            return ModelAndViewHelper.configure(ViewType.REDIRECT_MANAGE_STOCK_ADMIN, stockId);
        }

        boolean isEntering =  stockManageDTOmock.getOperationAmount() > 0;

        String operation = isEntering ? "ENTRADA" : "SAÍDA";

        StockHistory registeredStockHistory = isEntering
                ? stockHelper.up(stockManageDTOmock)
                : stockHelper.down(stockManageDTOmock);

        String message = "Operação de " + operation
                + " no estoque com a quantidade de " + registeredStockHistory.getAmount()
                + " unidades do produto \""
                + registeredStockHistory.getStock().getProduct().getTitle()
                + "\" registrada.";

        ViewMessageHelper.configureRedirectMessageWith(
                message,
                true,
                redirectAttributes
        );

        return ModelAndViewHelper.configure(ViewType.REDIRECT_STOCKS_ADMIN);
    }

    @ModelAttribute(PRODUCTS_MODEL_ATTRIBUTE)
    public List<Product> products() {
        return productDomainService.findAllActivatedBy(productMock)
                .stream()
                .filter(Product::hasNotStock)
                .collect(Collectors.toList());
    }
}