package com.mka.demos.localexplorer.resources.attractions.addresses.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mka.demos.localexplorer.resources.attractions.addresses.dto.AttractionsInputDto;
import com.mka.demos.localexplorer.resources.attractions.addresses.dto.AttractionsOutputDto;
import com.mka.demos.localexplorer.common.proxies.gpt3.CompletionsOutput;
import com.mka.demos.localexplorer.common.proxies.gpt3.CompletionsProxy;
import com.mka.demos.localexplorer.common.proxies.openweathermap.OpenWeatherMapProxy;
import com.mka.demos.localexplorer.common.proxies.openweathermap.DailyWeatherForcastOut;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.List;


@Service
@Data
@Slf4j
public class AttractionsService {

    private OpenWeatherMapProxy openWeatherMapProxy;

    private CompletionsProxy completionsProxy;

    private static final String LOCATION_PROMPT = "I am in {0}. My GPS coordinates are {1}, {2}";
    private static final String INSTRUCTION_PROMPT = " Suggest 4 activities (outdoor, indoor, cafés, restaurants) that i can do nearby";
    private static final String WEATHER_PROMPT = " taking into account todays weather. Forcast is : {0}}";
    private static final String TIME_PROMPT = ". The places must not be closed for the day or out of service}";
    private static final String STRUCTURE_PROMPT = ". Structure your answer as the following JSON object [{\"name\": \"\", \"description\": \"\", \"address\": \"\"}].";

    @Autowired
    public AttractionsService(OpenWeatherMapProxy openWeatherMapProxy, CompletionsProxy completionsProxy) {
        this.openWeatherMapProxy = openWeatherMapProxy;
        this.completionsProxy = completionsProxy;
    }

    public Optional<AttractionsOutputDto> getAttractionsToExplore(AttractionsInputDto location) {
        String weatherDescription = null;
        Optional<DailyWeatherForcastOut> todaysWeather = openWeatherMapProxy
                .getTodaysWeatherForcast(location.getLatitude(), location.getLongitude());
        if(todaysWeather.isPresent()) {
            weatherDescription = getWeatherDescription(todaysWeather.get());
        }
        String prompt = generateGpt3Prompt(location, weatherDescription);
        Optional<CompletionsOutput> attractionSuggetsionsString = completionsProxy.callGpt3Completions(prompt);

        if(attractionSuggetsionsString.isPresent()) {
            CompletionsOutput gpt3Response = attractionSuggetsionsString.get();
            if(gpt3Response.getChoices() != null && gpt3Response.getChoices().get(0) != null
                    && StringUtils.hasText(gpt3Response.getChoices().get(0).getText())) {
                return mapTextToAttractionsOutDto(gpt3Response.getChoices().get(0).getText());
            }
        }
        return Optional.empty();
    }

    private String getWeatherDescription(DailyWeatherForcastOut weatherForcast) {
        if(!CollectionUtils.isEmpty(weatherForcast.getList())
                && weatherForcast.getList().get(0) != null
                && !CollectionUtils.isEmpty(weatherForcast.getList().get(0).getWeather())
                && weatherForcast.getList().get(0).getWeather().get(0) != null
        ) {
            return weatherForcast.getList().get(0).getWeather().get(0).getDescription();
        }
        return null;
    }

    private String getFormattedLocationPrompt(AttractionsInputDto location) {
        return MessageFormat
                .format(LOCATION_PROMPT,
                        location.getCity(),
                        location.getLatitude(),
                        location.getLongitude());
    }

    private String getFormattedWeatherPrompt(String weatherDescription){
        return MessageFormat.format(WEATHER_PROMPT, weatherDescription);
    }

    private String generateGpt3Prompt(AttractionsInputDto location, String weatherDescription) {
        StringBuilder prompt = new StringBuilder()
                .append(getFormattedLocationPrompt(location))
                .append(INSTRUCTION_PROMPT);
        if(weatherDescription != null) {
            prompt.append(getFormattedWeatherPrompt(weatherDescription));
        }
        prompt.append(STRUCTURE_PROMPT);
        return prompt.toString();
    }

    private Optional<AttractionsOutputDto> mapTextToAttractionsOutDto(String text) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<AttractionsOutputDto.Attraction> attractions = mapper.readValue(text, new TypeReference<>(){});
            return Optional.of(AttractionsOutputDto.builder().attractions(attractions).build());
        } catch ( JsonProcessingException e) {
            log.error("Error when parsing gpt3 response {}", text);
        }
        return Optional.empty();
    }
}
