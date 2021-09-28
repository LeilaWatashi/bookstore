package com.watashi.bookstore.controller;

import com.watashi.bookstore.enums.View;
import com.watashi.bookstore.enums.ViewType;
import com.watashi.bookstore.exception.helper.ModelAndViewHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(path = AdminDashboardController.BASE_ADMIN_URL)
public class AdminDashboardController {

    public static final String BASE_ADMIN_URL = "/admin";
    public static final String DASHBOARD_URL = "/dashboard";


    @GetMapping(path = DASHBOARD_URL)
    public ModelAndView showDashboard() {
        return ModelAndViewHelper.configure(
                ViewType.DASHBOARD_ADMIN,
                View.ADMIN_DASHBOARD);
    }
}
