package org.kybartas.eventsync;

import org.kybartas.eventsync.dto.EventDto;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
public class EventSyncService {

    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;

    public EventSyncService(EventRepository eventRepository, FeedbackRepository feedbackRepository) {
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public Event createEvent(EventDto eventData) {

        if(eventData.getTitle().isEmpty() || eventData.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Event title and description are required");
        }

        Event event = new Event();
        event.setTitle(eventData.getTitle());
        event.setDescription(eventData.getDescription());

        return eventRepository.save(event);
    }

    public List<Event> getEvents() {

        return eventRepository.findAll();
    }

    public Feedback addFeedback(long eventId, String text) {

        if(text.isEmpty()) {
            throw new IllegalArgumentException("Feedback text is required");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NoSuchElementException("Event for id doesnt exist"));

        Feedback feedback = new Feedback();
        feedback.setEvent(event);
        feedback.setText(text);
        feedback.setSentiment(fetchSentiment(text));

        return feedbackRepository.save(feedback);
    }

    public SummaryDto getSummary(long eventId) {

        List<Feedback> eventFeedback = feedbackRepository.findAllByEvent_Id(eventId);

        if(eventFeedback.isEmpty()) {
            throw new NoSuchElementException("No feedback found for given eventId");
        }

        int total = 0, positive = 0, neutral = 0, negative = 0;
        for(Feedback feedback : eventFeedback) {

            String sentiment = feedback.getSentiment();
            total = total + 1;
            if (Objects.equals(sentiment, "positive")) {
                positive = positive + 1;
            } else if (Objects.equals(sentiment, "neutral")) {
                neutral = neutral + 1;
            } else if (Objects.equals(sentiment, "negative")) {
                negative = negative + 1;
            }
        }

        return new SummaryDto(total, positive, neutral, negative);
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