package com.zderival.FoodMaster.history;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recipe_history", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId","spoonacularId"})})
@Getter @Setter
@RequiredArgsConstructor
public class RecipeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private UUID userId;
    private int spoonacularId;
    private LocalDateTime timestamp;
}
