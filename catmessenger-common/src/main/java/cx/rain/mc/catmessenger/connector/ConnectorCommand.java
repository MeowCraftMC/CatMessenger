package cx.rain.mc.catmessenger.connector;

public class ConnectorCommand {

    private String sender;
    private String callback;
    private EnumCommand command;
    private int replyTo;
    private String[] arguments;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public EnumCommand getCommand() {
        return command;
    }

    public void setCommand(EnumCommand command) {
        this.command = command;
    }

    public int getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(int replyTo) {
        this.replyTo = replyTo;
    }

    public String[] getArguments() {
        return arguments;
    }

    public void setArguments(String[] arguments) {
        this.arguments = arguments;
    }

    public enum EnumCommand
    {
        ERROR(0),
        ONLINE(1),
        OFFLINE(2),
        QUERY_ONLINE(3),
        QUERY_WORLD_TIME(4),
        RESPONSE_ONLINE(5),
        RESPONSE_WORLD_TIME(6),
        RUN_COMMAND(7),
        COMMAND_RESULT(8),
        ;

        private final int id;

        EnumCommand(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
