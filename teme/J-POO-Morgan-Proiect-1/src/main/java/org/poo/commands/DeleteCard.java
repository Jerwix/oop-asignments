package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.Card;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class DeleteCard implements Command {
    private final List<User> users;
    private final CommandInput input;

    public DeleteCard(final List<User> users, final CommandInput command) {
        this.users = users;
        this.input = command;
    }

    public void execute() {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                for (final Card card : account.getCards()) {
                    if (card.getCardNumber().equals(input.getCardNumber())) {
                        account.getCards().remove(card);
                        final ObjectMapper mapper = new ObjectMapper();
                        final ObjectNode root = mapper.createObjectNode();
                        root.put("timestamp", input.getTimestamp());
                        root.put("description", "The card has been destroyed");
                        root.put("card", card.getCardNumber());
                        root.put("cardHolder", user.getEmail());
                        root.put("account", account.getIBAN());
                        user.getHistory().add(root);
                        break;
                    }
                }
            }
        }
    }
}
