package com.example.project.ecomm.ECommerce.entities.User;

import com.example.project.ecomm.ECommerce.token.ConfirmationToken;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@JsonFilter("Filter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;
    private String firstName;
    private String middleName;
    private String lastName;

    private String password;


    private boolean Is_Deleted = false;
    private boolean Is_Active = false;
    private boolean accountNotLocked = true;
    private int failedAttempt;
    private Date lockTime;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roleList = new ArrayList<>();

    @OneToOne(mappedBy = "user" , cascade = CascadeType.ALL)
    private ConfirmationToken confirmationToken;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    private List<Address> addressList;
}
