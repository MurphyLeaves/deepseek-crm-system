package com.lestcrm.deepseekcrmsystem.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lestcrm.deepseekcrmsystem.dto.CustomerEvaluationDTO;
import com.lestcrm.deepseekcrmsystem.dto.DeepSeekRequest;
import com.lestcrm.deepseekcrmsystem.dto.DeepSeekResponse;
import com.lestcrm.deepseekcrmsystem.entity.Customer;
import com.lestcrm.deepseekcrmsystem.entity.SysConfig;
import com.lestcrm.deepseekcrmsystem.mapper.CustomerMapper;
import com.lestcrm.deepseekcrmsystem.mapper.SysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DeepSeekService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Value("${deepseek.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 核心方法：评估客户（含缓存判断 + 结果持久化）
     *
     * @param customerId   客户ID
     * @param forceRefresh true=强制重新调用API（重新生成），false=有缓存则使用缓存
     */
    public CustomerEvaluationDTO evaluateCustomer(Long customerId, boolean forceRefresh) {
        // 1. 查询客户
        Customer customer = customerMapper.selectById(customerId);
        if (customer == null) {
            throw new RuntimeException("客户不存在");
        }

        // 2. 【核心改造点1】如果非强制刷新，且数据库已有评估结果，直接返回（省成本）
        if (!forceRefresh && StringUtils.hasText(customer.getAiPriorityLevel())) {
            return buildDTOFromCustomer(customer);
        }

        // 3. 需要调用AI：从数据库读取提示词模板
        String promptTemplate = getPromptFromDB();

        // 4. 拼接提示词（将客户信息填充进去）
        String finalPrompt = buildPrompt(promptTemplate, customer);

        // 5. 调用 DeepSeek API
        String aiResponse = callDeepSeekAPI(finalPrompt);

        // 6. 解析 AI 返回的 JSON
        CustomerEvaluationDTO dto = parseAIResponse(aiResponse);

        // 7. 【核心改造点2】将评估结果持久化到数据库
        customer.setAiPriorityLevel(dto.getPriorityLevel());
        customer.setAiPriorityScore(dto.getPriorityScore());
        customer.setAiReason(dto.getReason());
        customer.setAiEvalTime(LocalDateTime.now());
        customerMapper.updateById(customer);

        return dto;
    }

    // ========== 私有辅助方法 ==========

    /**
     * 从已保存的客户信息构建DTO（用于缓存命中）
     */
    private CustomerEvaluationDTO buildDTOFromCustomer(Customer customer) {
        CustomerEvaluationDTO dto = new CustomerEvaluationDTO();
        dto.setPriorityLevel(customer.getAiPriorityLevel());
        dto.setPriorityScore(customer.getAiPriorityScore());
        dto.setReason(customer.getAiReason());
        return dto;
    }

    /**
     * 从数据库获取提示词，若没有则使用默认硬编码兜底
     */
    private String getPromptFromDB() {
        SysConfig config = sysConfigMapper.selectByKey("ai_eval_prompt");
        if (config != null && StringUtils.hasText(config.getConfigValue())) {
            return config.getConfigValue();
        }
        // 兜底默认提示词（防止数据库表为空导致系统异常）
        return "你是一位资深的B2B销售管理专家。请根据客户信息评估跟进优先级。严格按JSON格式返回：{\"priorityLevel\":\"A/B/C\",\"priorityScore\":1-10,\"reason\":\"理由\"}。客户信息：{companyName}，行业：{industry}，规模：{employeeScale}，来源：{source}，备注：{remark}";
    }

    /**
     * 拼接提示词：将模板中的占位符（如{companyName}）替换为真实数据
     */
    private String buildPrompt(String template, Customer customer) {
        return template
                .replace("{companyName}", customer.getCompanyName() == null ? "未提供" : customer.getCompanyName())
                .replace("{industry}", customer.getIndustry() == null ? "未提供" : customer.getIndustry())
                .replace("{employeeScale}", customer.getEmployeeScale() == null ? "未提供" : customer.getEmployeeScale())
                .replace("{source}", customer.getSource() == null ? "未提供" : customer.getSource())
                .replace("{remark}", customer.getRemark() == null ? "无" : customer.getRemark());
    }

    /**
     * 调用 DeepSeek API
     */
    private String callDeepSeekAPI(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);

        DeepSeekRequest request = new DeepSeekRequest();
        request.setMessages(Collections.singletonList(userMessage));

        HttpEntity<DeepSeekRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("DeepSeek API调用失败: " + response.getStatusCode());
        }
    }

    /**
     * 解析 AI 返回的 JSON（增强兼容性：去除可能出现的 Markdown 代码块）
     */
    private CustomerEvaluationDTO parseAIResponse(String responseBody) {
        // 解析外层响应
        DeepSeekResponse deepSeekResponse = JSON.parseObject(responseBody, DeepSeekResponse.class);
        if (deepSeekResponse.getChoices() == null || deepSeekResponse.getChoices().isEmpty()) {
            throw new RuntimeException("DeepSeek返回结果为空");
        }

        // 获取AI生成的内容
        String content = deepSeekResponse.getChoices().get(0).getMessage().getContent();

        // 处理可能出现的 Markdown 代码块（如 ```json ... ```）
        String cleanContent = content
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();

        // 解析内层JSON
        JSONObject jsonObject = JSON.parseObject(cleanContent);

        CustomerEvaluationDTO result = new CustomerEvaluationDTO();
        result.setPriorityLevel(jsonObject.getString("priorityLevel"));
        result.setPriorityScore(jsonObject.getInteger("priorityScore"));
        result.setReason(jsonObject.getString("reason"));
        return result;
    }
}