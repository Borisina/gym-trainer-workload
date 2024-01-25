package com.kolya.gym.config;

import com.kolya.gym.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Component
public class BearerTokenFilter extends GenericFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String authorizationHeader = httpServletRequest.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            int TOKEN_BEGIN_INDEX = 7;
            String jwtToken = authorizationHeader.substring(TOKEN_BEGIN_INDEX);
            if (jwtService.isTokenValid(jwtToken)){
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken("user", null, new ArrayList<>()));
            }
        }

        chain.doFilter(request, response);
    }
}
