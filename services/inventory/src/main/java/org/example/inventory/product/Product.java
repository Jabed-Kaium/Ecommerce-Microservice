package org.example.inventory.product;

import jakarta.persistence.*;
import lombok.*;
import org.example.inventory.category.Category;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Long availableQuantity;
    private double price;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private String createdAt;
    private String updatedAt;
}
