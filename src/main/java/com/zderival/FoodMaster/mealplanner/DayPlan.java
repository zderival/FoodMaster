package com.zderival.FoodMaster.mealplanner;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "day_plan")
@Getter @Setter
@NoArgsConstructor
public class DayPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String day;
    private String breakfast;
    private String lunch;
    private String dinner;
}
