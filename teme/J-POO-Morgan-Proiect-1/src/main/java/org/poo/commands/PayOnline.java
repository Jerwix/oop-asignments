package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.Card;
import org.poo.bankData.Exchange;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class PayOnline implements Command {
    private final List<User> users;
    private final CommandInput input;
    private final ArrayNode output;
    private final Exchange exchange;

    public PayOnline(final List<User> users, final CommandInput command, final ArrayNode output,
                     final Exchange exchange) {
        this.users = users;
        this.input = command;
        this.output = output;
        this.exchange = exchange;
    }

    /**
     * finds the card used for payment
     * checks if it's frozen
     * adds the details in the user's history
     * replaces the card in case it was one time
     * displays the appropriate error in case the card was not found
     */
    public void execute() {
        for (final User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                for (final Account account : user.getAccounts()) {
                    for (final Card card : account.getCards()) {
                        if (card.getCardNumber().equals(input.getCardNumber())) {
                            if (card.getStatus().equals("frozen")) {
                                final ObjectMapper mapper = new ObjectMapper();
                                final ObjectNode root = mapper.createObjectNode();
                                root.put("timestamp", input.getTimestamp());
                                root.put("description", "The card is frozen");
                                user.getHistory().add(root);
                                return;
                            }
                            try {
                                exchange.payment(account, input.getAmount(), input.getCurrency());
                            } catch (final Exception e) {
                                final ObjectMapper mapper = new ObjectMapper();
                                final ObjectNode root = mapper.createObjectNode();
                                root.put("timestamp", input.getTimestamp());
                                root.put("description", e.getMessage());
                                user.getHistory().add(root);
                                return;
                            }
                            final ObjectMapper mapper = new ObjectMapper();
                            final ObjectNode root = mapper.createObjectNode();
                            root.put("timestamp", input.getTimestamp());
                            root.put("description", "Card payment");
                            root.put("amount",
                                     input.getAmount() * exchange.rate(input.getCurrency(),
                                                                       account.getCurrency()));
                            root.put("commerciant", input.getCommerciant());
                            user.getHistory().add(root);
                            if (card.getOneTime() == 1) {
                                card.replaceCard();
                            }
                            return;
                        }
                    }
                }
                final ObjectMapper mapper = new ObjectMapper();
                final ObjectNode root = mapper.createObjectNode();
                root.put("command", "payOnline");
                final ObjectNode commandOutput = mapper.createObjectNode();
                commandOutput.put("timestamp", input.getTimestamp());
                commandOutput.put("description", "Card not found");
                root.set("output", commandOutput);
                root.put("timestamp", input.getTimestamp());
                output.add(root);
                return;
            }
        }
    }
}
