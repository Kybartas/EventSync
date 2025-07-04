package org.kybartas.eventsync.dto;

public class EventDto {

    private final String title;
    private final String description;

    public EventDto(String title, String description) {
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
