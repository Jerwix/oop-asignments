package org.poo.bankData;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ExchangeInput;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public final class Exchange {
    private List<ExchangeInput> table;

    public Exchange(final ExchangeInput[] table) {
        this.table = new ArrayList<>(Arrays.asList(table));
        extendTable();
    }

    /**
     * for each exchange pair from "ABC" to "XYZ"
     * generates the inverse from "XYZ" to "ABC"
     */
    public void extendTable() {
        final int n = table.size();
        for (int i = 0; i < n; i++) {
            final String src = table.get(i).getFrom();
            final String dst = table.get(i).getTo();
            final double rate = table.get(i).getRate();
            table.add(new ExchangeInput(dst, src, 1 / rate));
        }
    }

    /**
     * Since USD is a centralized currency, every exchange from X to Y is in
     * at most 2 steps, so two for loops are enough.
     * @param src currency of the source
     * @param dst currency of the destination
     * @return the total multiplier rate of the exchange
     */
    public double rate(final String src, final String dst) {
        if (src.equals(dst)) {
            return 1.0;
        }
        for (final ExchangeInput trade : table) {
            if (trade.getFrom().equals(src) && trade.getTo().equals(dst)) {
                return trade.getRate();
            }
            for (final ExchangeInput trade2 : table) {
                if (trade2.getFrom().equals(trade.getTo()) && trade.getFrom().equals(src)
                    && trade2.getTo().equals(dst)) {
                    return trade.getRate() * trade2.getRate();
                }
            }
        }
        return 0;
    }

    /**
     * @param src    account that sends the money
     * @param dst    account that receives the money
     * @param amount amount of money based on src currency
     * @throws Exception if the transfer is not possible due to insufficient funds
     */
    public void transfer(final Account src, final Account dst, final double amount)
            throws Exception {
        if (src.getBalance() - amount >= 0) {
            src.setBalance(src.getBalance() - amount);
            dst.setBalance(dst.getBalance() + amount * rate(src.getCurrency(), dst.getCurrency()));
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    /**
     *
     * @param src account for payment
     * @param amount of money
     * @param currency the currency of the money sent
     * @throws Exception if there aren't sufficient funds for the payment
     */
    public void payment(final Account src, final double amount, final String currency)
            throws Exception {
        if (src.getBalance() - amount * rate(currency, src.getCurrency()) >= 0) {
            src.setBalance(src.getBalance() - amount * rate(currency, src.getCurrency()));
        } else {
            throw new Exception("Insufficient funds");
        }
    }

    /**
     * method to add the transaction info to the history of transactions of a user
     * @param user the user who made the payment
     * @param src the account used
     * @param dst the account where the money was sent
     * @param command details about the transaction
     */
    public void log(final User user, final Account src, final Account dst,
                    final CommandInput command) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode node = mapper.createObjectNode();
        node.put("timestamp", command.getTimestamp());
        node.put("description", command.getDescription());
        node.put("senderIBAN", src.getIBAN());
        node.put("receiverIBAN", dst.getIBAN());
        node.put("amount", command.getAmount() + " " + src.getCurrency());
        node.put("transferType", "sent");
        user.getHistory().add(node);

    }
}
