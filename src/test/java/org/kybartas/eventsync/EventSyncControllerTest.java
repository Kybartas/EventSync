package org.kybartas.eventsync;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kybartas.eventsync.dto.EventDto;
import org.kybartas.eventsync.dto.SummaryDto;
import org.kybartas.eventsync.entity.Event;
import org.kybartas.eventsync.entity.Feedback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventSyncController.class)
public class EventSyncControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private EventSyncService service;

    private Event testEvent;
    private List<Event> testEventList = new ArrayList<>();
    private Feedback testFeedback;
    private long eventId;
    private String feedbackText;
    private LocalDate currentDate;

    @BeforeEach
    public void setup() {
        eventId = 1L;
        feedbackText = "Feedback left for some event Test";
        currentDate = LocalDate.now();

        testEvent = new Event();
        testEvent.setId(eventId);
        testEvent.setTitle("Test Event");
        testEvent.setDescription("Test Description");
        testEventList.add(testEvent);

        testFeedback = new Feedback();
        testFeedback.setId(1L);
        testFeedback.setText(feedbackText);
        testFeedback.setTimeStamp(currentDate);
        testFeedback.setSentiment("AI evaluation");
        testFeedback.setEvent(testEvent);
    }

    @Test
    public void testCreateEvent_ReturnsSavedEvent() throws Exception {
        
        String eventJson = "{\"title\":\"Test Event\",\"description\":\"Test Description\"}";

        when(service.createEvent(any(EventDto.class))).thenReturn(testEvent);

        mvc.perform(post("/eventSync/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    public void testGetEvents_ReturnsEventList() throws Exception {

        when(service.getEvents()).thenReturn(testEventList);

        mvc.perform(get("/eventSync/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Event"))
                .andExpect(jsonPath("$[0].description").value("Test Description"));
    }

    @Test
    public void testGetSummary_ReturnsSummary() throws Exception {

        long eventId = 1L;
        SummaryDto expectedSummary = new SummaryDto(3, 1, 1, 1);

        when(service.getSummary(eventId)).thenReturn(expectedSummary);

        mvc.perform(get("/eventSync/events/" + eventId + "/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.positive").value(1))
                .andExpect(jsonPath("$.neutral").value(1))
                .andExpect(jsonPath("$.negative").value(1));
    }

    @Test
    public void testAddFeedback_ReturnsSavedFeedback() throws Exception {

        when(service.addFeedback(eventId, feedbackText)).thenReturn(testFeedback);

        mvc.perform(post("/eventSync/events/" + eventId + "/feedback")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(feedbackText))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.text").value("Feedback left for some event Test"))
                .andExpect(jsonPath("$.timeStamp").value(currentDate.toString()))
                .andExpect(jsonPath("$.sentiment").value("AI evaluation"))
                .andExpect(jsonPath("$.event.id").value(1L))
                .andExpect(jsonPath("$.event.title").value("Test Event"))
                .andExpect(jsonPath("$.event.description").value("Test Description"));
    }
}