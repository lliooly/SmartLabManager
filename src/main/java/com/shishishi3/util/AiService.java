package com.shishishi3.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class AiService {

    private static final String API_ENDPOINT = "https://api.deepseek.com/chat/completions";
    private static String apiKey;

    // 从配置文件加载API Key
    static {
        Properties props = new Properties();
        try (InputStream input = DbUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            props.load(input);
            apiKey = props.getProperty("deepseek.api.key");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送实验方案给DeepSeek模型进行风险评估
     * @param experimentDetails 用户输入的实验详情字符串
     * @return AI模型返回的JSON格式的评估报告字符串
     */
    public static String getRiskAssessment(String experimentDetails) {
        // 1. 设计高质量的提示词 (Prompt)
        String prompt = "你是一名顶级的实验室安全专家（HSE），你的任务是为我提供的实验方案进行全面的风险评估。" +
                "请严格按照以下JSON格式返回你的分析结果，不要有任何额外的解释和对话：" +
                "{\"risk_points\":[{\"step\":\"描述具体是哪个步骤\",\"risk\":\"识别出的具体风险\",\"level\":\"评估的风险等级 (高/中/低)\"}]," +
                "\"suggestions\":[\"第一条具体改进建议...\",\"第二条具体改进建议...\"]," +
                "\"emergency_plan\":{\"chemical_spill\":\"针对化学品泄漏的应急预案...\",\"fire\":\"针对火灾的应急预案...\",\"personal_injury\":\"针对人员受伤的应急预案...\"}}";

        // 2. 构建符合DeepSeek API要求的JSON请求体
        String jsonBody = "{" +
                "\"model\": \"deepseek-chat\"," +
                "\"messages\": [" +
                "  {\"role\": \"system\", \"content\": \"" + prompt + "\"}," +
                "  {\"role\": \"user\", \"content\": \"" + escapeJson(experimentDetails) + "\"}" +
                "]" +
                "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_ENDPOINT))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        try {
            // 3. 发送请求并获取响应
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 4. 解析响应，提取出AI生成的内容
            // 注意：这里是简化的解析，实际项目中建议使用Gson或Jackson库
            String responseBody = response.body();
            if (responseBody != null && responseBody.contains("\"content\":\"")) {
                int start = responseBody.indexOf("\"content\":\"") + 11;
                int end = responseBody.lastIndexOf("\"},\"finish_reason\"");
                if (start < end) {
                    return unescapeJson(responseBody.substring(start, end));
                }
            }
            return "{\"error\":\"无法解析AI模型的响应\"}";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "{\"error\":\"调用AI服务时发生网络或中断错误\"}";
        }
    }

    // 辅助方法，用于转义JSON字符串中的特殊字符
    private static String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // 辅助方法，用于反转义AI响应中的字符
    private static String unescapeJson(String text) {
        return text.replace("\\\"", "\"")
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}