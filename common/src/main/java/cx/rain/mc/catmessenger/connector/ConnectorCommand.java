package cx.rain.mc.catmessenger.connector;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConnectorCommand {

    private String sender;
    private String callback;
    private EnumCommand command;
    private int replyTo;
    private String[] arguments;

    @Getter
    public enum EnumCommand {
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
    }
}
