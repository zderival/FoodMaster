package com.zderival.FoodMaster.mealplanner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "week_plan")
@Getter @Setter
@NoArgsConstructor
public class WeekPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "week_plan_id")
    private List<DayPlan> days;
    private int weekNumber;
}
