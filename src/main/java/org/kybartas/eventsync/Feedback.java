package org.kybartas.eventsync;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String text;
    private LocalDateTime timeStamp;
    private String sentiment;

    @ManyToOne
    private Event event;

    @PrePersist
    public void generateTimeStamp() {
        this.timeStamp = LocalDateTime.now();
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

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }
    public LocalDateTime getTimeStamp() {
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