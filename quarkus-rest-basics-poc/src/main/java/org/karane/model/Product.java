package org.karane.model;

public class Product {

    public Long id;
    public String name;
    public double price;
    public String category;

    public Product() {
    }

    public Product(Long id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}
