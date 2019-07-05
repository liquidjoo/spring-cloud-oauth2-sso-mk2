package io.bluemoon.zuuloauth2.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class AuthenticationFilter extends ZuulFilter {
    private static final int FILTER_ORDER = 2;
    private static final boolean SHOULD_FILTER = false;

    private FilterUtils filterUtils;
    private RestTemplate restTemplate;

    public AuthenticationFilter(
            FilterUtils filterUtils,
            RestTemplate restTemplate
    ) {
        this.filterUtils = filterUtils;
        this.restTemplate = restTemplate;
    }

    @Override
    public String filterType() {
        return FilterUtils.PRE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private boolean isAuthTokenPresent() {
        if (filterUtils.getAuthToken() != null) {
            return true;
        }
        return false;
    }



    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        log.debug("what???{}", ctx);
        return null;
    }
}
