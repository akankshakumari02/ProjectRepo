package com.example.project.ecomm.ECommerce.entities.User;

import com.example.project.ecomm.ECommerce.entities.Product.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class Seller extends User
{
    private double gst;
    private Long companyContact;
    private String companyName;

    @OneToOne(mappedBy = "seller",cascade = CascadeType.ALL)
    private Address address;

    @OneToMany(mappedBy = "seller")
    private List<Product> productList;

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public Long getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(Long companyContact) {
        this.companyContact = companyContact;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
