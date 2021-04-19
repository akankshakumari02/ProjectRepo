package com.example.project.ecomm.ECommerce.JWT;

import com.example.project.ecomm.ECommerce.exception.ExceptionResponse;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.service.LogoutService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import com.nimbusds.oauth2.sdk.ErrorResponse;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Logout implements LogoutSuccessHandler
{
    @Autowired
    private LogoutService logoutService;

    private final SecretKey secretKey;

    public Logout(SecretKey secretKey)
    {
        this.secretKey = secretKey;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if(header != null)
        {
            String token = header.substring(7);
            if(logoutService.isBlacklisted(token))
            {
                throw new InvalidTokenException("Invalid Token");
            }

            try
            {
                Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
                logoutService.saveLogoutToken(token);
                String details = "You have been logout successfully.";
                ExceptionResponse error = new ExceptionResponse(new Date(), "Logout", details);
                //response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getWriter(), error);            }
            catch (JwtException exception)
            {
                throw new InvalidTokenException("Invalid Token");
            }
            /*logoutService.saveLogoutToken(token);
            ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), "Logout", "Successful Logout");
            new ObjectMapper().writeValue(response.getWriter(), exceptionResponse);
*/        }
    }
}
