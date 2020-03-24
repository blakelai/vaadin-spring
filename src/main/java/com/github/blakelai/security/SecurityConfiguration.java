package com.github.blakelai.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private static final String LOGOUT_SUCCESS_URL = "/";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        Set<String> googleScopes = new HashSet<>(Arrays.asList("email", "profile"));

        OidcUserService googleUserService = new OidcUserService();
        googleUserService.setAccessibleScopes(googleScopes);

        http
                // Ignore csrf for Vaadin internal request
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(SecurityUtils::isFrameworkInternalRequest)
                )

                // Restrict access to our application.
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()
                        .anyRequest().permitAll()
                )

                // Configure the login page.
                .oauth2Login(oauthLogin -> oauthLogin.userInfoEndpoint().oidcUserService(googleUserService))

                // Configure logout
                .logout(logout -> logout.logoutSuccessUrl(LOGOUT_SUCCESS_URL));
    }

    /**
     * Allows access to static resources, bypassing Spring security.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                // Vaadin Flow static resources
                "/VAADIN/**",

                // Push websocket
                "/vaadinServlet/**",

                // the standard favicon URI
                "/favicon.ico",

                // the robots exclusion standard
                "/robots.txt",

                // web application manifest
                "/manifest.webmanifest",
                "/sw.js",
                "/offline-page.html",

                // icons and images
                "/icons/**",
                "/images/**",

                // (development mode) static resources
                "/frontend/**",

                // (development mode) webjars
                "/webjars/**",

                // (development mode) H2 debugging console
                "/h2-console/**",

                // (production mode) static resources
                "/frontend-es5/**", "/frontend-es6/**");
    }
}
