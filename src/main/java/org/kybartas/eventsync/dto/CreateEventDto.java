package org.kybartas.eventsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "To accept a clean 2-param json for event creation")
public class CreateEventDto {

    private final String title;
    private final String description;

    public CreateEventDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
}
