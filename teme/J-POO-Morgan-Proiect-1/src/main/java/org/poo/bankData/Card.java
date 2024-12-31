package org.poo.bankData;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.utils.Utils;

@Data
@NoArgsConstructor
public final class Card {
    private String cardNumber;
    private String status;
    private int oneTime;

    public Card(final String cardNumber, final int oneTime) {
        this.cardNumber = cardNumber;
        this.status = "active";
        this.oneTime = oneTime;
    }

    /**
     * for a "new" one time card, just replace the number
     */
    public void replaceCard() {
        this.cardNumber = Utils.generateCardNumber();
    }
}
