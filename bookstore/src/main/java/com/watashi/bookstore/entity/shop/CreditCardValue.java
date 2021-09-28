package com.watashi.bookstore.entity.shop;


import com.watashi.bookstore.entity.Entity;
import com.watashi.bookstore.entity.user.CreditCard;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter

@Component
public class CreditCardValue extends Entity {

    private CreditCard creditCard;
    private Double value;

}
