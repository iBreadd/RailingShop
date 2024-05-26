package com.example.RailingShop.Entities;

import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Името на продукта не може да бъде празно!")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    @NotNull(message = "Цената не може да бъде нула!")
    @Min(value = 0,message = "Цената трябва да е по-голяма от 0.00")
    private BigDecimal price;

    @Column(name = "quantity")
    @NotNull(message = "Количеството не може да бъде нула!")
    @Min(value = 0, message = "Количеството трябва да е повече или равно на 1")
    private int quantity;

    @Column
    @NotNull
    private boolean available;
    @Column(name = "color")
    @NotNull(message = "Цвета не може да бъде празна!")
    @Enumerated(EnumType.STRING)
    private ProductColor color;
    @Column(name = "material")
    @NotNull(message = "Материала не може да бъде празна!")
    @Enumerated(EnumType.STRING)
    private ProductMaterial material;
    @Column
    private String imageUrl;
    @Column(name = "manufacturer")
    @NotBlank(message = "Производителя не може да бъде празна!")
    private String manufacturer;

    public Product(){

    }

    public Product(Long id, String name, String description, BigDecimal price, int quantity, boolean available, ProductColor color, ProductMaterial material, String imageUrl, String manufacturer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.available = available;
        this.color = color;
        this.material = material;
        this.imageUrl = imageUrl;
        this.manufacturer = manufacturer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ProductColor getColor() {
        return color;
    }

    public void setColor(ProductColor color) {
        this.color = color;
    }

    public ProductMaterial getMaterial() {
        return material;
    }

    public void setMaterial(ProductMaterial material) {
        this.material = material;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
