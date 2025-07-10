package org.kybartas.eventsync;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.kybartas.eventsync.dto.CreateEventDto;
import org.kybartas.eventsync.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EventSyncIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EventSyncService service;

    @Test
    public void testFullEventFlow() throws Exception {

        // 1. Create an event
        CreateEventDto createEventDto = new CreateEventDto("Test Event", "Test Description");
        String eventJson = objectMapper.writeValueAsString(createEventDto);

        String responseJson = mvc.perform(post("/eventSync/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Event createdEvent = objectMapper.readValue(responseJson, Event.class);

        // 2. Get events has data?
        mvc.perform(get("/eventSync/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].title").exists())
                .andExpect(jsonPath("$.[0].description").exists());

        // 3. Add feedback
        mvc.perform(post("/eventSync/events/" + createdEvent.getId() + "/feedback")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Feedback left for some event Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Feedback left for some event Test"))
                .andExpect(jsonPath("$.sentiment").isNotEmpty())
                .andExpect(jsonPath("$.event.id").value(createdEvent.getId()));

        // 4. Get summary (should have 1 feedback now)
        mvc.perform(get("/eventSync/events/" + createdEvent.getId() + "/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    public void testCreateEvent_NoTitle_ReturnsBadRequest() throws Exception {

        CreateEventDto createEventDto = new CreateEventDto("", "Test Description");
        String eventJson = objectMapper.writeValueAsString(createEventDto);

        mvc.perform(post("/eventSync/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEvent_NoDescription_ReturnsBadRequest() throws Exception {

        CreateEventDto createEventDto = new CreateEventDto("Test Event", "");
        String eventJson = objectMapper.writeValueAsString(createEventDto);

        mvc.perform(post("/eventSync/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateEvent_NoTitleNoDescription_ReturnsBadRequest() throws Exception {

        CreateEventDto createEventDto = new CreateEventDto("", "");
        String eventJson = objectMapper.writeValueAsString(createEventDto);

        mvc.perform(post("/eventSync/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFeedback_InvalidId_ReturnsBadRequest() throws Exception {

        mvc.perform(post("/eventSync/events/99999/feedback")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("Test Feedback"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddFeedback_NoText_ReturnsBadRequest() throws Exception {

        // Careful not to trigger exception from invalidId instead of no text
        CreateEventDto createEventDto = new CreateEventDto("Test Event", "Test Description");
        String eventJson = objectMapper.writeValueAsString(createEventDto);
        for(int i = 0; i < 3; i++) {
            mvc.perform(post("/eventSync/events")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(eventJson))
                    .andExpect(status().isOk());
        }

        mvc.perform(post("/eventSync/events/1/feedback")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetSummary_InvalidId_ReturnsBadRequest() throws Exception {

        mvc.perform(get("/eventSync/events/99999/summary"))
                .andExpect(status().isBadRequest());
    }
}