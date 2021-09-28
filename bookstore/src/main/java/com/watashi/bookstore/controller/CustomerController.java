package com.watashi.bookstore.controller;

import com.watashi.bookstore.dto.CustomerUpdateDTO;
import com.watashi.bookstore.dto.UserUpdateDTO;
import com.watashi.bookstore.entity.shop.Sale;
import com.watashi.bookstore.entity.shop.ShopCart;
import com.watashi.bookstore.entity.shop.Trade;
import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.User;
import com.watashi.bookstore.entity.user.UserType;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.exception.helper.LoggedUserHelper;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import com.watashi.bookstore.exception.helper.ModelMapperHelper;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class CustomerController {

    public static final String ACCOUNT_URI = "/account";
    public static final String ORDERS_URI = "/orders";
    public static final String TRADES_URI = "/trades";
    public static final String ADRESSES_URI = "/adresses";
    public static final String CREDIT_CARDS_URI = "/credit-cards";
    public static final String CHANGE_PASSWORD_URI = "/change-password";

    private final IDomainService<Customer> customerDomainService;
    private final IDomainService<Sale> saleDomainService;
    private final IDomainService<Trade> tradeDomainService;

    private final ShopCart shopCart;
    private final Customer mockCustomer;
    private final Sale mockSale;
    private final Trade mockTrade;
    private final CustomerUpdateDTO mockCustomerUpdateDTO;

    private final ModelAndViewHelper modelAndViewHelper;

    private final LoggedUserHelper loggedUserHelper;

    @Autowired
    public CustomerController(@Qualifier("domainService")
                              IDomainService<Customer> customerDomainService,
                              @Qualifier("domainService")
                              IDomainService<Sale> saleDomainService,
                              @Qualifier("domainService")
                              IDomainService<Trade> tradeDomainService,
                              ShopCart shopCart,
                              Customer mockCustomer,
                              Sale mockSale, Trade mockTrade, CustomerUpdateDTO mockCustomerUpdateDTO,
                              ModelAndViewHelper modelAndViewHelper,
                              LoggedUserHelper loggedUserHelper) {
        this.customerDomainService = customerDomainService;
        this.saleDomainService = saleDomainService;
        this.tradeDomainService = tradeDomainService;
        this.shopCart = shopCart;
        this.mockCustomer = mockCustomer;
        this.mockSale = mockSale;
        this.mockTrade = mockTrade;
        this.mockCustomerUpdateDTO = mockCustomerUpdateDTO;
        this.modelAndViewHelper = modelAndViewHelper;
        this.loggedUserHelper = loggedUserHelper;
    }

    @GetMapping(path = ACCOUNT_URI)
    public ModelAndView showAccount() {
        ModelAndView modelAndView = ModelAndViewHelper
                .configure(ViewType.CUSTOMER_ACCOUNT_SHOP, View.DETAILS);

        Customer customer = loggedUserHelper.getLoggedCustomerUser();
        CustomerUpdateDTO customerUpdateDTO = ModelMapperHelper.fromEntityToDTO(customer, CustomerUpdateDTO.class);
        ModelAndViewHelper.addObjectTo(modelAndView, customerUpdateDTO, ModelAttributeType.CUSTOMER);

        return modelAndView;
    }

    @PostMapping(path = ACCOUNT_URI)
    public ModelAndView saveAccount(@Valid CustomerUpdateDTO customerUpdateDTO,
                                    Errors errors,
                                    RedirectAttributes redirectAttributes) {
        if (errors.hasErrors()) {
            return ModelAndViewHelper.configure(ViewType.CUSTOMER_ACCOUNT_SHOP, View.DETAILS);
        }
        try {
            User loggedUserUpdated = LoggedUserHelper.getLoggedUser();
            UserUpdateDTO loggedUserDTOUpdated = ModelMapperHelper.fromEntityToDTO(loggedUserUpdated, UserUpdateDTO.class);
            UserUpdateDTO updatedDTOUser = customerUpdateDTO.getUser();

            if (updatedDTOUser.getPassword() != null && !updatedDTOUser.getPassword().isEmpty()) {
                loggedUserDTOUpdated.setUsername(updatedDTOUser.getUsername());
                loggedUserDTOUpdated.setPassword(updatedDTOUser.getPassword());
            }

            // TODO: PLEASE, find error "Failed to convert value of type 'br.com.utily.ecommerce.dto.domain.user.customer.CustomerUpdateDTO' to required type 'java.lang.String'"

            customerUpdateDTO.setUser(loggedUserDTOUpdated);
            Customer customer = ModelMapperHelper.fromDTOToEntity(customerUpdateDTO, Customer.class);
            User user = customer.getUser();
            user.setUserType(UserType.CUSTOMER);
            customer.setUser(user);
            Customer savedCustomer = customerDomainService.save(customer);

            CustomerUpdateDTO savedCustomerUpdateDTO = ModelMapperHelper.fromEntityToDTO(savedCustomer, CustomerUpdateDTO.class);

            ModelAndView modelAndView = ModelAndViewHelper.configure(ViewType.REDIRECT_CUSTOMER_ACCOUNT_SHOP);
            ModelAndViewHelper.addObjectTo(modelAndView, savedCustomerUpdateDTO, ModelAttributeType.CUSTOMER);
            redirectAttributes.addFlashAttribute("message", "Seus dados foram atualizados.");

            return modelAndView;

        } catch (Exception exception) {

            System.out.println(exception.getMessage());
            exception.printStackTrace();

            return ModelAndViewHelper.configure(ViewType.CUSTOMER_ACCOUNT_SHOP, View.DETAILS);
        }
    }

    @GetMapping(path = ORDERS_URI)
    public ModelAndView showOrders() {
        ModelAndView modelAndView = ModelAndViewHelper
                .configure(ViewType.CUSTOMER_ORDER_SHOP, View.LIST);

        Customer loggedCustomerUser = loggedUserHelper.getLoggedCustomerUser();

        List<Sale> customerOrders = saleDomainService.findAllBy(loggedCustomerUser, mockSale);

        Boolean thereAreOrders = !customerOrders.isEmpty();

        ModelAndViewHelper.addObjectTo(modelAndView, customerOrders, ModelAttributeType.ORDERS);
        ModelAndViewHelper.addObjectTo(modelAndView, thereAreOrders, ModelAttributeType.THERE_ARE_ORDERS);

        return modelAndView;
    }

    @GetMapping(path = ORDERS_URI + "/{id}")
    public ModelAndView viewOrderDetails(@PathVariable Long id) {
        ModelAndView modelAndView = ModelAndViewHelper
                .configure(ViewType.CUSTOMER_ORDER_SHOP, View.DETAILS);

        Optional<Sale> orderOptional = saleDomainService.findById(id, mockSale);

        Sale foundOrder = orderOptional.orElseThrow(NotFoundException::new);

        ModelAndViewHelper.addObjectTo(modelAndView, foundOrder, ModelAttributeType.ORDER);

        return modelAndView;
    }

    @GetMapping(path = TRADES_URI)
    public ModelAndView showTrades() {
        ModelAndView modelAndView = ModelAndViewHelper
                .configure(ViewType.CUSTOMER_TRADE_SHOP, View.LIST);

        Customer loggedCustomerUser = loggedUserHelper.getLoggedCustomerUser();

        List<Trade> customerTrades = tradeDomainService.findAllBy(loggedCustomerUser, mockTrade);

        Boolean thereAreTrades = !customerTrades.isEmpty();

        ModelAndViewHelper.addObjectTo(modelAndView, customerTrades, ModelAttributeType.TRADES);
        ModelAndViewHelper.addObjectTo(modelAndView, thereAreTrades, ModelAttributeType.THERE_ARE_TRADES);

        return modelAndView;
    }

    @GetMapping(path = TRADES_URI + "/{id}")
    public ModelAndView viewTradeDetails(@PathVariable Long id) {
        ModelAndView modelAndView = ModelAndViewHelper
                .configure(ViewType.CUSTOMER_TRADE_SHOP, View.DETAILS);

        Optional<Trade> tradeOptional = tradeDomainService.findById(id, mockTrade);

        Trade foundTrade = tradeOptional.orElseThrow(NotFoundException::new);

        ModelAndViewHelper.addObjectTo(modelAndView, foundTrade, ModelAttributeType.TRADE);

        return modelAndView;
    }

    @ModelAttribute("shopCart")
    public ShopCart shopCart() {
        return shopCart;
    }
}
