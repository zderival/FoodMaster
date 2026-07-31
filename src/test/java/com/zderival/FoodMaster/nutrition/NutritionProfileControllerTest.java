package com.zderival.FoodMaster.nutrition;

import com.zderival.FoodMaster.config.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NutritionProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void createProfile_withInValidGoal_throws400Error() throws Exception{
        String token = jwtUtil.generateToken("zderival");
        mockMvc.perform(post("/profile/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"goal\": \"get lean\"}").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }}
