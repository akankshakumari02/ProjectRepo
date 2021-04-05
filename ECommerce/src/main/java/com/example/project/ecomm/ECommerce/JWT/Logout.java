package com.example.project.ecomm.ECommerce.JWT;

import com.example.project.ecomm.ECommerce.exception.ExceptionResponse;
import com.example.project.ecomm.ECommerce.service.LogoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
public class Logout implements LogoutSuccessHandler
{
    @Autowired
    private LogoutService logoutService;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String header = httpServletRequest.getHeader("Authorization");

        if(header != null)
        {
            String token = header.substring(4);
            logoutService.saveLogout(token);
            ExceptionResponse response = new ExceptionResponse(new Date(), "Logout", "Successful Logout");
            new ObjectMapper().writeValue(httpServletResponse.getWriter(), response);
        }
    }
}
