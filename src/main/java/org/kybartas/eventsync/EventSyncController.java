package org.kybartas.eventsync;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.kybartas.eventsync.dto.EventDto;
import org.kybartas.eventsync.dto.SummaryDto;
import org.kybartas.eventsync.entity.Event;
import org.kybartas.eventsync.entity.Feedback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/eventSync")
@Tag(name = "Endpoints")
public class EventSyncController {

    private final EventSyncService service;

    public EventSyncController(EventSyncService service) {
        this.service = service;
    }

    @PostMapping(value = "/events", consumes = "application/json")
    public ResponseEntity<Event> createEvent(
            @RequestBody EventDto eventDto) {

        Event event = service.createEvent(eventDto);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {

        List<Event> events = service.getEvents();
        return ResponseEntity.ok(events);
    }

    @PostMapping(value = "events/{eventId}/feedback", consumes = "text/plain")
    public ResponseEntity<Feedback> addFeedback(
            @PathVariable String eventId,
            @RequestBody String text) {

        Feedback feedback = service.addFeedback(Long.parseLong(eventId), text);
        return ResponseEntity.ok(feedback);
    }

    @GetMapping("events/{eventId}/summary")
    public ResponseEntity<SummaryDto> getSummary(
            @PathVariable String eventId) {

        SummaryDto summary = service.getSummary(Long.parseLong(eventId));
        return ResponseEntity.ok(summary);
    }
}
