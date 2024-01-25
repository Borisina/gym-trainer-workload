package com.kolya.gym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kolya.gym.exception.InvalidTokenException;
import com.kolya.gym.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class BearerTokenFilter extends GenericFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            sendError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Bearer token is missing");
            return;
        }

        int TOKEN_BEGIN_INDEX = 7;
        String jwtToken = authorizationHeader.substring(TOKEN_BEGIN_INDEX);

        try{
            jwtService.validateToken(jwtToken);
        }catch (InvalidTokenException | SignatureException e){
            sendError(httpResponse, HttpServletResponse.SC_FORBIDDEN, "Invalid Bearer token");
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", null, new ArrayList<>()));

        chain.doFilter(request, response);
    }

    private void sendError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");

        String json = new ObjectMapper().writeValueAsString(Collections.singletonMap("error", message));

        response.getWriter().write(json);
    }
}
