package com.zderival.FoodMaster.saved;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "saved_recipes")
@Getter @Setter
@RequiredArgsConstructor
public class SavedRecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID userId;
    private int spoonacularId;
    private double fiber;
    private double fat;
    private double calories;
    private double protein;
}
