package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.Account;
import org.poo.bankData.Exchange;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public class SplitPayment implements Command {
    private final List<User> users;
    private final CommandInput input;
    private final Exchange exchange;

    public SplitPayment(final List<User> users, final CommandInput command,
                        final Exchange exchange) {
        this.users = users;
        this.input = command;
        this.exchange = exchange;
    }

    /**
     * executes the payment as usual for all the accounts involved
     * with the amount of money split equally
     */
    public void execute() {
        for (final User user : users) {
            for (final Account account : user.getAccounts()) {
                if (input.getAccounts().contains(account.getIBAN())) {
                    try {
                        exchange.payment(account, input.getAmount() / input.getAccounts().size(),
                                         input.getCurrency());
                    } catch (final Exception e) {
                        System.out.println(e.getMessage());
                    }
                    final ObjectNode root = jsonNodes();
                    user.getHistory().add(root);
                }
            }
        }
    }

    private ObjectNode jsonNodes() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode root = mapper.createObjectNode();
        root.put("timestamp", input.getTimestamp());
        root.put("description", "Split payment of " + String.format("%.2f", input.getAmount()) + " "
                                + input.getCurrency());
        root.put("currency", input.getCurrency());
        root.put("amount", input.getAmount() / input.getAccounts().size());
        final ArrayNode accounts = mapper.createArrayNode();
        for (final String acc : input.getAccounts()) {
            accounts.add(acc);
        }
        root.set("involvedAccounts", accounts);
        return root;
    }
}
