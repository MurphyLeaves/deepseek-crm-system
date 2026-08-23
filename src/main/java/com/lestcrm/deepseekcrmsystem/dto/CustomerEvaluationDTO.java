package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;

@Data
public class CustomerEvaluationDTO {
    private String priorityLevel;   // A/B/C
    private Integer priorityScore;  // 1-10
    private String reason;          // 评估理由
}