package com.watashi.bookstore.controller;

import com.watashi.bookstore.dto.CustomerSignUpDTO;
import com.watashi.bookstore.dto.DTO;
import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.User;
import com.watashi.bookstore.entity.user.UserType;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(path = AuthController.AUTH_URL)
public class                                                                                                                                                                                AuthController {

    public static final String AUTH_URL = "/auth";
    public static final String LOGIN_URL = "/login";
    public static final String SIGN_UP_URL = "/sign-up";

    private final IDomainService<Customer> customerDomainService;

    private final ModelAndViewHelper modelAndViewHelper;
    private final Customer mockCustomer;
    private final CustomerSignUpDTO mockCustomerSignUpDTO;

    @Autowired
    public AuthController(@Qualifier("domainService") IDomainService<Customer> customerDomainService,
                          Customer mockCustomer,
                          CustomerSignUpDTO mockCustomerSignUpDTO,
                          ModelAndViewHelper modelAndViewHelper) {
        this.customerDomainService = customerDomainService;
        this.mockCustomer = mockCustomer;
        this.mockCustomerSignUpDTO = mockCustomerSignUpDTO;
        this.modelAndViewHelper = modelAndViewHelper;
    }

    @GetMapping(path = LOGIN_URL)
    public ModelAndView showLogin() {
        return ModelAndViewHelper.configure(ViewType.AUTH_APPLICATION, View.LOGIN);
    }

    @GetMapping(path = SIGN_UP_URL)
    public ModelAndView showSignUp() {
        return ModelAndViewHelper.configure(
                ViewType.AUTH_APPLICATION,
                View.SIGN_UP,
                mockCustomerSignUpDTO,
                ModelAttributeType.CUSTOMER);
    }

    @PostMapping(path = SIGN_UP_URL)
    public ModelAndView doSignUp(@Validated
                                 @DTO(CustomerSignUpDTO.class) Customer customer,
                                 Errors errors,
                                 RedirectAttributes redirectAttributes) {

        if (errors.hasErrors()) {
            return ModelAndViewHelper.configure(ViewType.AUTH_APPLICATION, View.SIGN_UP);
        }
        try {
            ModelAndView modelAndView = ModelAndViewHelper.configure(ViewType.REDIRECT_LOGIN_APPLICATION);

            User user = customer.getUser();
            user.setUserType(UserType.CUSTOMER);
            customer.setUser(user);
            customerDomainService.save(customer);

            redirectAttributes.addFlashAttribute("message", "Conta criada com sucesso! Agora você pode fazer login.");

            return modelAndView;
        } catch (Exception exception) {
            return ModelAndViewHelper.configure(ViewType.AUTH_APPLICATION, View.SIGN_UP);
        }
    }


}
