package com.example.project.ecomm.ECommerce.entities.Product;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonFilter("Filter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMetadataField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String name;

    @OneToMany(mappedBy = "categoryMetadataField", cascade = CascadeType.ALL)
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues = new ArrayList<>();

    public CategoryMetadataField(String name)
    {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryMetadataField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", categoryMetadataFieldValues=" + categoryMetadataFieldValues +
                '}';
    }
}
