package org.poo.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.bankData.User;
import org.poo.fileio.CommandInput;

import java.util.List;

public final class PrintTransactions implements Command {
    private final List<User> users;
    private final CommandInput input;
    private final ArrayNode output;

    public PrintTransactions(final List<User> users, final CommandInput command,
                             final ArrayNode output) {
        this.users = users;
        this.input = command;
        this.output = output;
    }

    /**
     * makes a deep copy of the user's history and adds it to the output
     */
    public void execute() {
        for (final User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                final ObjectMapper mapper = new ObjectMapper();
                final ObjectNode root = mapper.createObjectNode();

                final ArrayNode originalArrayNode = user.getHistory();
                final ArrayNode deepCopyArrayNode = mapper.createArrayNode();

                for (final JsonNode node : originalArrayNode) {
                    deepCopyArrayNode.add(node.deepCopy());
                }

                root.put("command", "printTransactions");
                root.set("output", deepCopyArrayNode);
                root.put("timestamp", input.getTimestamp());
                System.out.println(output);
                System.out.println(root);
                output.add(root);
            }
        }

    }
}
