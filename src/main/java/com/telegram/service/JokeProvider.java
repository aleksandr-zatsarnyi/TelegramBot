package com.telegram.service;

import com.telegram.dto.JokeDto;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JokeProvider {

    private final RestTemplate restTemplate;

    public JokeProvider(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JokeDto getJoke() {
        ResponseEntity<String> response = restTemplate.exchange("http://rzhunemogu.ru/RandJSON.aspx?CType=1", HttpMethod.GET, null, String.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return new JokeDto(getContent(response.getBody()));
        }

        throw new RuntimeException();
    }

    private String getContent(String response) {
        int startOfContent = response.indexOf("\"content\":\"") + 11;
        int endOfContent = response.indexOf("}") - 1;
        return response.substring(startOfContent, endOfContent);
    }
}
