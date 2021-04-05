package com.example.project.ecomm.ECommerce.entities.Product;

import com.example.project.ecomm.ECommerce.entities.Order.Cart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int quantityAvailable;
    private int price;

   /* private String primaryImageName;*/

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(mappedBy = "productVariation", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();
}
