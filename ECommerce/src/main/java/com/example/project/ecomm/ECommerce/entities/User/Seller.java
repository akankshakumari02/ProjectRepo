package com.example.project.ecomm.ECommerce.entities.User;

import com.example.project.ecomm.ECommerce.entities.Product.Product;
import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@JsonFilter("Filter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
public class Seller extends User
{
    private String gst;

    @ElementCollection
    @CollectionTable(name = "seller_contacts", joinColumns = @JoinColumn(name = "seller_id", referencedColumnName = "id"))
    private List<Long> contactList;

    private String companyName;

    @OneToMany(mappedBy = "seller")
    private List<Product> productList;
}
