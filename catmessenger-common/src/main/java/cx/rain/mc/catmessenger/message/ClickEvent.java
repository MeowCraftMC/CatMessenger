package cx.rain.mc.catmessenger.message;

public enum ClickEvent {
    OPEN_URL("open_url"),
    RUN_COMMAND("run_command"),
    SUGGEST_COMMAND("suggest_command"),
    COPY("copy"),
    ;

    private final String name;

    ClickEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ClickEvent of(String name) {
        for (var v : values()) {
            if (v.getName().equals(name)) {
                return v;
            }
        }

        return null;
    }
}
