package com.watashi.bookstore.entity.shop;

import com.watashi.bookstore.entity.Entity;
import com.watashi.bookstore.entity.user.Address;
import com.watashi.bookstore.entity.user.CreditCard;
import com.watashi.bookstore.entity.user.Customer;
import javassist.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Scope( scopeName = WebApplicationContext.SCOPE_SESSION,
        proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class SaleInProgress extends Entity {

    private String identifyNumber;
    private SalesStatusType status;
    private Customer customer;
    private List<Address> adresses = new ArrayList<>();
    private List<CartItem> cartItems = new ArrayList<>();
    private List<CreditCardValue> usedCreditCards = new ArrayList<>();
    private Voucher voucher;
    private Freight freight;
    private double subtotal;
    private double total;
    private Boolean voucherAlreadyApplied = false;

    public void addAddress(Address address) {
        adresses.add(address);
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
        addToTotal(subtotal);
    }

    public void addToTotal(Double value) {
        total += value;
    }

    public void calculateFreight() throws NotFoundException {
        String zipCode = adresses.stream()
                .filter(address -> address.isShippingAndBilling() || address.isBilling())
                .findFirst()
                .orElseThrow(()-> new NotFoundException("Cep não encontrado"))
                .getCep();
        freight.calculate(subtotal, zipCode);
        addToTotal(freight.getValue());
    }

    public void addCreditCardValue(CreditCardValue creditCardValue) {
        usedCreditCards.add(creditCardValue);
    }

    private void generateIdentifyNumber() {
        String millis = String.valueOf(System.currentTimeMillis());
        StringBuilder invertedMillis = new StringBuilder();

        String uniqueCode = (customer != null && customer.getId() != null)
                ? customer.getId().toString()
                : String.valueOf(this.hashCode());

        invertedMillis.append(uniqueCode);

        for (int i = millis.length() - 1; i >= 0; i--) {
            invertedMillis.append(millis.charAt(i));
        }

        identifyNumber = invertedMillis.toString();
    }

    public void putStatusToProcessing() {
        this.status = SalesStatusType.PROCESSING;
    }

    public void finish() {
        generateIdentifyNumber();
        putStatusToProcessing();
    }

    public void removeCreditCardUsedById(Long creditCardId) {
        usedCreditCards.removeIf(saleCreditCard -> saleCreditCard.getCreditCard().getId().equals(creditCardId));
    }

    public Boolean creditCreditCardAlreadyAdded(CreditCard creditCard) {
        return usedCreditCards.stream()
                .anyMatch(saleCreditCard -> saleCreditCard
                        .getCreditCard()
                        .equals(creditCard));
    }

    public Double calculateRemainingAmount() {
        Double coverageAmountByPaymentMethod = usedCreditCards.stream()
                .map(CreditCardValue::getValue)
                .reduce(0.0, Double::sum);
        
        return total - coverageAmountByPaymentMethod;
    }

    public Boolean isRemainingAmountFullyCovered() {
        return calculateRemainingAmount() == .0;
    }

    public Double calculateTotalWithoutVoucher() {
        return isDiscountVoucher()
                ? total / (1 - voucher.getMultiplicationFactor())
                : total - voucher.getValue();
    }

    public Double calculateTotalWithFreightBeforeVoucherApplied() {
        return (isDiscountVoucher()
                ? total / (1 - voucher.getMultiplicationFactor())
                : total + voucher.getValue()
        ) + freight.getValue();
    }

    public Boolean isDiscountVoucher() {
        return voucher.getType().equals(VoucherType.DISCOUNT);
    }

    public void applyVoucher(Voucher voucher) {
        if (!voucherAlreadyApplied) {
            this.voucher = voucher;
            double x = (isDiscountVoucher() ? voucher.getMultiplicationFactor() : voucher.getValue());
            addToTotal((isDiscountVoucher() ? total * x : Math.min(x, total)) * -1);

            voucherAlreadyApplied = true;
        }
    }
}
