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
    private Integer max_tokens = 500;
}