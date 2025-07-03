package org.kybartas.eventsync;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/eventSync")
public class EventSyncController {

    private final EventSyncService service;
    private final EventRepository eventRepository;
    public EventSyncController(EventSyncService service, EventRepository eventRepository) {
        this.service = service;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(
            @RequestBody Event event) {

        return ResponseEntity.ok(eventRepository.save(event));
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEvents() {

        return ResponseEntity.ok(eventRepository.findAll());
    }

    @PostMapping("events/{eventId}/feedback")
    public ResponseEntity<?> handleFeedback(
            @PathVariable String eventId,
            @RequestBody String feedback) {

        return ResponseEntity.ok(service.addFeedback(Long.parseLong(eventId), feedback));
    }

    @GetMapping("events/{eventId}/summary")
    public ResponseEntity<?> returnSummary(
            @PathVariable String eventId) {

        return ResponseEntity.ok(service.getSummary(Long.parseLong(eventId)));
    }
}
