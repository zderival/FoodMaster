package com.zderival.FoodMaster.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void register_duplicateUsername_throws409Error()throws Exception{
        UUID random_user = UUID.randomUUID();

        String json = """
    {"username": "%s", "password": "Godisgood2", "email": "%s@email.com"}
    """.formatted(random_user,random_user);
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andExpect(status().isOk());
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)).andExpect(status().isConflict());
    }
}
