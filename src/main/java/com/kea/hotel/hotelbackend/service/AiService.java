package com.kea.hotel.hotelbackend.service;

import com.kea.hotel.hotelbackend.mongodb.document.AiInteraction;
import com.kea.hotel.hotelbackend.mongodb.repository.AiInteractionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    private final AiInteractionRepository repository;
    private final RestTemplate restTemplate;

    @Value("${ai.service.url:http://localhost:8000}")
    private String aiServiceUrl;

    public AiService(AiInteractionRepository repository) {
        this.repository = repository;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);
        factory.setReadTimeout(60_000);
        this.restTemplate = new RestTemplate(factory);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> askGuest(String question) {
        Map<String, String> request = Map.of("question", question);
        Map<String, Object> response = restTemplate.postForObject(
                aiServiceUrl + "/guest/ask", request, Map.class);

        String answer = response != null ? (String) response.get("answer") : "AI service unavailable";

        AiInteraction interaction = new AiInteraction();
        interaction.setBotType("guest");
        interaction.setQuestion(question);
        interaction.setAnswer(answer);
        interaction.setTimestamp(LocalDateTime.now());
        repository.save(interaction);

        return Map.of("answer", answer);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> askStaff(String question) {
        Map<String, String> request = Map.of("question", question);
        Map<String, Object> response = restTemplate.postForObject(
                aiServiceUrl + "/staff/ask", request, Map.class);

        String answer = response != null ? (String) response.get("answer") : "AI service unavailable";
        List<String> sources = response != null ? (List<String>) response.get("sources") : List.of();

        AiInteraction interaction = new AiInteraction();
        interaction.setBotType("staff");
        interaction.setQuestion(question);
        interaction.setAnswer(answer);
        interaction.setSources(sources);
        interaction.setTimestamp(LocalDateTime.now());
        repository.save(interaction);

        return Map.of("answer", answer, "sources", sources != null ? sources : List.of());
    }

    public Page<AiInteraction> getInteractions(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<AiInteraction> getInteractionsByBotType(String botType, Pageable pageable) {
        return repository.findByBotType(botType, pageable);
    }
}
