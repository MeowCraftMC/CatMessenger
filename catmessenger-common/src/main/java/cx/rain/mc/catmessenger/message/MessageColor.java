package cx.rain.mc.catmessenger.message;

public class MessageColor {

    public static final MessageColor BLACK = new MessageColor("black");
    public static final MessageColor DARK_BLUE = new MessageColor("dark_blue");
    public static final MessageColor DARK_GREEN = new MessageColor("dark_green");
    public static final MessageColor DARK_AQUA = new MessageColor("dark_aqua");
    public static final MessageColor DARK_RED = new MessageColor("dark_red");
    public static final MessageColor DARK_PURPLE = new MessageColor("dark_purple");
    public static final MessageColor GOLD = new MessageColor("gold");
    public static final MessageColor GRAY = new MessageColor("gray");
    public static final MessageColor DARK_GRAY = new MessageColor("dark_gray");
    public static final MessageColor BLUE = new MessageColor("blue");
    public static final MessageColor GREEN = new MessageColor("green");
    public static final MessageColor AQUA = new MessageColor("aqua");
    public static final MessageColor RED = new MessageColor("red");
    public static final MessageColor LIGHT_PURPLE = new MessageColor("light_purple");
    public static final MessageColor YELLOW = new MessageColor("yellow");
    public static final MessageColor WHITE = new MessageColor("white");
    public static final MessageColor RESET = new MessageColor("reset");

    private String name = null;
    private int hex = 0;

    protected MessageColor(String name) {
        this.name = name;
    }

    public MessageColor(int hex) {
        this.hex = hex;
    }

    public boolean isHex() {
        return name != null;
    }

    public String asString() {
        return isHex() ? '#' + Integer.toHexString(hex) : name;
    }

    public static MessageColor fromString(String str) {
        return switch (str) {
            case "black" -> BLACK;
            case "dark_blue" -> DARK_BLUE;
            case "dark_green" -> DARK_GREEN;
            case "dark_aqua" -> DARK_AQUA;
            case "dark_red" -> DARK_RED;
            case "dark_purple" -> DARK_PURPLE;
            case "gold" -> GOLD;
            case "gray" -> GRAY;
            case "dark_gray" -> DARK_GRAY;
            case "blue" -> BLUE;
            case "green" -> GREEN;
            case "aqua" -> AQUA;
            case "red" -> RED;
            case "light_purple" -> LIGHT_PURPLE;
            case "yellow" -> YELLOW;
            case "white" -> WHITE;
            case "reset" -> RESET;
            default -> str.startsWith("#") ? new MessageColor(Integer.parseInt(str.replace("#", ""))) : null;
        };
    }
}
