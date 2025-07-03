package org.kybartas.eventsync.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Schema(example = "There is no way that this space math will have any practical use.")
    private String text;

    private LocalDate timeStamp;

    @Schema(example = "negative")
    private String sentiment;

    @ManyToOne
    private Event event;

    @PrePersist
    public void generateTimeStamp() {
        this.timeStamp = LocalDate.now();
    }

    public Feedback() {};

    public void setId(long id) {
        this.id = id;
    }
    public long getId() {
        return id;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public void setTimeStamp(LocalDate timeStamp) {
        this.timeStamp = timeStamp;
    }
    public LocalDate getTimeStamp() {
        return timeStamp;
    }

    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
    public String getSentiment() {
        return sentiment;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    public Event getEvent() {
        return event;
    }
}