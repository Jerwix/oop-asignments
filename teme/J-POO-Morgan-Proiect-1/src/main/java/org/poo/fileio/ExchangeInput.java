package org.poo.fileio;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class ExchangeInput {
    private String from;
    private String to;
    private double rate;
    private int timestamp;

    public ExchangeInput(final String from, final String to, final double rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }
}
