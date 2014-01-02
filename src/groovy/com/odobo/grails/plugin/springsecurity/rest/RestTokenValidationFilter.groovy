package com.odobo.grails.plugin.springsecurity.rest

import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.filter.GenericFilterBean

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * Created by mariscal on 23/12/13.
 */
class RestTokenValidationFilter extends GenericFilterBean {

    String headerName

    RestAuthenticationProvider restAuthenticationProvider

    AuthenticationSuccessHandler authenticationSuccessHandler
    AuthenticationFailureHandler authenticationFailureHandler

    @Override
    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = request

        String tokenValue = servletRequest.getHeader(headerName)

        if (tokenValue) {
            try {
                RestAuthenticationToken authenticationRequest = new RestAuthenticationToken(tokenValue)
                RestAuthenticationToken authenticationResult = restAuthenticationProvider.authenticate(authenticationRequest)

                SecurityContextHolder.context.setAuthentication(authenticationResult)

                authenticationSuccessHandler.onAuthenticationSuccess(request, response, authenticationResult)
            } catch (AuthenticationException ae) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, ae)
            }
        }

        chain.doFilter(request, response)
    }
}
