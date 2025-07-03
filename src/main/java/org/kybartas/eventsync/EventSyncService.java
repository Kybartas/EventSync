package org.kybartas.eventsync;

import org.springframework.stereotype.Service;

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
        feedback.setSentiment("mad");

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
}
