package com.recommender.clothing_item_service.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Id;

import java.util.Set;


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

    @ElementCollection(targetClass = EStyle.class)
    @Enumerated(EnumType.STRING)
    private Set<EStyle> style;

    @Enumerated(EnumType.STRING)
    private ESize size;

    @NotNull(message = "Price is needed")
    @Positive(message = "Price must be higher than 0")
    private Double price;

    private String color;

    @Min(value = 0, message = "Stock must be zero or more")
    private int stock;
}
