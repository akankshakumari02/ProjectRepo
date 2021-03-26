package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.ConfirmationToken;
import com.example.project.ecomm.ECommerce.entities.User.Customer;
import com.example.project.ecomm.ECommerce.entities.User.Seller;
import com.example.project.ecomm.ECommerce.entities.User.User;
import com.example.project.ecomm.ECommerce.repository.TokenRepository;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class UserService
{
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public static final String INVALID = "invalidToken";
    public static final String EXPIRED = "expired";
    public static final String VALID =   "valid";

    public String createVerificationToken(final User user)
    {
        final String token = UUID.randomUUID().toString();
        final ConfirmationToken myToken = new ConfirmationToken(token,user);
        tokenRepository.save(myToken);
        return myToken.getToken();
    }

    public void sendActivationLinkWithCustomer(Customer customer)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(customer.getEmail());
        mailMessage.setSubject("Registration Link");
        mailMessage.setText("Click the link to confirm your account:"+"http://localhost:8080/customer/confirm-account?token=" + createVerificationToken(customer));
        emailService.sendEmail(mailMessage);
    }

    public void sendActivationLinkWithSeller(Seller seller) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(seller.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("Your account has been created,waiting for Approval. ");
        emailService.sendEmail(mailMessage);
    }
}
