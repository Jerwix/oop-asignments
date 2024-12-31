package org.poo.bankData;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public final class Account {
    private String IBAN;
    private Double balance;
    private String currency;
    private String type;
    private List<Card> cards;
    private Double minBalance;

    public Account(final String currency, final String type) {
        this.IBAN = Utils.generateIBAN();
        this.balance = 0.0;
        this.currency = currency;
        this.type = type;
        this.cards = new ArrayList<>();
        this.minBalance = 0.0;
    }
}
