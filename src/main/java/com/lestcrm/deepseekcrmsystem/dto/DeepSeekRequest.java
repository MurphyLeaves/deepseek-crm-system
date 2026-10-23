package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class DeepSeekRequest {
    private String model = "deepseek-v4-flash";
    private List<Map<String, String>> messages;
    private Boolean stream = false;
    private Double temperature = 0.3;
    // 注意：当前模型为推理模型，会先输出思考过程再输出正文。
    // 上限过小（如 500）时思考过程会把额度耗尽，正文为空，导致解析失败。
    private Integer max_tokens = 2000;
}