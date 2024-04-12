package com.daiken.workoutprogress.auth.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Filter to add security headers to GraphQL responses
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GraphQLResponseHeaderFilter implements Filter {

    private static final String GRAPHQL_URI = "/graphql";

    /**
     * Process the request and response
     *
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        if (request instanceof RequestFacade &&
                GRAPHQL_URI.equalsIgnoreCase(((RequestFacade) request).getRequestURI())) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            httpServletResponse.setHeader("Strict-Transport-Security", "max-age=31536000; " +
                    "includeSubDomains; preload");
            httpServletResponse.setHeader("Content-Security-Policy", "default-src 'self'");
        }

        chain.doFilter(request, response);
    }

}
