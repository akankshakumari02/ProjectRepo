package com.example.project.ecomm.ECommerce.service;

import com.example.project.ecomm.ECommerce.entities.User.*;
import com.example.project.ecomm.ECommerce.exception.AddressNotFoundException;
import com.example.project.ecomm.ECommerce.exception.InvalidTokenException;
import com.example.project.ecomm.ECommerce.repository.AddressRepository;
import com.example.project.ecomm.ECommerce.repository.TokenRepository;
import com.example.project.ecomm.ECommerce.repository.UserRepository;
import com.example.project.ecomm.ECommerce.validatorobjects.AddressDto;
import com.example.project.ecomm.ECommerce.validatorobjects.PasswordDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;


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

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final long LOCK_TIME =  24 * 60 * 60 * 1000;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void sendActivationLinkOfCustomer(Customer customer)
    {
        ConfirmationToken confirmationToken = new ConfirmationToken(customer);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(customer.getEmail());
        mailMessage.setSubject("Registration Link");
        mailMessage.setText("Click the link to confirm your account:"+"http://localhost:8080/customer/confirm?token=" + confirmationToken.getToken());
        emailService.sendEmail(mailMessage);
    }

    public void sendActivationLinkOfSeller(Seller seller) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(seller.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("Your account has been created,waiting for Approval. ");
        emailService.sendEmail(mailMessage);
    }

    public String validateUser(String token)
    {
        final ConfirmationToken confirmationToken = tokenRepository.findByToken(token);
        if(confirmationToken == null)
        {
            return "InvalidToken";
        }

        final User user = confirmationToken.getUser();
        final Calendar cal = Calendar.getInstance();
        if((confirmationToken.getExpiryDate()
                .getTime() - cal.getTime()
                .getTime()) <= 0)
        {
            return "ExpiredToken";
/*
            sendActivationLinkOfCustomer(customer);
*/
        }

        user.setIs_Active(true);
        userRepository.save(user);
        return "ValidToken";
    }

    //resent token link Customer
    public void resendTokenLinkCustomer(Customer customer)
    {
        ConfirmationToken confirmationToken = new ConfirmationToken(customer);
        tokenRepository.save(confirmationToken);
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(customer.getEmail());
        mailMessage.setSubject("Resent Registration Link");
        mailMessage.setText("Click the link to confirm your account:"+"http://localhost:8080/customer/confirm?token=" + confirmationToken.getToken());
        emailService.sendEmail(mailMessage);
    }

    //verify token
    public User getUser(final String verifyToken)
    {
        final ConfirmationToken token = tokenRepository.findByToken(verifyToken);
        if(token != null)
        {
            return token.getUser();
        }
        return null;
    }

    //reset password
    public void resetPassword(PasswordDto passwordDto, String token)
    {
        String result = validateUser(token);
        if(result.equals("InvalidToken"))
        {
            throw new InvalidTokenException("InvalidToken");
        }

        User user = getUser(token);
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        userRepository.save(user);
    }

    public void updatePassword(String email, PasswordDto passwordDto)
    {
        User user = findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        resetSuccess(email);
        userRepository.save(user);
    }
    public void updateAddress(AddressDto addressDto, int id) {

        Optional<Address> addressOptional = addressRepository.findById(id);
        addressOptional.orElseThrow(() -> new AddressNotFoundException("Address Not Found"));
        addressOptional.ifPresent(address -> {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(addressDto,address);
            addressRepository.save(address);
        });
    }
    public void resetSuccess(String email)
    {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        //simpleMailMessage.setFrom("aakanksha021997@gmail.com");
        simpleMailMessage.setSubject("Password Reset");
        simpleMailMessage.setText("Password has been successfully reset");
        emailService.sendEmail(simpleMailMessage);
    }
   public void resendTokenLinkSeller(Seller seller)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(seller.getEmail());
        mailMessage.setSubject("Resent Registration Link");
        mailMessage.setText("Your account has been created,waiting for Approval. ");
        emailService.sendEmail(mailMessage);
    }

    public void sendLinkWithActivationMessage(User user)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Activated Account");
        mailMessage.setText("Congratulations!! Your Account is Activated");
        emailService.sendEmail(mailMessage);
    }

    public void sendLinkWithDeActivationMessage(User user)
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Deactivated Account");
        mailMessage.setText("Your Account is Deactivated");
        emailService.sendEmail(mailMessage);
    }

    /* Login Related operations */
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void resetFailedAttempts(String email)
    {
        userRepository.updateAttempts(0, email);
    }

    public void lockAccount(User user)
    {
        user.setAccountNotLocked(false);
        user.setLockTime(new Date());
        userRepository.save(user);
    }

    public boolean unlock(User user)
    {
        long lockTime = user.getLockTime().getTime();
        long currentTime = System.currentTimeMillis();

        if(lockTime + LOCK_TIME < currentTime)
        {
            user.setAccountNotLocked(true);
            user.setLockTime(null);
            user.setFailedAttempt(0);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public void increaseAttempts(User user)
    {
        int newAttempts = user.getFailedAttempt() + 1;
        userRepository.updateAttempts(newAttempts , user.getEmail());
    }

//forgot password
    public void resetPasswordLinkCustomer(Customer customer)
    {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        tokenRepository.save(confirmationToken);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(customer.getEmail());
        simpleMailMessage.setSubject("Reset Password");
        simpleMailMessage.setText("Click the Link Below to Reset your Password"+ "http://localhost:8080/confirmPasswordCustomer?token=" +confirmationToken.getToken());
        emailService.sendEmail(simpleMailMessage);
    }

    public void resetPasswordLinkSeller(Seller seller)
    {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        tokenRepository.save(confirmationToken);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(seller.getEmail());
        simpleMailMessage.setSubject("Reset Password");
        simpleMailMessage.setText("Click the Link Below to Reset your Password"+ "http://localhost:8080/confirmPasswordSeller?token=" +confirmationToken.getToken());
        emailService.sendEmail(simpleMailMessage);
    }
}
