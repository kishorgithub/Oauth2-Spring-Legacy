package com.example.rs.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * Author: R K Rahu
 *
 * Resource server API end(protected) can be access by user only with the valid token key.
 * Token could be opaque(lot of variant) or JWT.
 *
 * In Case of JWT, if we generate JWT token with the same signing key then we can avoid calling Authorization Server.
 * Jwt is self explanatory, we just need to sign the token with Authorization Server signing Key.
 * Resource Server have to configure JWT token in ResourceServerConfigurerAdapter.
 * This scenario works where Resource and Authorization server both work under same organization/environment.
 *
 * If we have opaque token, then we need to consult Authorization Server with no exception.
 * In this scenario, we have configure client id, secret key and token info uri ("{authorization_url}/oauth/check_token").
 * Resource Server call Authorization Server API end(/oauth/check_token) to validate token.
 */

@Configuration
@EnableResourceServer
public class ResourceServerConfig {
}
