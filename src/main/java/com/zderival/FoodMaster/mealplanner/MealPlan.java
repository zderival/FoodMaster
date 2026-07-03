package com.zderival.FoodMaster.mealplanner;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "meal_planner")
@Getter @Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @JsonIgnore
    private UUID userId;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "meal_plan_id")
    private List<WeekPlan> weeks;
    private int numberOfWeeks;
}
