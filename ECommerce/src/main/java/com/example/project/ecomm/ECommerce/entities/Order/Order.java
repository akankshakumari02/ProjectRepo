package com.example.project.ecomm.ECommerce.entities.Order;

import com.example.project.ecomm.ECommerce.entities.User.Customer;
import lombok.*;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int amountPaid;
    private Date dateCreated;
    private String paymentMethod;

    /*private String customerAddressCity;
    private String customerAddressState;
    private String customerAddressCountry;
    private String customerAddressAddressLine;
    private int customerAddressZipCode;
    private String customerAddressLabel;*/

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderProduct> orderProductList = new ArrayList<>();

}
