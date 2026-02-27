package com.evns.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoreFlowE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldBlockUnauthorizedRequest() throws Exception {
        mockMvc.perform(get("/api/v1/profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRunCoreBusinessFlow() throws Exception {
        String token = loginAndGetToken();

        MvcResult mealResult = mockMvc.perform(post("/api/v1/meals")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mealType":"dinner",
                                  "imageUrl":"https://img.test/a.jpg",
                                  "note":"test",
                                  "totalCalories":900,
                                  "proteinG":40,
                                  "fatG":20,
                                  "carbG":110
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();
        long mealId = objectMapper.readTree(mealResult.getResponse().getContentAsString()).path("data").path("id").asLong();

        MvcResult emotionResult = mockMvc.perform(post("/api/v1/emotions/analyze")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mealId": %d,
                                  "text":"今天压力大 fallback",
                                  "sceneTag":"workday"
                                }
                                """.formatted(mealId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sourceProvider").value("siliconflow"))
                .andReturn();

        long emotionId = objectMapper.readTree(emotionResult.getResponse().getContentAsString())
                .path("data").path("emotionId").asLong();

        mockMvc.perform(post("/api/v1/recommendations/generate")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mealId": %d,
                                  "emotionId": %d
                                }
                                """.formatted(mealId, emotionId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.adviceList").isArray());

        mockMvc.perform(get("/api/v1/dashboard/overview").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.mealCount").value(1));

        mockMvc.perform(get("/api/v1/provider-logs").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    private String loginAndGetToken() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "username":"demo",
                                  "password":"demo"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andReturn();

        JsonNode root = objectMapper.readTree(loginResult.getResponse().getContentAsString());
        return root.path("data").path("token").asText();
    }
}
