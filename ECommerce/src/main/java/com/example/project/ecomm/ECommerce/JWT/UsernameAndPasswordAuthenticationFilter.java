package com.example.project.ecomm.ECommerce.JWT;

import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.exception.ReadingRequestBodyException;
import com.example.project.ecomm.ECommerce.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class UsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter
{
    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final UserService userService;
    private String email;
    public static final int FAILED_ATTEMPTS = 3;


    public UsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException
    {
        try {
            AuthenticationCredentials authenticationCredentials = new ObjectMapper().readValue(request.getInputStream(),AuthenticationCredentials.class);
            email = authenticationCredentials.getEmail();

            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationCredentials.getEmail(), authenticationCredentials.getPassword());

            return authenticationManager.authenticate(authentication);
        } catch (IOException exception)
        {
            throw new ReadingRequestBodyException("Can't Read Request Body");
        }
    }

    //for successful authentication
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServletException
    {
        String confirmEmail = authentication.getName(); //getting the email here
        User user = userService.findUserByEmail(confirmEmail);
        if(user.getFailedAttempt() > 0)
        {
            userService.resetFailedAttempts(confirmEmail);
        }

        String token = Jwts.builder()
                .setSubject(confirmEmail)
                .claim("authorities", authentication.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationDays())))
                .signWith(secretKey)
                .compact();

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + token);
    }

    //for unsuccessful authentication

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException) throws IOException, ServletException {
        User user = userService.findUserByEmail(email);
        if (user != null) {
            if (user.isIs_Active() && user.isAccountNotLocked()) {
                if (user.getFailedAttempt() < FAILED_ATTEMPTS - 1) {
                    userService.increaseAttempts(user);
                } else {
                    userService.lockAccount(user);
                    authenticationException = new LockedException("Account has been locked due to 3 failed login attempts.");

                    new ObjectMapper().writeValue(response.getWriter(), authenticationException);
                }
            }
                 else if (!user.isAccountNotLocked()) {
                    if (userService.unlock(user)) {
                        authenticationException = new LockedException("Account has been unlocked. Login Again");
                        new ObjectMapper().writeValue(response.getWriter(), authenticationException);
                    }
                }
            }

        super.unsuccessfulAuthentication(request,response,authenticationException);
    }
}
