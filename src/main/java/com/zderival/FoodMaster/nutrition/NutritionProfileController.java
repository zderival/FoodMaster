package com.zderival.FoodMaster.nutrition;

import com.zderival.FoodMaster.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class NutritionProfileController {
    private final NutritionProfileService nutritionProfileService;
    private final UserService userService;

    @PostMapping("/create")
    public NutritionProfileResponse createProfile(@RequestBody NutritionProfileRequest request){
        return nutritionProfileService.createProfile(userService.extractUser().getId(),request);
    }

    @PutMapping("/update")
    public void updateProfile(@RequestBody NutritionProfileRequest request){
        nutritionProfileService.updateProfile(userService.extractUser().getId(),request);
    }

    @GetMapping("/get")
    public NutritionProfileResponse getProfile(){
        return nutritionProfileService.getProfile(userService.extractUser().getId());
    }
    @DeleteMapping("/delete")
    public void deleteProfile(){
        nutritionProfileService.deleteProfile(userService.extractUser().getId());
    }
}
