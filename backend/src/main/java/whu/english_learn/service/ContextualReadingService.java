package whu.english_learn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import whu.english_learn.dto.ContextualReadingRequestDTO;

@Service
public class ContextualReadingService {

    private static final String API_URL = "your_api_url";
    private static final String API_KEY = "your_api_key";

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> sendRequest(ContextualReadingRequestDTO dto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        HttpEntity<ContextualReadingRequestDTO> request = new HttpEntity<>(dto, headers);

        return restTemplate.postForEntity(API_URL, request, String.class);
    }
}
