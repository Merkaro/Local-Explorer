package com.mka.demos.localexplorer.common.proxies.gpt3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CompletionsOutput {

    private List<Choice> choices;

    @Data
    public static class Choice {

        private String text;

        @JsonProperty("finish_reason")
        private String finishReason;
    }
}
