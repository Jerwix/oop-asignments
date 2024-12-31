package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class AddAccount implements Command {
    private final List<User> users;
    private final CommandInput input;

    public AddAccount(final List<User> users, final CommandInput input) {
        this.users = users;
        this.input = input;
    }

    /**
     * searches for the user and adds an account
     */
    public void execute() {
        final Account account = new Account(input.getCurrency(), input.getAccountType());
        for (final User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                user.getAccounts().add(account);
                addInHistory(user);
            }
        }
    }

    /**
     * adds the event of creating a new account to the user's history
     * @param user the user to update
     */
    public void addInHistory(final User user) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode addAccount = mapper.createObjectNode();
        addAccount.put("timestamp", input.getTimestamp());
        addAccount.put("description", "New account created");
        user.getHistory().add(addAccount);
    }
}

