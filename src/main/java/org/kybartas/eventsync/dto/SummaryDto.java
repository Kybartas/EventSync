package org.kybartas.eventsync.dto;

import java.util.List;

public class SummaryDto {

    private final int count;
    private final List<String> sentiments;

    public SummaryDto(List<String> sentiments) {
        this.count = sentiments.size();
        this.sentiments = sentiments;
    }

    public int getCount() {
        return count;
    }

    public List<String> getSentiments() {
        return sentiments;
    }
}
