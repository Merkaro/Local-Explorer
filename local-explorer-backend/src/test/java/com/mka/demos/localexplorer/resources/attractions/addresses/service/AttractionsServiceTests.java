package com.mka.demos.localexplorer.resources.attractions.addresses.service;

import com.mka.demos.localexplorer.TestUtils;
import com.mka.demos.localexplorer.common.proxies.gpt3.CompletionsOutput;
import com.mka.demos.localexplorer.common.proxies.gpt3.CompletionsProxy;
import com.mka.demos.localexplorer.common.proxies.openweathermap.OpenWeatherMapProxy;
import com.mka.demos.localexplorer.resources.attractions.addresses.dto.AttractionsInputDto;
import com.mka.demos.localexplorer.resources.attractions.addresses.dto.AttractionsOutputDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AttractionsServiceTests {

    @Mock
    private OpenWeatherMapProxy openWeatherMapProxy;

    @Mock
    private CompletionsProxy completionsProxy;

    @InjectMocks
    private AttractionsService attractionsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void test_callOpenWeatherMapKO_callCompletionsKO_KO() {
        when(openWeatherMapProxy.getTodaysWeatherForcast(any(), any())).thenReturn(Optional.empty());
        when(completionsProxy.callGpt3Completions(any())).thenReturn(Optional.empty());

        Optional<AttractionsOutputDto> actualResponseOp = attractionsService.getAttractionsToExplore(getDefaultLocation());
        Assertions.assertTrue(actualResponseOp.isEmpty());
    }

    @Test
    void test_callOpenWeatherMapKO_OK() {
        when(openWeatherMapProxy.getTodaysWeatherForcast(any(), any()))
                .thenReturn(Optional.empty());
        CompletionsOutput mockedCompletionsOutput =
                TestUtils.getResponseFromJsonFile("mocks/attractions/addresses/completionsOutput_OK.json", CompletionsOutput.class);
        when(completionsProxy.callGpt3Completions(any()))
                .thenReturn(Optional.of(mockedCompletionsOutput));

        AttractionsOutputDto expectedResponse = TestUtils.getResponseFromJsonFile(
                "mocks/attractions/addresses/attractionsOutput_expected_OK.json",
                        AttractionsOutputDto.class);

        Optional<AttractionsOutputDto> actualResponseOp = attractionsService.getAttractionsToExplore(getDefaultLocation());
        Assertions.assertTrue(actualResponseOp.isPresent());

        AttractionsOutputDto actualResponse = actualResponseOp.get();
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    private AttractionsInputDto getDefaultLocation() {
        return AttractionsInputDto.builder()
                .longitude("123")
                .latitude("456")
                .city("Rabat, Morocco")
                .build();
    }
}
