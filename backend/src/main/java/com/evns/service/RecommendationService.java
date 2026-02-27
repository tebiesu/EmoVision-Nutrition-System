package com.evns.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {
    public record RecommendationResult(List<String> adviceList, List<String> riskFlags, String explainText) {
    }

    public RecommendationResult generate(String emotion, String goalType, double carbG, double sugarScore) {
        List<String> advice = new ArrayList<>();
        List<String> risk = new ArrayList<>();
        List<String> rules = new ArrayList<>();

        if ("stress".equals(emotion) || sugarScore >= 0.7) {
            advice.add("减少精制糖摄入");
            advice.add("用高蛋白零食替代甜食");
            risk.add("情绪性进食风险");
            rules.add("R001");
        }
        if ("lose_fat".equals(goalType) && carbG > 80) {
            advice.add("晚餐碳水建议下调20%");
            risk.add("晚餐碳水超标");
            rules.add("R002");
        }
        if ("tired".equals(emotion)) {
            advice.add("注意补水与电解质");
            advice.add("补充优质蛋白");
            risk.add("疲劳状态影响饮食质量");
            rules.add("R003");
        }

        if (advice.isEmpty()) {
            advice.add("当前饮食结构平稳，保持规律作息");
            rules.add("R000");
        }

        List<String> finalAdvice = advice.stream().distinct().limit(3).toList();
        List<String> finalRisk = risk.stream().distinct().limit(2).toList();
        String explain = "命中规则: " + String.join(", ", rules);
        return new RecommendationResult(finalAdvice, finalRisk, explain);
    }
}
