package org.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;

import java.util.List;

@Entity
@Table(name="category")
public class Category {
//    Java commonly uses camelCase : private int instructorProfileId;
//    Databases commonly use snake_case : @Column(name = "instructor_profile_id")
//    So Hibernate maps : instructorProfileId = instructor_profile_id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;
    @Column(name = "description", length = 100)
    private String description;

    //Constructor --> Select all
    //id is actually not needed because we are generating it automatically.
    //public Category(int id, String name, String description) {
    public Category(String name, String description) {
        //this.id = id;
        this.name = name;
        this.description = description;
    }

    //Getter & Setter --> Select All
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();



    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Category() {
    }
}
