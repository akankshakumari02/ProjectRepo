package com.example.project.ecomm.ECommerce.security;

import com.example.project.ecomm.ECommerce.JWT.JwtConfig;
import com.example.project.ecomm.ECommerce.JWT.TokenVerifier;
import com.example.project.ecomm.ECommerce.JWT.UsernameAndPasswordAuthenticationFilter;
import com.example.project.ecomm.ECommerce.service.AppUserDetailsService;
import com.example.project.ecomm.ECommerce.service.LogoutService;
import com.example.project.ecomm.ECommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;
    private final AppUserDetailsService appUserDetailsService;
    private final UserService userService;
    private final LogoutService logoutService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    public ApplicationSecurityConfiguration(JwtConfig jwtConfig, SecretKey secretKey, AppUserDetailsService appUserDetailsService,
                                     UserService userService, LogoutService logoutService)
    {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
        this.appUserDetailsService = appUserDetailsService;
        this.userService = userService;
        this.logoutService = logoutService;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(appUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http.csrf().disable()
              .addFilter(new UsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey, userService))
              .addFilterAfter(new TokenVerifier(secretKey, jwtConfig, logoutService), UsernameAndPasswordAuthenticationFilter.class)
              .authorizeRequests()
              .antMatchers("/admin/**").hasAnyRole("ADMIN")
              .antMatchers("/seller/**").hasAnyRole("SELLER")
              .antMatchers("/customer/**").hasAnyRole("CUSTOMER")
              .antMatchers("/","/register/**").permitAll()
              .antMatchers("/").permitAll()
              .anyRequest().authenticated()
              .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .logout().logoutSuccessHandler(logoutSuccessHandler);
  }
}
