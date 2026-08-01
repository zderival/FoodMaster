package com.zderival.FoodMaster.nutrition;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class NutritionProfileControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Test
    @Transactional
    // This test is to prove that when creating a new nutrition with a invalid goal, the correct
    // status error to be thrown is 400/BadRequest.
    public void createProfile_withInValidGoal_throws400Error() throws Exception{
        UUID user = UUID.randomUUID();
        String json = """
    {"username": "%s", "password": "Godisgood2", "email": "%s@email.com"}
    """.formatted(user,user);
       MvcResult result =  mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andReturn();
       String response = result.getResponse().getContentAsString();
       ObjectMapper mapper = new ObjectMapper();
        String token = mapper.readTree(response).get("token").asText();

        mockMvc.perform(post("/profile/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"goal\": \"get lean\"}").header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest());
    }}
