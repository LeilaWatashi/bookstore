package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.config.UserDetailsImpl;
import com.watashi.bookstore.entity.user.Customer;
import com.watashi.bookstore.entity.user.User;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LoggedUserHelper {

    private final Customer mockCustomer;
    private final IDomainService<Customer> customerDomainService;

    @Autowired
    public LoggedUserHelper(Customer mockCustomer,
                             @Qualifier("domainService") IDomainService<Customer> customerDomainService) {
        this.mockCustomer = mockCustomer;
        this.customerDomainService = customerDomainService;
    }

    public static User getLoggedUser() {
        Authentication auth = getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

        return userDetails.getUser();
    }

    public Customer getLoggedCustomerUser() {
        User loggedUser = getLoggedUser();
        Optional<Customer> optionalCustomer = customerDomainService.findBy(loggedUser, mockCustomer);

        return optionalCustomer.orElseThrow(NotFoundException::new);
    }

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


}
