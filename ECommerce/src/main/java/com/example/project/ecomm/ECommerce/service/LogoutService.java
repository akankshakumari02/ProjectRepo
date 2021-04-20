package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.token.LogoutToken;
import com.example.project.ecomm.ECommerce.repository.LogoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogoutService
{
    @Autowired
    private LogoutRepository logoutRepository;

    public void saveLogoutToken(String token)
    {
        logoutRepository.save(new LogoutToken(token));
    }

    public boolean isBlacklisted(String token)
    {
        return logoutRepository.findByToken(token) != null;
    }
}
