package com.recommender.clothing_item_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClothingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is needed")
    private String name;

    @Enumerated(EnumType.STRING)
    private EStyle style;

    @Enumerated(EnumType.STRING)
    private ESize size;

    @NotNull(message = "Price is needed")
    @Positive(message = "Price must be higher than 0")
    private Double price;

    private String colour;

    private int stock;
}
