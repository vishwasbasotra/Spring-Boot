package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;

    @NotBlank
    @Size(min = 3, max = 500, message = "Name should be between 2 to 2- characters")
    private String productName;

    private String image;

    @NotBlank
    @Size(min = 10, max = 200, message = "Enter Description")
    private String description;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NonNull
    @Positive(message = "Price must be positive")
    private Double price;

    private Double discount;
    private Double specialPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<CartItem> cartItems = new ArrayList<>();
}
