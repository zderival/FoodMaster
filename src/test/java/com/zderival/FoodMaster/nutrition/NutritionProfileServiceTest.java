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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        assertThrows(InvalidGoalException.class,()-> {
            nutritionProfileService.createProfile(UUID.randomUUID(),request);
        });
    }
    @Test
    public void createProfile_withValiddGoal(){
        NutritionProfileRequest request = new NutritionProfileRequest();
        request.setGoal("lean");
        // when(any()).thenReturn - this line is doing 3 things at once. when() is a function that works as an conditional
        // meaning "When this function happens". any() indicates that any matching value for the method
        // can go in that methods parameters. thenReturn() is function that forces the return value to
        // whatever the funtion in context of the test should return.
        when(nutritionProfileRepository.existsNutritionProfileByUserId(any())).thenReturn(false);
        // assertDoesNotThrow is when you are making sure that the exception is thrown here, and your
        // test goes through successfully the method business logic
        assertDoesNotThrow(() -> {
            nutritionProfileService.createProfile(UUID.randomUUID(),request);
        });

    }

}
