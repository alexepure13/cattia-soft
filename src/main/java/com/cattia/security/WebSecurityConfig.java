package com.cattia.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] STATICCONTENT = {ContextPath.BOWER.getContextString(), ContextPath.BUILD.getContextString(), ContextPath.CUSTOM.getContextString(), ContextPath.DIST.getContextString(), ContextPath.PLUGINS.getContextString(), ContextPath.FAVICON.getContextString()};

    private final UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()

                .authorizeRequests()
                .antMatchers(STATICCONTENT).permitAll()

                .antMatchers("/").hasAuthority("INDEX_VIEW")
                .antMatchers("/index").hasAuthority("INDEX_VIEW")
                .antMatchers("/dashboard").hasAuthority("INDEX_VIEW")
                .antMatchers("/userAccountOverview").hasAuthority("USER_ACCOUNT_VIEW")
                .antMatchers("/editUserAccount").hasAuthority("USER_ACCOUNT_EDIT")
                .antMatchers("/editPassword").hasAuthority("USER_ACCOUNT_EDIT_PASSWORD")
                .antMatchers("/deactivateUserAccount").hasAuthority("USER_ACCOUNT_DEACTIVATE")
                .antMatchers("/activateUserAccount").hasAuthority("USER_ACCOUNT_ACTIVATE")
                .antMatchers("/userAccountForm").hasAuthority("USER_ACCOUNT_CREATE_FORM")
                .antMatchers("/submitNewUserAccount").hasAuthority("USER_ACCOUNT_SUBMIT_NEW")
                .antMatchers("/submitExistingUserAccount").hasAuthority("USER_ACCOUNT_SUBMIT_EXISTING")
                .antMatchers("/saveNewPassword").hasAuthority("USER_ACCOUNT_SUBMIT_PASSWORD")
                .antMatchers("/refugeeOverview").hasAuthority("REFUGEE_VIEW")
                .antMatchers("/deactivateRefugee").hasAuthority("REFUGEE_DEACTIVATE")
                .antMatchers("/activateRefugee").hasAuthority("REFUGEE_ACTIVATE")
                .antMatchers("/createRefugee").hasAuthority("REFUGEE_CREATE_FORM")
                .antMatchers("/editRefugee").hasAuthority("REFUGEE_EDIT")
                .antMatchers("/submitRefugee").hasAuthority("REFUGEE_SUBMIT")
                .antMatchers("/assignProducts").hasAuthority("REFUGEE_ASSIGN_PRODUCTS")
                .antMatchers("/saveToOrder").hasAuthority("REFUGEE_SAVE_TO_ORDER")
                .antMatchers("/saveOrderItem").hasAuthority("REFUGEE_SAVE_ORDER_ITEM")
                .antMatchers("/removeOrderItem").hasAuthority("REFUGEE_REMOVE_ORDER_ITEM")
                .antMatchers("/confirmOrder").hasAuthority("REFUGEE_CONFIRM_ORDER")
                .antMatchers("/productOverview").hasAuthority("WAREHOUSE_VIEW")
                .antMatchers("/updateStock").hasAuthority("WAREHOUSE_UPDATE_STOCK")
                .antMatchers("/saveProductStock").hasAuthority("WAREHOUSE_SUBMIT_PRODUCT_STOCK")
                .antMatchers("/deactivateProduct").hasAuthority("WAREHOUSE_DEACTIVATE_PRODUCT")
                .antMatchers("/activateProduct").hasAuthority("WAREHOUSE_ACTIVATE_PRODUCT")
                .antMatchers("/createProduct").hasAuthority("WAREHOUSE_CREATE_PRODUCT_FORM")
                .antMatchers("/editProduct").hasAuthority("WAREHOUSE_EDIT_PRODUCT")
                .antMatchers("/submitProduct").hasAuthority("WAREHOUSE_SUBMIT_PRODUCT")
                .antMatchers("/license").hasAuthority("VIEW_LICENSE")
                .antMatchers("/profile").hasAuthority("VIEW_PROFILE")
                .anyRequest().authenticated()

                .and().formLogin().loginProcessingUrl("/signin").loginPage("/login").defaultSuccessUrl("/", true).permitAll()
                .usernameParameter("cattia-username")
                .passwordParameter("cattia-password")
                .and()

                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login").clearAuthentication(true)

                .and().exceptionHandling().accessDeniedPage("/accessDenied");
        http.headers().frameOptions().disable();
    }
}
