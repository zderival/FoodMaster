package com.zderival.FoodMaster.nutrition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NutritionProfileService {
    private final NutritionProfileRepository nutritionProfileRepository;
    private static final Set<String> validGoals = Set.of("lose weight","lean","get stronger","build muscle");

    public NutritionProfileResponse createProfile(UUID userId, NutritionProfileRequest request){
        if (nutritionProfileRepository.existsNutritionProfileByUserId(userId)){
            throw new ProfileExistsException("You already have a Nutrition Profile.");}

        NutritionProfile nutritionProfile = new NutritionProfile();
        nutritionProfile.setUserId(userId);
        nutritionProfile.setDiet(request.getDiet());
        nutritionProfile.setAllergies(request.getAllergies());
        if (checkGoal(request.getGoal())) {
            nutritionProfile.setGoal(request.getGoal());
        }else{
            throw new InvalidGoalException("This is an invalid goal. " +
                    "Please enter a goal from within the list");
        }
        nutritionProfile.setPreferences(request.getPreferences());
        nutritionProfileRepository.save(nutritionProfile);
        return new NutritionProfileResponse(nutritionProfile.getDiet(), nutritionProfile.getGoal(), nutritionProfile.getAllergies(),nutritionProfile.getPreferences());
    }

    public void updateProfile(UUID userId, NutritionProfileRequest request){
        NutritionProfile nutritionProfile = nutritionProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));

        Optional.ofNullable(request.getDiet()).ifPresent(nutritionProfile::setDiet);
        Optional.ofNullable(request.getAllergies()).ifPresent(nutritionProfile::setAllergies);
        if (checkGoal(request.getGoal())) {
            Optional.ofNullable(request.getGoal()).ifPresent(nutritionProfile::setGoal);
        }else {throw new InvalidGoalException("This is an invalid goal. " +
                "Please enter a goal from within the list\"");}
        Optional.ofNullable(request.getPreferences()).ifPresent(nutritionProfile::setPreferences);

        nutritionProfileRepository.save(nutritionProfile);
    }

    public NutritionProfileResponse getProfile(UUID userId){
        NutritionProfile profile = nutritionProfileRepository.findByUserId(userId)
                .orElseThrow(()  -> new ProfileNotFoundException("Profile does not exist."));
        return mapToResponse(profile);
    }

    public void deleteProfile(UUID userId){
        nutritionProfileRepository.deleteByUserId(userId);
    }

    private NutritionProfileResponse mapToResponse(NutritionProfile profile){
        return new NutritionProfileResponse(profile.getDiet(), profile.getGoal(), profile.getAllergies(),profile.getPreferences());
    }

    public NutritionProfile getProfileOrNull(UUID userId) {
        return nutritionProfileRepository.findByUserId(userId).orElse(null);
    }

    private boolean checkGoal(String goal){
        if (goal == null){return true;}
        return validGoals.contains(goal.toLowerCase());
    }
}
