package com.zderival.FoodMaster.nutrition;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "nutrition_profile")
@Getter
@Setter
@RequiredArgsConstructor
public class NutritionProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID userId;
    private String diet;
    private String goal;
    @ElementCollection
    private List<String> allergies;
    @ElementCollection
    private List<String> preferences;
}
