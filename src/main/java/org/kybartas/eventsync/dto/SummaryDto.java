package org.kybartas.eventsync.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "To return a clean 4-param json with feedback summary")
public class SummaryDto {

    private final int total;
    private final int positive;
    private final int neutral;
    private final int negative;

    public SummaryDto(int total, int positive, int neutral, int negative) {
        this.total = total;
        this.positive = positive;
        this.neutral = neutral;
        this.negative = negative;
    }

    public int getTotal() {
        return total;
    }
    public int getPositive() {
        return positive;
    }
    public int getNeutral() {
        return neutral;
    }
    public int getNegative() {
        return negative;
    }
}
