package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.entity.shop.CreditCardValue;
import com.watashi.bookstore.entity.shop.Sale;
import com.watashi.bookstore.entity.shop.SaleCreditCard;
import com.watashi.bookstore.entity.shop.SaleCreditCardId;
import com.watashi.bookstore.entity.user.CreditCard;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaleCreditCardHelper {

    private final IDomainService<CreditCard> creditCardDomainService;
    private final CreditCard mockCreditCard;

    private ObjectProvider<SaleCreditCard> saleCreditCardProvider;
    private ObjectProvider<SaleCreditCardId> saleCreditCardIdProvider;

    @Autowired
    public SaleCreditCardHelper(@Qualifier("domainService")
                                IDomainService<CreditCard> creditCardDomainService,
                                CreditCard mockCreditCard,
                                ObjectProvider<SaleCreditCard> saleCreditCardProvider,
                                ObjectProvider<SaleCreditCardId> saleCreditCardIdProvider) {
        this.creditCardDomainService = creditCardDomainService;
        this.mockCreditCard = mockCreditCard;
        this.saleCreditCardProvider = saleCreditCardProvider;
        this.saleCreditCardIdProvider = saleCreditCardIdProvider;
    }

    public SaleCreditCard adapt(CreditCardValue creditCardValue, Sale sale) {
        Long creditCardId = creditCardValue.getCreditCard().getId();
        Optional<CreditCard> creditCardOptional = creditCardDomainService.findById(creditCardId, mockCreditCard);
        CreditCard creditCard = creditCardOptional.orElseThrow(NotFoundException::new);
        creditCardValue.setCreditCard(creditCard);

        SaleCreditCard saleCreditCard = saleCreditCardProvider.getObject();
        SaleCreditCardId saleCreditCardId = saleCreditCardIdProvider.getObject();


        return saleCreditCard.adapt(saleCreditCardId, sale, creditCardValue);
    }
}
