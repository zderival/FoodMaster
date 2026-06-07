package com.zderival.FoodMaster.nutrition;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NutritionProfileService {
    private final NutritionProfileRepository nutritionProfileRepository;
    public NutritionProfileResponse createProfile(UUID userId, NutritionProfileRequest request){
        if (nutritionProfileRepository.existsNutritionProfileByUserId(userId)){
            throw new ProfileExistsException("You already have a Nutrition Profile.");}

        NutritionProfile nutritionProfile = new NutritionProfile();
        nutritionProfile.setUserId(userId);
        nutritionProfile.setDiet(request.getDiet());
        nutritionProfile.setAllergies(request.getAllergies());
        nutritionProfile.setGoal(request.getGoal());
        nutritionProfile.setPreferences(request.getPreferences());
        nutritionProfileRepository.save(nutritionProfile);
        return new NutritionProfileResponse(nutritionProfile.getDiet(), nutritionProfile.getGoal(), nutritionProfile.getAllergies(),nutritionProfile.getPreferences());
    }

    public void updateProfile(UUID userId, NutritionProfileRequest request){
        NutritionProfile nutritionProfile = nutritionProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile does not exist"));

        Optional.ofNullable(request.getDiet()).ifPresent(nutritionProfile::setDiet);
        Optional.ofNullable(request.getAllergies()).ifPresent(nutritionProfile::setAllergies);
        Optional.ofNullable(request.getGoal()).ifPresent(nutritionProfile::setGoal);
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
}
