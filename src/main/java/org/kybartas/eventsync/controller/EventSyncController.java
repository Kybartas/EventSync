package org.kybartas.eventsync.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.kybartas.eventsync.service.EventSyncService;
import org.kybartas.eventsync.dto.SummaryDto;
import org.kybartas.eventsync.entity.Event;
import org.kybartas.eventsync.entity.Feedback;
import org.kybartas.eventsync.repository.EventRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/eventSync")
@Tag(name = "Endpoints")
public class EventSyncController {

    private final EventSyncService service;
    private final EventRepository eventRepository;

    public EventSyncController(EventSyncService service, EventRepository eventRepository) {
        this.service = service;
        this.eventRepository = eventRepository;
    }

    @PostMapping(value = "/events")
    public ResponseEntity<Event> createEvent(
            @RequestBody Event event) {

        return ResponseEntity.ok(eventRepository.save(event));
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getEvents() {

        return ResponseEntity.ok(eventRepository.findAll());
    }

    @PostMapping(value = "events/{eventId}/feedback")
    public ResponseEntity<Feedback> handleFeedback(
            @PathVariable String eventId,
            @RequestBody String feedback) {

        return ResponseEntity.ok(service.addFeedback(Long.parseLong(eventId), feedback));
    }

    @GetMapping("events/{eventId}/summary")
    public ResponseEntity<SummaryDto> returnSummary(
            @PathVariable String eventId) {

        return ResponseEntity.ok(service.getSummary(Long.parseLong(eventId)));
    }
}
