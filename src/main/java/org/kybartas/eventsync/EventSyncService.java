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

    public Event createEvent(String title, String description) {

        Event event = new Event();
        event.setTitle(title);
        event.setDescription(description);

        return eventRepository.save(event);
    }

    public List<Event> getEvents() {

        return eventRepository.findAll();
    }

    public Feedback addFeedback(long eventId, String text) {

        Event event = eventRepository.findById(eventId).orElseThrow();
        Feedback feedback = new Feedback();

        feedback.setEvent(event);
        feedback.setText(text);
        feedback.setSentiment("mad");

        return feedbackRepository.save(feedback);
    }

    public List<String> getSummary(long eventId) {

        List<Feedback> eventFeedback = feedbackRepository.findAllByEvent_Id(eventId);
        List<String> summary = new ArrayList<>();

        for(Feedback feedback : eventFeedback) {
            summary.add(feedback.getSentiment());
        }

        summary.add(0, "Feedback count: " + summary.size());

        return summary;
    }
}
