package com.daiken.workoutprogress.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Collection;

public class SecureCookieFilter extends GenericFilterBean {

    private static final String COOKIE_SPEC_PRODUCTION = "SameSite=Strict;Secure";

    private static final Logger logger = LoggerFactory.getLogger(SecureCookieFilter.class);

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
                logger.debug("doFilter: handling first Set-Cookie (" + setCookieHeader + ")");
                response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s;%s", setCookieHeader,
                        cookieSpec));
                first = false;
            } else {
                logger.debug("doFilter: handling additional Set-Cookie (" + setCookieHeader + ")");
                response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s;%s", setCookieHeader,
                        cookieSpec));
            }
        }

        chain.doFilter(_request, _response);
    }

}
