package org.kybartas.eventsync;

import org.kybartas.eventsync.dto.CreateEventDto;
import org.kybartas.eventsync.dto.SummaryDto;
import org.kybartas.eventsync.entity.Event;
import org.kybartas.eventsync.entity.Feedback;
import org.kybartas.eventsync.repository.EventRepository;
import org.kybartas.eventsync.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class EventSyncService {

    private final EventRepository eventRepository;
    private final FeedbackRepository feedbackRepository;

    @Value("${huggingface.api.url}")
    private String huggingfaceUrl;

    @Value("${huggingface.api.token}")
    private String huggingfaceToken;

    public EventSyncService(EventRepository eventRepository, FeedbackRepository feedbackRepository) {
        this.eventRepository = eventRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public Event createEvent(CreateEventDto eventData) {

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

        if (!eventRepository.existsById(eventId)) {
            throw new IllegalArgumentException("Event for id doesnt exist");
        }

        List<Feedback> eventFeedback = feedbackRepository.findAllByEvent_Id(eventId);
        int positive = 0, neutral = 0, negative = 0;

        for(Feedback feedback : eventFeedback) {

            switch (feedback.getSentiment()) {
                case "positive" -> positive++;
                case "neutral" -> neutral++;
                case "negative" -> negative++;
            }
        }

        return new SummaryDto(positive, neutral, negative);
    }

    // external api response looks like this:
    // [[{"label":"LABEL_0","score":0.8936647772789001},
    // {"label":"LABEL_1","score":0.09478294849395752},
    // {"label":"LABEL_2","score":0.011552193202078342}]]
    // LABEL_0 == negative, LABEL_1 == neutral, LABEL_2 == positive
    private String fetchSentiment(String input) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(huggingfaceToken);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        JSONObject body = new JSONObject();
        body.put("inputs", input);

        HttpEntity<String> entity = new HttpEntity<>(body.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(huggingfaceUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {

            JSONArray results = new JSONArray(response.getBody()).getJSONArray(0);
            String maxScoreLabel = "";
            double maxScore = -1;

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String resultLabel = result.getString("label");
                double resultScore = result.getDouble("score");

                if (resultScore > maxScore) {
                    maxScore = resultScore;
                    maxScoreLabel = resultLabel;
                }
            }

            return mapLabel(maxScoreLabel);
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