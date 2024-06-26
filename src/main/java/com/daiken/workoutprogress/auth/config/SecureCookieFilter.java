package com.daiken.workoutprogress.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collection;

/**
 * Filter to secure cookies
 */
@Slf4j
public class SecureCookieFilter extends GenericFilterBean {

    private static final String COOKIE_SPEC_PRODUCTION = "SameSite=Strict;Secure";


    /**
     * Process the request and response
     *
     * @param _request  The request to process
     * @param _response The response associated with the request
     * @param chain     Provides access to the next filter in the chain for this filter to pass the request
     */
    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) _request;
        HttpServletResponse response = (HttpServletResponse) _response;

        Collection<String> setCookieHeaders = response.getHeaders(HttpHeaders.SET_COOKIE);
        boolean first = true;

        String cookieSpec = COOKIE_SPEC_PRODUCTION;

        for (String setCookieHeader : setCookieHeaders) {
            if (first) {
                log.debug("doFilter: handling first Set-Cookie (" + setCookieHeader + ")");
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s;%s", setCookieHeader,
                        cookieSpec));
                first = false;
            } else {
                log.debug("doFilter: handling additional Set-Cookie (" + setCookieHeader + ")");
                response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s;%s", setCookieHeader,
                        cookieSpec));
            }
        }

        chain.doFilter(_request, _response);
    }

}
