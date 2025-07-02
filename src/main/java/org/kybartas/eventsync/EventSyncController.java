package org.kybartas.eventsync;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eventSync")
public class EventSyncController {

    private final EventSyncService service;
    public EventSyncController(EventSyncService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<?> createEvent(
            @RequestParam("title") String title,
            @RequestParam("description") String description) {

        Event newEvent = service.createEvent(title, description);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping("/events")
    public ResponseEntity<?> getEvents() {

        return ResponseEntity.ok(service.getEvents());
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
