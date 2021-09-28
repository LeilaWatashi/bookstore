package com.watashi.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/shop/books");
        registry.addRedirectViewController("/shop", "/shop/books");
        registry.addRedirectViewController("/shop/products", "/shop/books");
    }

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        super();
        this.userDetailsService = userDetailsService;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                    .configurationSource(request -> new CorsConfiguration()
                            .applyPermitDefaultValues())
                    .and()
                .headers()
                    .disable()
                    .authorizeRequests()
                        .antMatchers(
                                "/assets/**",

                                "/dashboard/sales/**",

                                "/auth/login",
                                "/doLogin",
                                "/logout",
                                "/auth/sign-up",

                                "/shop",
                                "/shop/cart/**",
                                "/shop/books/**",
                                "/"
                        )
                            .permitAll()
                                .and()
                            .csrf()
                                .disable()
                            .antMatcher("/**")
                                .authorizeRequests()
                                .anyRequest()
                                .authenticated()
                            .and()
                        .formLogin()
                            .loginPage("/auth/login")
                            .loginProcessingUrl("/doLogin")
                            .usernameParameter("username")
                            .passwordParameter("password")
                            .failureUrl("/auth/login?error=")
                            .defaultSuccessUrl("/shop/books")

                        .and()
                            .logout()
                                .logoutSuccessUrl("/auth/login?logout=true")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID");
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        PasswordEncoder encoder = new BCryptPasswordEncoder();
//        return encoder;
//    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
