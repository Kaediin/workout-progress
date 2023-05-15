package com.daiken.workoutprogress.auth.config;

import org.apache.catalina.connector.RequestFacade;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GraphQLResponseHeaderFilter implements Filter {

    private static final String GRAPHQL_URI = "/graphql";

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
