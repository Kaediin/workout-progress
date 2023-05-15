package com.daiken.workoutprogress.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

    @Value("${cors.allowed_origins}")
    private String allowedOrigins;

    public CorsFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (response instanceof final HttpServletResponse httpServletResponse && request instanceof final HttpServletRequest httpServletRequest) {
            final String origin = ((HttpServletRequest) request).getHeader("Origin");

            if (Arrays.stream(this.allowedOrigins.split(","))
                    .map(String::trim)
                    .anyMatch(s -> s.equals("*") || s.equalsIgnoreCase(origin))) {
                httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
            } else {
                httpServletResponse.addHeader("Error", "[CorsFilter] Rejected request. The allowed " +
                        "origins does NOT include the following origin: " + origin);
            }

            httpServletResponse.setHeader(
                    "Access-Control-Allow-Methods",
                    "OPTIONS, HEAD, GET, PUT, POST, DELETE, PATCH"
            );
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, sentry-trace");
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("Access-Control-Max-Age", "3600");

            if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);

                return;
            }
        }
        chain.doFilter(request, response);
    }

}
