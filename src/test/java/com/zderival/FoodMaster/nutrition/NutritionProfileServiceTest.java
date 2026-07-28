package com.zderival.FoodMaster.nutrition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// Mockito - a package used for mock testing. Mocking is when you fake a dependency so
// that you don't have to use actual API's or database when testing.
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Always use annotation below when testing with Mockito
@ExtendWith(MockitoExtension.class)
public class NutritionProfileServiceTest {
/* Before testing, you need all the dependencies required to test methods inside the class.
* Currently from the NutritonProfileService class, you need the repository */
    @InjectMocks
    // @InjectMocks - Creates the real instance and injects mocks into it
    private NutritionProfileService nutritionProfileService;
    @Mock
    // @Mock - creates the fake version of the dependency.
    private NutritionProfileRepository nutritionProfileRepository;

    @Test
    // This test is to test if the functionality of the checkGoal helper method, is doing its job
    // and generating the right result.
    // @Test -
    public void createProfile_withInvalidGoal_throwsInvalidGoalException(){
        NutritionProfileRequest request = new NutritionProfileRequest();
        request.setGoal("get fat");
        assertDoesNotThrow(InvalidGoalException.class,()-> {
            nutritionProfileService.createProfile(UUID.randomUUID(),request);
        });
    }
}
