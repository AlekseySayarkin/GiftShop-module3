package com.epam.esm.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    private final ApplicationContext context;

    @Autowired
    public MethodSecurityConfiguration(ApplicationContext context) {
        this.context = context;
    }

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        var expressionHandler = new OAuth2MethodSecurityExpressionHandler();
        expressionHandler.setApplicationContext(context);
        return expressionHandler;
    }
}
