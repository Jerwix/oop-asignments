package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.Card;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class CheckCardStatus implements Command {
    private final List<User> users;
    private final CommandInput input;
    private final ArrayNode output;

    public CheckCardStatus(final List<User> users, final CommandInput command,
                           final ArrayNode output) {
        this.users = users;
        this.input = command;
        this.output = output;
    }

    /**
     * Loops through all users, accounts, cards until the requested card is found
     * if the balance is under the set minimum, the card is frozen and the appropriate
     * message is displayed
     * if the card is not found, the appropriate message is displayed
     */
    public void execute() {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                for (final Card card : account.getCards()) {
                    if (card.getCardNumber().equals(input.getCardNumber())) {
                        if (account.getBalance() <= account.getMinBalance()
                            && card.getStatus().equals("active")) {
                            card.setStatus("frozen");
                            final ObjectMapper mapper = new ObjectMapper();
                            final ObjectNode root = mapper.createObjectNode();
                            root.put("timestamp", input.getTimestamp());
                            root.put("description",
                                     "You have reached the minimum amount of funds, the card will"
                                     + " be frozen");
                            user.getHistory().add(root);
                        }
                        return;
                    }
                }
            }
        }
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode root = mapper.createObjectNode();
        final ObjectNode node = mapper.createObjectNode();
        node.put("command", input.getCommand());
        root.put("timestamp", input.getTimestamp());
        root.put("description", "Card not found");
        node.set("output", root);
        node.put("timestamp", input.getTimestamp());
        output.add(node);
    }
}
