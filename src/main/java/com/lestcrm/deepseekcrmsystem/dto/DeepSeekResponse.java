package com.lestcrm.deepseekcrmsystem.dto;

import lombok.Data;
import java.util.List;

@Data
public class DeepSeekResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private Message message;
    }

    @Data
    public static class Message {
        private String content;
        // 推理模型会先把思考过程放在该字段，正文在 content 中；
        // 当 content 因 token 耗尽为空时，用它兜底提取 JSON
        private String reasoningContent;
    }
}