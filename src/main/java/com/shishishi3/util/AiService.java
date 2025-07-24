package com.shishishi3.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Properties;

public class AiService {

    private static final String API_ENDPOINT = "https://api.deepseek.com/chat/completions";
    private static String apiKey;

    static {
        Properties props = new Properties();
        try (InputStream input = AiService.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                apiKey = props.getProperty("deepseek.api.key");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRiskAssessment(String contentToAssess) {
        if (apiKey == null || apiKey.isBlank()) {
            return "{\"error\":\"API Key未配置或加载失败，请检查db.properties文件。\"}";
        }

        String prompt = "你是一名顶级的实验室安全专家(HSE)，请为我提供的实验方案进行全面的风险评估。" +
                "你的回答必须且只能是一个完整的JSON对象，严格遵循以下结构，不要有任何额外的解释和对话：" +
                "{" +
                "  \"risk_assessment\": [" +
                "    {\"risk_item\": \"识别出的具体风险点\", \"risk_level\": \"评估的风险等级(高/中/低)\", \"protective_measure\": \"针对该风险点的具体防护措施建议\"}" +
                "  ]," +
                "  \"emergency_plan\": {" +
                "    \"procedure_guidance\": \"突发情况的通用应对流程指引。\", " +
                "    \"accident_record_analysis\": \"发生安全事故后，应如何记录与分析的指导建议。\"" +
                "  }" +
                "}";

        String jsonBody = String.format(
                "{\"model\": \"deepseek-chat\", \"messages\": [{\"role\": \"system\", \"content\": \"%s\"}, {\"role\": \"user\", \"content\": \"%s\"}]}",
                escapeJson(prompt),
                escapeJson(contentToAssess)
        );

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(20))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .timeout(Duration.ofSeconds(60))
                .build();

        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            String responseBody = new String(response.body(), StandardCharsets.UTF_8);

            if (response.statusCode() != 200) {
                return "{\"error\":\"AI服务返回了错误状态码: " + response.statusCode() + ", 响应: " + escapeJson(responseBody) + "\"}";
            }

            if (responseBody != null && !responseBody.isBlank()) {
                JsonObject responseObject = JsonParser.parseString(responseBody).getAsJsonObject();
                String content = responseObject.getAsJsonArray("choices")
                        .get(0).getAsJsonObject()
                        .get("message").getAsJsonObject()
                        .get("content").getAsString();

                // ==================== 最终核心修正：清理AI返回的Markdown格式 ====================
                String cleanJson = content.trim(); // 去除首尾空白
                if (cleanJson.startsWith("```json")) {
                    cleanJson = cleanJson.substring(7); // 去除 "```json"
                }
                if (cleanJson.endsWith("```")) {
                    cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
                }

                // 再次去除可能存在的首尾空白
                cleanJson = cleanJson.trim();

                return cleanJson; // 返回清理后的纯净JSON
                // =================================================================================

            }
            return "{\"error\":\"AI模型返回了空的响应\"}";
        } catch (java.net.http.HttpTimeoutException e) {
            e.printStackTrace();
            return "{\"error\":\"调用AI服务超时，请稍后再试。\"}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"调用AI服务或解析响应时发生错误: " + e.getMessage() + "\"}";
        }
    }

    private static String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\t");
    }
}