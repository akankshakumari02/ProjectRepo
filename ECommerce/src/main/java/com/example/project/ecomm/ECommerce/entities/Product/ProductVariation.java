package com.example.project.ecomm.ECommerce.entities.Product;

import com.example.project.ecomm.ECommerce.entities.Order.Cart;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("Filter")
@Data
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ProductVariation
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private int quantityAvailable;
    private int price;
    private String image;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private String metadata;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @OneToMany(mappedBy = "productVariation", cascade = CascadeType.ALL)
    private List<Cart> carts = new ArrayList<>();


    @Override
    public String toString() {
        return "ProductVariation{" +
                "id=" + id +
                ", quantityAvailable=" + quantityAvailable +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", metadata='" + metadata + '\'' +
                ", product=" + product + '\'' +
                '}';
    }
}
