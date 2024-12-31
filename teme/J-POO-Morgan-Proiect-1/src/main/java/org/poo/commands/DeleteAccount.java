package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class DeleteAccount implements Command {
    private final List<User> users;
    private final CommandInput input;
    private final ArrayNode output;

    public DeleteAccount(final List<User> users, final CommandInput command,
                         final ArrayNode output) {
        this.users = users;
        this.input = command;
        this.output = output;
    }

    /**
     * searches for the account and deletes it, adds the appropriate details
     * to the output
     */
    @Override
    public void execute() {
        for (final User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                for (final Account account : user.getAccounts()) {
                    if (account.getIBAN().equals(input.getAccount()) && account.getBalance() <= 0) {
                        user.getAccounts().remove(account);
                        final ObjectNode root = jsonNodes();
                        output.add(root);
                        return;
                    }
                }
            }
        }
        final ObjectNode root =
                nodes("error",
                      "Account couldn't be deleted - see org.poo.transactions for details");
        output.add(root);
    }

    private ObjectNode nodes(final String error, final String v) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode root = mapper.createObjectNode();
        root.put("command", "deleteAccount");
        final ObjectNode commandOutput = mapper.createObjectNode();
        commandOutput.put(error,
                          v);
        commandOutput.put("timestamp", input.getTimestamp());
        root.set("output", commandOutput);
        root.put("timestamp", input.getTimestamp());
        return root;
    }

    private ObjectNode jsonNodes() {
        return nodes("success", "Account deleted");
    }
}
