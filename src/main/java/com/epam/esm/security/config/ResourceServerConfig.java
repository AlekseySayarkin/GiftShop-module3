package com.epam.esm.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final TokenStore tokenStore;

    @Autowired
    public ResourceServerConfig(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers("/auth/login/**").anonymous()
                .antMatchers("/auth/signup/**").anonymous()
                .antMatchers(HttpMethod.GET, "/**")
                .access("#oauth2.hasScope('read') or !#oauth2.isUser()")
                .antMatchers(HttpMethod.POST, "/**")
                .access("#oauth2.hasScope('write') or !#oauth2.isUser()")
                .antMatchers(HttpMethod.DELETE, "/**")
                .access("#oauth2.hasScope('write') or !#oauth2.isUser()")
                .antMatchers(HttpMethod.PUT, "/**")
                .access("#oauth2.hasScope('write') or !#oauth2.isUser()")
                .anyRequest().authenticated();
    }
}
