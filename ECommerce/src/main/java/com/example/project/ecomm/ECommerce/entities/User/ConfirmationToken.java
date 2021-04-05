package com.example.project.ecomm.ECommerce.entities.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationToken
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String token;

    //unidirectional
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private Date expiryDate = calculateExpiryDate(8);

    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    public ConfirmationToken(User user)
    {
        this.token = UUID.randomUUID().toString();
        this.user = user;
        this.creationDate = new Date();
    }



    private Date calculateExpiryDate(int expiryTimeInMinutes)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
