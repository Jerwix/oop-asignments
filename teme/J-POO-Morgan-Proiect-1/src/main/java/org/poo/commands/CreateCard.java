package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.Card;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

import java.util.List;

public final class CreateCard implements Command {

    private final List<User> users;
    private final CommandInput input;

    public CreateCard(final List<User> users, final CommandInput input) {
        this.users = users;
        this.input = input;
    }

    @Override
    public void execute() {
        final Card card = new Card(Utils.generateCardNumber(), 0);
        addCard(card, users, input);
    }

    /**
     * searches for the right user, then the right account
     * and adds the details to the history of the user
     * @param card to be added to the account
     * @param users the owner of the account
     * @param input details about the command to be included in the history of the user
     */
    static void addCard(final Card card, final List<User> users, final CommandInput input) {
        for (final User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                for (final Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(input.getAccount())) {
                        account.getCards().add(card);

                        final ObjectMapper mapper = new ObjectMapper();
                        final ObjectNode root = mapper.createObjectNode();
                        root.put("timestamp", input.getTimestamp());
                        root.put("description", "New card created");
                        root.put("card", card.getCardNumber());
                        root.put("cardHolder", user.getEmail());
                        root.put("account", account.getIBAN());
                        user.getHistory().add(root);
                    }
                }
            }
        }
    }
}
