package com.watashi.bookstore.exception.helper;

import com.watashi.bookstore.enums.*;
import com.watashi.bookstore.exception.NotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class ModelAndViewHelper {

    public static ModelAndView configure(final ViewType eViewType) {
        String view = computeViewByEntityType(eViewType);
        return extractConfiguredFrom(view);
    }

    public static ModelAndView configure(final ViewType eViewType, Long pathVariable) {
        String view = computeViewByEntityType(eViewType);
        String pathVariableToReplace = "/".concat(pathVariable.toString());

        view = view.replace(Endpoint.PATH_VARIABLE.getPath(), pathVariableToReplace);

        return extractConfiguredFrom(view);
    }

    public static ModelAndView configure(final ViewType eViewType, final Enum<?> eView) {
        String view = computeViewByEntityType(eViewType, eView);
        return extractConfiguredFrom(view);
    }

    public static ModelAndView configure(final ViewType eViewType,
                                         final Enum<?> eView, Object additionalObject,
                                         ModelAttributeType eModelAttributeType) {
        String view = computeViewByEntityType(eViewType, eView);
        return extractConfiguredFrom(view, additionalObject, eModelAttributeType);
    }

    private static ModelAndView extractConfiguredFrom(String view) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(view);
        return modelAndView;
    }

    private static ModelAndView extractConfiguredFrom(String view, Object additionalObject, ModelAttributeType eModelAttributeType) {
        ModelAndView modelAndView = extractConfiguredFrom(view);
        addObjectTo(modelAndView, additionalObject, eModelAttributeType);
        return modelAndView;
    }

    public static void addObjectTo(ModelAndView target, Object object,
                                   ModelAttributeType eModelAttributeType) {
        target.addObject(eModelAttributeType.getName(), object);
    }

    public static void addObjectTo(ModelAndView target, Collection<?> objects, ModelAttributeType eModelAttributeType) {
        target.addObject(eModelAttributeType.getName(), objects);
    }

    public static void addModelMapTo(ModelAndView target, Map<String, ?> modelMap) {
        target.addAllObjects(modelMap);
    }

    private static Collection<Enum<?>> identifyPaths(final ViewType eViewType) {
        Collection<Enum<?>> paths = new ArrayList<>();

        switch (eViewType) {

            case CUSTOMER_ACCOUNT_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CUSTOMER);
                paths.add(PageFolder.ACCOUNT);
                break;
            case CUSTOMER_ADDRESS_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CUSTOMER);
                paths.add(PageFolder.ADDRESS);
                break;
            case CUSTOMER_CREDIT_CARD_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CUSTOMER);
                paths.add(PageFolder.CREDIT_CARD);
                break;

            case CUSTOMER_ORDER_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CUSTOMER);
                paths.add(PageFolder.ORDER);
                break;

            case CUSTOMER_TRADE_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CUSTOMER);
                paths.add(PageFolder.TRADE);
                break;

            case PRODUCT_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.PRODUCT);
                break;

            case PRODUCT_ADMIN:
                paths.add(PageFolder.ADMIN);
                paths.add(PageFolder.PRODUCT);
                break;

            case STOCK_ADMIN:
                paths.add(PageFolder.ADMIN);
                paths.add(PageFolder.STOCK);
                break;

            case STOCK_HISTORY_ADMIN:
                paths.add(PageFolder.ADMIN);
                paths.add(PageFolder.STOCK);
                paths.add(PageFolder.STOCK_HISTORY);
                break;

            case CART_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CART);
                break;

            case CHECKOUT_STEP_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CHECKOUT);
                paths.add(PageFolder.STEP);
                break;

            case CHECKOUT_FINISH_SHOP:
                paths.add(PageFolder.SHOP);
                paths.add(PageFolder.CHECKOUT);
                break;

            case SALE_ADMIN:
                paths.add(PageFolder.ADMIN);
                paths.add(PageFolder.SALE);
                break;

            case TRADE_ADMIN:
                paths.add(PageFolder.ADMIN);
                paths.add(PageFolder.TRADE);
                break;

            case REDIRECT_TRADE_IN_PROGRESS:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ORDER);
                paths.add(Endpoint.TRADE);
                paths.add(Endpoint.REQUEST);
                paths.add(Endpoint.IN_PROGRESS);
                paths.add(Endpoint.ITEMS);
                break;

            case REDIRECT_TRADE_SHOP_FINISH:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ORDER);
                paths.add(Endpoint.TRADE);
                paths.add(Endpoint.REQUEST);
                paths.add(Endpoint.FINISH);
                break;

            case REDIRECT_STOCKS_ADMIN:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ADMIN);
                paths.add(Endpoint.STOCKS);
                break;

            case REDIRECT_NEW_STOCK_ADMIN:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ADMIN);
                paths.add(Endpoint.STOCKS);
                paths.add(Endpoint.NEW);
                break;

            case REDIRECT_MANAGE_STOCK_ADMIN:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ADMIN);
                paths.add(Endpoint.STOCKS);
                paths.add(Endpoint.PATH_VARIABLE);
                paths.add(Endpoint.MANAGE);
                break;

            case REDIRECT_SALES_ADMIN:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ADMIN);
                paths.add(Endpoint.SALES);
                break;

            case REDIRECT_TRADES_ADMIN:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.ADMIN);
                paths.add(Endpoint.TRADES);
                break;

            case REDIRECT_PRODUCT_SHOP:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.SHOP);
                paths.add(Endpoint.PRODUCTS);
                break;

            case REDIRECT_CHECKOUT_STEP_ONE:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.SHOP);
                paths.add(Endpoint.CHECKOUT);
                paths.add(Endpoint.STEP);
                paths.add(Endpoint.STEP_ONE);
                break;

            case REDIRECT_CHECKOUT_STEP_TWO:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.SHOP);
                paths.add(Endpoint.CHECKOUT);
                paths.add(Endpoint.STEP);
                paths.add(Endpoint.STEP_TWO);
                break;

            case REDIRECT_CHECKOUT_STEP_THREE:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.SHOP);
                paths.add(Endpoint.CHECKOUT);
                paths.add(Endpoint.STEP);
                paths.add(Endpoint.STEP_THREE);
                break;

            case REDIRECT_LOGIN_APPLICATION:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.AUTH);
                paths.add(Endpoint.LOGIN);
                break;

            case REDIRECT_CUSTOMER_ACCOUNT_SHOP:
                paths.add(View.REDIRECT);
                paths.add(Endpoint.CUSTOMER);
                paths.add(Endpoint.ACCOUNT);
                break;

            case AUTH_APPLICATION:
                paths.add(PageFolder.AUTH);
                break;

            case DASHBOARD_ADMIN:
                paths.add(PageFolder.ADMIN);
                break;

            default:
                throw new NotFoundException();
        }

        return paths;
    }

    private static String computeViewByEntityType(final ViewType eViewType) {
        Collection<Enum<?>> paths = identifyPaths(eViewType);
        return PathBuilderHelper.build(paths);
    }

    private static String computeViewByEntityType(final ViewType eViewType, final Enum<?> eView) {
        Collection<Enum<?>> paths = new ArrayList<>();

        paths.add(PageFolder.PAGES);
        paths.addAll(identifyPaths(eViewType));
        paths.add(eView);

        return PathBuilderHelper.build(paths);
    }
}
