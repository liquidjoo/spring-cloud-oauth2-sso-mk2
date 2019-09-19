package io.bluemoon.authorizationserver.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        System.out.println("---------custom access denied handler");
        System.out.println(request.getRequestURI());
        System.out.println(accessDeniedException.getMessage());

        response.setContentType("application/json;charset=UTF-8");
        Map map = new HashMap();
        map.put("errorauth", "400");
        map.put("message", accessDeniedException.getMessage());
        map.put("path", request.getServletPath());
        map.put("timestamp", LocalDateTime.now().toString());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), map);
        } catch (Exception e) {
            throw new ServletException();
        }
    }
}
