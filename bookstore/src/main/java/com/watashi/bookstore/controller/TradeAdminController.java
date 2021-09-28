package com.watashi.bookstore.controller;

import com.watashi.bookstore.entity.shop.Trade;
import com.watashi.bookstore.entity.shop.TradeStatus;
import com.watashi.bookstore.entity.shop.Voucher;
import com.watashi.bookstore.entity.shop.VoucherType;
import com.watashi.bookstore.entity.stock.StockHistory;
import com.watashi.bookstore.entity.user.UserVoucher;
import com.watashi.bookstore.enums.ModelAttributeType;
import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.InternalServerErrorException;
import com.watashi.bookstore.exception.NotFoundException;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import com.watashi.bookstore.exception.helper.StockHelper;
import com.watashi.bookstore.exception.helper.ViewMessageHelper;
import com.watashi.bookstore.exception.helper.VoucherHelper;
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
@RequestMapping(path = TradeAdminController.BASE_TRADE_ADMIN_URL)
public class TradeAdminController {

    public static final String BASE_TRADE_ADMIN_URL = "/admin/trades";
    public static final String UPDATE_STATUS_URL = "/{id}/update-status";
    public static final String GENERATE_VOUCHER_URL = "/{tradeId}/generate-voucher";

    private final IDomainService<Trade> tradeDomainService;
    private final IDomainService<Voucher> voucherDomainService;

    private final Trade mockTrade;

    private StockHelper stockHelper;
    private VoucherHelper voucherHelper;

    @Autowired
    public TradeAdminController(@Qualifier("domainService")
                                            IDomainService<Trade> tradeDomainService,
                                @Qualifier("domainService")
                                IDomainService<Voucher> voucherDomainService,
                                Trade mockTrade,
                                StockHelper stockHelper,
                                VoucherHelper voucherHelper) {
        this.tradeDomainService = tradeDomainService;
        this.voucherDomainService = voucherDomainService;
        this.mockTrade = mockTrade;
        this.stockHelper = stockHelper;
        this.voucherHelper = voucherHelper;
    }

    @GetMapping
    public ModelAndView findAll() {
        List<Trade> trades = tradeDomainService.findAll(mockTrade);

        return ModelAndViewHelper.configure(
                ViewType.TRADE_ADMIN,
                View.LIST,
                trades,
                ModelAttributeType.TRADES);
    }

    @PostMapping(path = UPDATE_STATUS_URL)
    public ModelAndView updateStatus(@PathVariable Long id,
                                     TradeStatus newStatus,
                                     RedirectAttributes redirectAttributes) {
        Optional<Trade> foundTradeOptional = tradeDomainService.findById(id, mockTrade);
        Trade trade = foundTradeOptional.orElseThrow(NotFoundException::new);
        TradeStatus oldStatus = trade.getStatus();

        trade.setStatus(newStatus);

        String extraMessage = "";
        if (trade.isReceivedItems()) {
            List<StockHistory> stockHistoriesOperation = stockHelper.up(trade.getItems());
            Integer totalAmountReturnedToStocks = stockHistoriesOperation.stream()
                    .map(StockHistory::getAmount)
                    .reduce(0, (Integer::sum));

            extraMessage = "*Obs: foram retornados no total " + totalAmountReturnedToStocks
                    + " itens aos seus respectivos estoques. ";
        }

        Trade updatedTrade = tradeDomainService.save(trade);

        boolean isSuccess = updatedTrade.getStatus() == newStatus;
        String tradeTypeInLowerCase = trade.getType().getDisplayName().toLowerCase();

        String successMessage = (!extraMessage.isEmpty() ? extraMessage : "") +
                "O status da " + tradeTypeInLowerCase + " foi alterado com sucesso de \""
                + oldStatus.getDisplayName() + "\" para \"" + newStatus.getDisplayName() + "\".";
        String errorMessage = "Ocorreu um erro ao tentar alterar o status da " + tradeTypeInLowerCase + " \""
                + oldStatus.getDisplayName() + "\" para \"" + newStatus.getDisplayName() + "\".";

        String message = isSuccess ? successMessage : errorMessage;
        redirectAttributes.addFlashAttribute(ModelAttributeType.MESSAGE.getName(), message);
        redirectAttributes.addFlashAttribute(ModelAttributeType.IS_SUCCESS_MESSAGE.getName(), isSuccess);

        return ModelAndViewHelper.configure(ViewType.REDIRECT_TRADES_ADMIN);
    }

    @PostMapping(path = GENERATE_VOUCHER_URL)
    public ModelAndView generateVoucher(@PathVariable Long tradeId, RedirectAttributes redirectAttributes) {
        Optional<Trade> tradeOptional = tradeDomainService.findById(tradeId, mockTrade);
        Trade foundTrade = tradeOptional.orElseThrow(NotFoundException::new);

        if (!foundTrade.isReceivedItems()) {
            return ModelAndViewHelper.configure(ViewType.REDIRECT_TRADES_ADMIN);
        }

        if (foundTrade.getItems().isEmpty()) {
            throw new InternalServerErrorException();
        }

        Double voucherValue = foundTrade.calculateTotalBalanceOfItems();

        Voucher savedVoucher = voucherHelper
                .adaptAndSave(voucherValue, VoucherType.TRADE);

        UserVoucher savedCustomerVoucher = voucherHelper
                .adaptAndSave(foundTrade.getOrder().getCustomer(), savedVoucher);

        foundTrade.changeStatusGeneratedVoucher();
        tradeDomainService.save(foundTrade);

        String message = savedCustomerVoucher.getVoucher().formatToText()
                + " gerado com sucesso para o cliente \""
                + savedCustomerVoucher.getCustomer().getName() + "\".";

        ViewMessageHelper.configureRedirectMessageWith(message, true, redirectAttributes);

        return ModelAndViewHelper.configure(ViewType.REDIRECT_TRADES_ADMIN);
    }
}
