package com.telegram.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telegram.dto.GifDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class YesNoProvider {
    private final RestTemplate restTemplate;

    public YesNoProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GifDto getGif() {
        ResponseEntity<String> response = restTemplate.exchange("https://yesno.wtf/api", HttpMethod.GET, null, String.class);
        ObjectMapper mapper = new ObjectMapper();

        try {
            GifDto dto = mapper.readValue(response.getBody(), GifDto.class);
            return dto;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
