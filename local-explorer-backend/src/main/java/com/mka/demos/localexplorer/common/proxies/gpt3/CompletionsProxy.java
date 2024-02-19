package com.mka.demos.localexplorer.common.proxies.gpt3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
public class CompletionsProxy {

    @Value("${openai.api.completion.endpoint}")
    private String endpoint;

    @Value("${openai.api.apikey}")
    private String apiKey;

    private RestTemplate restTemplate;

    @Autowired
    public CompletionsProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<CompletionsOutput> callGpt3Completions(String prompt) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(this.apiKey);

            HttpEntity<CompletionsInput> requestEntity = new HttpEntity<>(
                    CompletionsInput.builder().prompt(prompt).model("gpt-3.5-turbo-instruct").maxTokens(1024).topP(0).build(),
                    headers);

            ResponseEntity<CompletionsOutput> response = restTemplate.postForEntity(this.endpoint, requestEntity, CompletionsOutput.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return Optional.of(response.getBody());
            }
        } catch (Exception e) {
            log.error("something went wrong while calling OpenAI Completions", e);
        }
        return Optional.empty();
    }
}
