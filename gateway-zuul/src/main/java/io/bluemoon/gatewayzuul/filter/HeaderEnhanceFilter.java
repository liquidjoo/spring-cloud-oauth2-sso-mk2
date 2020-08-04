package io.bluemoon.gatewayzuul.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HeaderEnhanceFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorization = ((HttpServletRequest) request).getHeader("Authorization");
        String requestURI = ((HttpServletRequest) request).getRequestURI();
        // test if request url is permit all, then remove authorization from header


    }
}
