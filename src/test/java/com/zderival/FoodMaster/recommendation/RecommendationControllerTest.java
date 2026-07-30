package com.zderival.FoodMaster.recommendation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Boots the actual full Spring application context meaning real beans, real config,
// real security filter chain. This is what makes it an integration test
// instead of a unit test (no mocking involved here).
@SpringBootTest
// Tells Spring to also create a MockMvc bean, wired against that real context above,
// so we can simulate HTTP requests without starting an actual server on a real port.
@AutoConfigureMockMvc
public class RecommendationControllerTest {
    // MockMvc is a Spring test utility. It is not like a mock in the Mockito, everything
    // behind it is from your actual program.
    // It lets me simulate a full HTTP request/response cycle
    // in-memory, hitting the actual filter chain, and real
    // controller — all without actually starting a server on a real network port.
    // Spring creates this bean automatically because of @AutoConfigureMockMvc above,
    // and just injects it here to use in my test methods.

    // MockMvc is injected here as a real Spring-managed bean.
    // This will be used to fire fake requests at our real app.
    @Autowired
    private MockMvc mockmvc;

    // The goal of the test is to prove that
    // Spring Security's filter chain blocks this request
    // before it  reaches RecommendationController — no JWT means the
    // "bouncer" (security filter) rejects it at the door. The controller
    // and service code should never execute in this test.
    @Test
    public void getRecommendation_withoutJwt_throws401() throws Exception{
        // Simulate a GET request to /recommendations with NO Authorization header.
        // Send it through the real filter chain + real app (in-memory, no real network).
        // Expect the response status to be 401 Unauthorized which is proof the security
        // config is correctly guarding the endpoint.
        mockmvc.perform(get("/recommendations")).andExpect(status().isUnauthorized());
    }
}