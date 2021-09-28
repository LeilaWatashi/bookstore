package com.watashi.bookstore.controller;

import com.watashi.bookstore.dto.CreditCardIdAndValueDTO;
import com.watashi.bookstore.dto.VoucherIdDTO;
import com.watashi.bookstore.entity.shop.*;
import com.watashi.bookstore.entity.user.*;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.exception.helper.*;
import com.watashi.bookstore.service.IAssociativeDomainService;
import com.watashi.bookstore.service.IDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = CheckoutShopController.BASE_CHECKOUT_URL)
public class CheckoutShopController {

    public static final String BASE_CHECKOUT_URL = "/shop/checkout";
    public static final String CHECKOUT_STEP_ONE_URL = "/step/one";
    public static final String CHECKOUT_STEP_TWO_URL = "/step/two";
    public static final String CHECKOUT_STEP_THREE_URL = "/step/three";
    public static final String CHECKOUT_STEP_ONE_VALIDATION_URL = "/step/one/validation";
    public static final String CHECKOUT_STEP_TWO_PAYMENT_CC_URL = "/step/two/payment/credit-cards";
    public static final String CHECKOUT_STEP_TWO_PAYMENT_VOUCHER_URL = "/step/two/payment/voucher";
    public static final String CHECKOUT_FINISH_URL = "/finish";

    private final IDomainService<Sale> saleDomainService;
    private final IDomainService<Address> addressDomainService;
    private final IDomainService<CreditCard> creditCardDomainService;
    private final IDomainService<Voucher> voucherDomainService;
    private final IAssociativeDomainService<UserVoucher> customerVoucherAlternativeDomainService;

    private CreditCard mockCreditCard;
    private UserVoucher mockCustomerVoucher;
    private Voucher mockVoucher;

    private SaleInProgress saleInProgress;
    private ShopCart shopCart;
    private Customer loggedCustomer;

    private CreditCardIdAndValueDTO mockCreditCardAndValueDTO;
    private VoucherIdDTO mockVoucherIdDTO;

    private LoggedUserHelper loggedUserHelper;
    private CheckoutHelper checkoutHelper;
    private CustomerVoucherHelper customerVoucherHelper;
    private StockHelper stockHelper;
    private FreightHelper freightHelper;

    private final UUID hash;

    @Autowired
    public CheckoutShopController(@Qualifier("domainService") final IDomainService<Sale> saleDomainService,
                                  @Qualifier("domainService") final IDomainService<Address> addressDomainService,
                                  @Qualifier("domainService") final IDomainService<CreditCard> creditCardDomainService,
                                  @Qualifier("domainService") final IDomainService<Voucher> voucherDomainService,
                                  @Qualifier("associativeDomainService") final IAssociativeDomainService<UserVoucher> customerVoucherAlternativeDomainService) {
        this.saleDomainService = saleDomainService;
        this.addressDomainService = addressDomainService;
        this.creditCardDomainService = creditCardDomainService;
        this.voucherDomainService = voucherDomainService;
        this.customerVoucherAlternativeDomainService = customerVoucherAlternativeDomainService;

        hash = UUID.randomUUID();
    }

    @Autowired
    private void setDependencyEntities(final ShopCart shopCart,
                                       final SaleInProgress saleInProgress,
                                       final CreditCard mockCreditCard,
                                       final UserVoucher mockCustomerVoucher,
                                       final Voucher mockVoucher) {
        this.shopCart = shopCart;
        this.saleInProgress = saleInProgress;
        this.mockCreditCard = mockCreditCard;
        this.mockCustomerVoucher = mockCustomerVoucher;
        this.mockVoucher = mockVoucher;
    }

    @Autowired
    private void setDependencyDTOs(CreditCardIdAndValueDTO mockCreditCardAndValueDTO,
                                   VoucherIdDTO mockVoucherIdDTO) {
        this.mockCreditCardAndValueDTO = mockCreditCardAndValueDTO;
        this.mockVoucherIdDTO = mockVoucherIdDTO;
    }

    @Autowired
    private void setDependencyHelpers(final LoggedUserHelper loggedUserHelper,
                                      final CheckoutHelper checkoutHelper,
                                      final CustomerVoucherHelper customerVoucherHelper,
                                      final StockHelper stockHelper,
                                      final FreightHelper freightHelper) {
        this.loggedUserHelper = loggedUserHelper;
        this.checkoutHelper = checkoutHelper;
        this.customerVoucherHelper = customerVoucherHelper;
        this.stockHelper = stockHelper;
        this.freightHelper = freightHelper;
    }

    @GetMapping(path = CHECKOUT_STEP_ONE_URL)
    public ModelAndView initializeStepOne(HttpSession httpSession) {
        SessionHelper.initialize(httpSession);

        loggedCustomer = loggedUserHelper.getLoggedCustomerUser();
        saleInProgress.setCustomer(loggedCustomer);

        Optional<ShopCart> shopCartOptional = ProxyHelper.recoveryEntityFromProxy(shopCart);
        ShopCart shopCartFromProxy = shopCartOptional.orElseThrow(NotFoundException::new);
        saleInProgress.setCartItems(shopCartFromProxy.getItems());
        saleInProgress.setSubtotal(shopCart.getTotal());

        ModelAndView modelAndView = ModelAndViewHelper.configure(
                ViewType.CHECKOUT_STEP_SHOP,
                View.CHECKOUT_STEP_ONE,
                loggedCustomer,
                ModelAttributeType.CUSTOMER);

        modelAndView.addObject(
                ModelAttributeType.NOT_ENOUGH_ADDRESS.getName(),
                false);
        modelAndView.addObject(
                ModelAttributeType.ENABLE_NEXT_STEP.getName(),
                false
        );

        return modelAndView;
    }

    @PostMapping(path = CHECKOUT_STEP_ONE_VALIDATION_URL)
    public ModelAndView handlerStepOne(Address address) throws javassist.NotFoundException {
        loggedCustomer = loggedUserHelper.getLoggedCustomerUser();

        ModelAndView modelAndView = ModelAndViewHelper.configure(
                ViewType.CHECKOUT_STEP_SHOP,
                View.CHECKOUT_STEP_ONE,
                loggedCustomer,
                ModelAttributeType.CUSTOMER);

        Long addressId = address.getId();
        boolean isExistingAddress = addressId != null;

        if (isExistingAddress) {
            Optional<Address> foundAddressOptional = addressDomainService.findById(addressId, address);
            Address addressToAdd = foundAddressOptional.orElseThrow(NotFoundException::new);

            if (!addressToAdd.isShippingAndBilling()) {
                AddressType missingAddressType = address.isShipping()
                        ? AddressType.BILLING
                        : AddressType.SHIPPING;

                List<Address> adressesWithMissingType = checkoutHelper
                        .filterAdressesByType(
                                loggedCustomer.getAdresses(),
                                missingAddressType
                        );

                Map<String, Object> mvObjects = new HashMap<>();
                mvObjects.put(ModelAttributeType.NOT_ENOUGH_ADDRESS.getName(), true);
                mvObjects.put(ModelAttributeType.ADDRESS_TYPE.getName(), missingAddressType);
                mvObjects.put(ModelAttributeType.ADRESSES.getName(), adressesWithMissingType);

                modelAndView.addAllObjects(mvObjects);

                return modelAndView;
            }

            saleInProgress.addAddress(addressToAdd);

            Freight freight = freightHelper.build();
            saleInProgress.setFreight(freight);
            saleInProgress.calculateFreight();

            modelAndView = ModelAndViewHelper.configure(ViewType.REDIRECT_CHECKOUT_STEP_TWO);
        }

        return modelAndView;
    }

    @GetMapping(path = CHECKOUT_STEP_TWO_URL)
    public ModelAndView loadStepTwo() {
        ModelAndView modelAndView = ModelAndViewHelper.configure(
                ViewType.CHECKOUT_STEP_SHOP,
                View.CHECKOUT_STEP_TWO,
                saleInProgress,
                ModelAttributeType.SALE);

        loggedCustomer = loggedUserHelper.getLoggedCustomerUser();
        saleInProgress.setCustomer(loggedCustomer);

        List<CreditCard> customerCreditCards = creditCardDomainService.findAllBy(loggedCustomer, mockCreditCard);
        List<Voucher> customerVouchers = customerVoucherAlternativeDomainService
                .findAllValidBy(loggedCustomer, mockCustomerVoucher)
                .stream()
                .map(UserVoucher::getVoucher)
                .collect(Collectors.toList());

        Boolean enableNextStep = saleInProgress.isRemainingAmountFullyCovered();

        mockCreditCardAndValueDTO.setValue(saleInProgress.calculateRemainingAmount());

        Map<String, Object> modelMap = new HashMap<>();
        modelMap.put(ModelAttributeType.CREDIT_CARDS.getName(), customerCreditCards);
        modelMap.put(ModelAttributeType.VOUCHERS.getName(), customerVouchers);
        modelMap.put(ModelAttributeType.CREDIT_CARD_AND_VALUE.getName(), mockCreditCardAndValueDTO);
        modelMap.put(ModelAttributeType.VOUCHER.getName(), mockVoucherIdDTO);
        modelMap.put(ModelAttributeType.ENABLE_NEXT_STEP.getName(), enableNextStep);
        modelMap.put(ModelAttributeType.CREDIT_CARDS.getName(), customerCreditCards);
        ModelAndViewHelper.addModelMapTo(modelAndView, modelMap);

        return modelAndView;
    }

    @PostMapping(path = CHECKOUT_STEP_TWO_PAYMENT_CC_URL)
    public ModelAndView addCreditCardValueForPaymentStepTwo(@Valid CreditCardIdAndValueDTO creditCardIdAndValueDTO,
                                                            Errors errors,
                                                            RedirectAttributes redirectAttributes) {
        ModelAndView modelAndView = ModelAndViewHelper.configure(ViewType.REDIRECT_CHECKOUT_STEP_TWO);

        if (errors.hasFieldErrors("value")) {
            FieldError valueFieldError = errors.getFieldError("value");
            String message = valueFieldError != null ? valueFieldError.getDefaultMessage() : "Valor inválido";

            redirectAttributes.addFlashAttribute(ModelAttributeType.IS_SUCCESS_MESSAGE.getName(), false);
            redirectAttributes.addFlashAttribute(ModelAttributeType.MESSAGE.getName(), message);

            return modelAndView;
        }

        CreditCardValue creditCardValue = ModelMapperHelper.fromDTOToEntity(creditCardIdAndValueDTO, CreditCardValue.class);

        Optional<CreditCard> creditCardOptional = creditCardDomainService
                .findById(
                        creditCardValue.getCreditCard().getId(),
                        mockCreditCard
                );

        creditCardValue.setCreditCard(
                creditCardOptional.orElseThrow(NotFoundException::new)
        );

        saleInProgress.addCreditCardValue(creditCardValue);

        redirectAttributes.addFlashAttribute(ModelAttributeType.IS_SUCCESS_MESSAGE.getName(), true);
        redirectAttributes.addFlashAttribute(ModelAttributeType.MESSAGE.getName(), "Forma de pagamento e valor adicionado.");

        return modelAndView;
    }

    @PostMapping(path = CHECKOUT_STEP_TWO_PAYMENT_VOUCHER_URL)
    public ModelAndView addVoucherForPaymentStepTwo(@Valid VoucherIdDTO voucherIdDTO) {
        if (!saleInProgress.getVoucherAlreadyApplied()) {
            Long voucherId = voucherIdDTO.getId();
            Optional<Voucher> voucherOptional = voucherDomainService.findById(voucherId, mockVoucher);
            Voucher voucher = voucherOptional.orElseThrow(NotFoundException::new);

            saleInProgress.applyVoucher(voucher);
        }
        return ModelAndViewHelper.configure(ViewType.REDIRECT_CHECKOUT_STEP_TWO);
    }

    @GetMapping(path = CHECKOUT_STEP_THREE_URL)
    public ModelAndView initializeStepThree() {
        ModelAndView modelAndView = ModelAndViewHelper.configure(
                ViewType.CHECKOUT_STEP_SHOP,
                View.CHECKOUT_STEP_THREE,
                saleInProgress,
                ModelAttributeType.SALE);

        ModelAndViewHelper.addObjectTo(modelAndView, hash.toString(), ModelAttributeType.HASH);

        return modelAndView;
    }

    @PostMapping(path = CHECKOUT_FINISH_URL)
    public ModelAndView finish(@RequestParam String hash) {
        if (this.hash.equals(UUID.fromString(hash))) {
            saleInProgress.finish();

            Optional<SaleInProgress> saleInProgressOptional = ProxyHelper.recoveryEntityFromProxy(saleInProgress);
            Sale sale = checkoutHelper.adapt(
                    saleInProgressOptional.orElseThrow(InternalError::new)
            );
            Sale savedSale = saleDomainService.save(sale);

            if (savedSale.getVoucher() != null) {
                UserVoucher customerVoucherMock = customerVoucherHelper.provideNewObject();
                Optional<UserVoucher> foundCustomerVoucherOptional = customerVoucherAlternativeDomainService.findValidByEmbeddedEntity(
                        customerVoucherMock,
                        savedSale.getCustomer(),
                        savedSale.getVoucher()
                );
                UserVoucher foundCustomerVoucher = foundCustomerVoucherOptional
                        .orElseThrow(NotFoundException::new);

                foundCustomerVoucher.setUsed(true);
                customerVoucherAlternativeDomainService.save(foundCustomerVoucher);
            }

            Map<Long, Integer> productsAndOperationAmounts = new HashMap<>();

            for (SaleItem saleItem : savedSale.getItems()) {
                Long productStockId = saleItem.getProduct().getStock().getId();
                Integer itemOperationAmount = saleItem.getQuantity();

                productsAndOperationAmounts.put(productStockId, itemOperationAmount);
            }

            stockHelper.down(productsAndOperationAmounts);

            if (savedSale.getId() != null) {
                SessionHelper.removeAttributesForSaleFinished(
                        shopCart,
                        saleInProgress
                );

                return ModelAndViewHelper.configure(
                        ViewType.CHECKOUT_FINISH_SHOP,
                        View.FINISH,
                        savedSale,
                        ModelAttributeType.SALE);
            }
        }
        
        return ModelAndViewHelper.configure(ViewType.REDIRECT_CHECKOUT_STEP_THREE);
    }
}
