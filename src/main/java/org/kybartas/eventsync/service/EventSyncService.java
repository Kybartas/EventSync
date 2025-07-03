package org.kybartas.eventsync.service;

import org.kybartas.eventsync.dto.SummaryDto;
import org.kybartas.eventsync.entity.Event;
import org.kybartas.eventsync.entity.Feedback;
import org.kybartas.eventsync.repository.EventRepository;
import org.kybartas.eventsync.repository.FeedbackRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@Service
public class EventSyncService {

    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;
    public EventSyncService(EventRepository eventRepository, FeedbackRepository feedbackRepository) {
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public Feedback addFeedback(long eventId, String text) {

        Event event = eventRepository.findById(eventId).orElseThrow();
        Feedback feedback = new Feedback();

        feedback.setEvent(event);
        feedback.setText(text);
        feedback.setSentiment(fetchSentiment(text));

        return feedbackRepository.save(feedback);
    }

    public SummaryDto getSummary(long eventId) {

        List<Feedback> eventFeedback = feedbackRepository.findAllByEvent_Id(eventId);
        List<String> sentiments = new ArrayList<>();

        for(Feedback feedback : eventFeedback) {
            sentiments.add(feedback.getSentiment());
        }

        return new SummaryDto(sentiments);
    }

    // external api response looks like this:
    // [{"label":"LABEL_0","score":0.8936647772789001},
    // {"label":"LABEL_1","score":0.09478294849395752},
    // {"label":"LABEL_2","score":0.011552193202078342}]
    // 0 -> negative, 1 -> neutral, 2 -> positive
    private String fetchSentiment(String input) {
        
        String url = "https://api-inference.huggingface.co/models/cardiffnlp/twitter-roberta-base-sentiment";
        String token = "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";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        JSONObject body = new JSONObject();
        body.put("inputs", input);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONArray outer = new JSONArray(response.getBody());
            JSONArray inner = outer.getJSONArray(0);

            double maxScore = -1;
            String bestLabel = "";
            for (int i = 0; i < inner.length(); i++) {
                JSONObject obj = inner.getJSONObject(i);
                double score = obj.getDouble("score");
                if (score > maxScore) {
                    maxScore = score;
                    bestLabel = obj.getString("label");
                }
            }
            return mapLabel(bestLabel);
        }
        return "unknown";
    }

    private String mapLabel(String label) {
        return switch (label) {
            case "LABEL_0" -> "negative";
            case "LABEL_1" -> "neutral";
            case "LABEL_2" -> "positive";
            default -> "unknown";
        };
    }
}
