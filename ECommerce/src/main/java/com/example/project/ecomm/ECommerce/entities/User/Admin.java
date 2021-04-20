package com.example.project.ecomm.ECommerce.entities.User;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User{
}
