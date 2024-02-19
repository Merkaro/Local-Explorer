package com.mka.demos.localexplorer.common.proxies.gpt3;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompletionsInput {

    private String model;

    private String prompt;

    @JsonProperty("max_tokens")
    private Integer maxTokens;

    @JsonProperty("top_p")
    private Integer topP;

}
