package com.zderival.FoodMaster.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    // This test is to prove that when a new account is made with a duplicate user in the database
    // the expected result is 409 Conflict error.
    // The first perform inputs the user the into the user into the database.
    // The second perform runs the same endpoint again,
    // and checks for the expected error (409 Conflict)
    // Transactional allows for the test user to rollback from the database after testing is finished
    // pass or fail.
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
