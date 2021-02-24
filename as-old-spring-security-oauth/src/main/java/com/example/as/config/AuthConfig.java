package com.example.as.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * Author: R K Rahu
 *
 * Authorization Server project (Spring Security OAuth) has been discarded from Spring Security platform.
 * Now they have started new community experimental project for Authorization Server.
 *
 * EOL of Spring Security OAuth (https://spring.io/blog/2020/05/07/end-of-life-for-spring-security-oauth).
 * Announcement (https://spring.io/blog/2020/05/07/end-of-life-for-spring-security-oauth).
 *
 * This Authorization Server project is build on older version provided for Authorization Server by Spring Team.
 * This project build on 2.3.8.RELEASE of spring security OAuth2.
 *
 * They have deprecated "spring-security-oauth2" after this release "2.3.X"
 */

@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Oauth2 Default token URLs setter, AuthorizationServerSecurityConfiguration#configure()
     * It configure "/oauth/token" , "/oauth/token_key" and "/oauth/check_token"
     *
     * Resource Server make a call for token authentication.
     * #RequestMapping(value = "/oauth/check_token") of Auth server get called by Resource server for token validation.
     *
     * Common built-in expressions
     *
     * Expression	                Description
     * hasRole([role])	            Returns true if the current principal has the specified role.
     * hasAnyRole([role1,role2])	Returns true if the current principal has any of the supplied roles (given as a comma-separated list of strings)
     * principal	                Allows direct access to the principal object representing the current user
     * authentication	            Allows direct access to the current Authentication object obtained from the SecurityContext
     * permitAll	                Always evaluates to true
     * denyAll	                    Always evaluates to false
     * isAnonymous()	            Returns true if the current principal is an anonymous user
     * isRememberMe()	            Returns true if the current principal is a remember-me user
     * isAuthenticated()	        Returns true if the user is not anonymous
     * isFullyAuthenticated()	    Returns true if the user is not an anonymous or a remember-me user
     *
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
//                .tokenKeyAccess("permitAll()") //It allows "/oauth/token_key" to all
//                and public key (if available) get exposed by the Authorization Server
                .checkTokenAccess("isAuthenticated()"); // It makes "/oauth/check_token" exposed to Resource Server.
    }

    /**
     *  In case of generic token store, we just need to provide token store.
     *  But in case of JWT token store we need add token store and accessTokenConverter to the end points
     *
     * @param endpoints
     */
    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .tokenStore(tokenStore())
//                .tokenStore(getJwtTokenStore())
//                .accessTokenConverter(getJwtAccessTokenConverter())
                .authenticationManager(authenticationManager);

    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("client")
                .secret("secret")
                .scopes("read", "write")
                .authorizedGrantTypes("client_credentials")
                .accessTokenValiditySeconds(3600);
    }

    /** It generates opaque token, which can not be described by issuer or client id or resource id.
     *  That's why validation of this key could be done by only this Authorization Server.
     *
     *  Resource server have to contact to this authorization server for validation of token.
     *  By "/oauth/check_token" url end token validation will be done.
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }


    /**
     * It generate JWT token, which is self explanatory.
     * So If we generate JWT token with the same signing key we don't need to contact to this Authorization Server.
     * In this scenario we don't need to configure "/oauth/check_token" API end in Authorization server.
     * we can omit "checkTokenAccess("isAuthenticated()")" from AuthorizationServerSecurityConfigurer.
     *
     * @return
     */
    @Bean
    public TokenStore getJwtTokenStore() {
        JwtTokenStore jwtTokenStore = new JwtTokenStore(getJwtAccessTokenConverter());
        return jwtTokenStore;
    }

    /**
     * @return
     */
    @Bean
    public JwtAccessTokenConverter getJwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("123");
        return jwtAccessTokenConverter;
    }
}
