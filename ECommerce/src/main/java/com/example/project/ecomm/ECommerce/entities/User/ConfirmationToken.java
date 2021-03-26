package com.example.project.ecomm.ECommerce.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken
{
    private static final int EXPIRATION = 60*24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String token;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public ConfirmationToken(String token, User user)
    {
        this.token = token;
        this.user = user;
        this.creationDate = new Date();
    }

    private Date expiryDate = calculateExpiryDate(20);
    private Date creationDate;

    private Date calculateExpiryDate(int expiryTimeInMinutes)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
