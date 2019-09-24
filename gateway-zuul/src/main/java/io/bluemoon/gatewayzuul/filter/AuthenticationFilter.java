package io.bluemoon.gatewayzuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        RequestContext ctx = RequestContext.getCurrentContext();
        if (auth instanceof OAuth2Authentication) {
            Object details = auth.getDetails();
            if (details instanceof OAuth2AuthenticationDetails) {
                OAuth2AuthenticationDetails oauth = (OAuth2AuthenticationDetails) details;
                ctx = RequestContext.getCurrentContext();
                return true;
            }
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {

        String contextUri = RequestContext.getCurrentContext().getRequest().getRequestURI();
        if (contextUri.equals("/uaa/user")) {
            try {
                RequestContext.getCurrentContext().getResponse().sendRedirect("http://localhost:8765/");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }
}
