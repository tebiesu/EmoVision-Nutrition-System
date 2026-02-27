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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerCoverageE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCoverProfileMealDashboardAndValidationError() throws Exception {
        String token = loginAndGetToken();

        mockMvc.perform(put("/api/v1/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"alice\",\"age\":26,\"goalType\":\"lose_fat\",\"dietTaboo\":[\"peanut\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName").value("alice"));

        mockMvc.perform(get("/api/v1/profile")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.goalType").value("lose_fat"));

        mockMvc.perform(put("/api/v1/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userName\":\"\",\"age\":26,\"goalType\":\"lose_fat\",\"dietTaboo\":[]}"))
                .andExpect(status().isBadRequest());

        MvcResult mealResp = mockMvc.perform(post("/api/v1/meals")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mealType\":\"lunch\",\"totalCalories\":680,\"proteinG\":30,\"fatG\":18,\"carbG\":75}"))
                .andExpect(status().isOk())
                .andReturn();
        long mealId = objectMapper.readTree(mealResp.getResponse().getContentAsString()).path("data").path("id").asLong();

        MvcResult emotionResp = mockMvc.perform(post("/api/v1/emotions/analyze")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mealId\":" + mealId + ",\"text\":\"stable state\",\"sceneTag\":\"daily\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sourceProvider").value("newapi"))
                .andReturn();
        JsonNode e = objectMapper.readTree(emotionResp.getResponse().getContentAsString()).path("data");
        long emotionId = e.path("emotionId").asLong();

        mockMvc.perform(post("/api/v1/recommendations/generate")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mealId\":" + mealId + ",\"emotionId\":" + emotionId + "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.explainText").exists());

        mockMvc.perform(get("/api/v1/dashboard/overview")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalCalories").exists());

    }

    private String loginAndGetToken() throws Exception {
        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"demo\",\"password\":\"demo\"}"))
                .andExpect(status().isOk())
                .andReturn();
        return objectMapper.readTree(loginResult.getResponse().getContentAsString()).path("data").path("token").asText();
    }
}
