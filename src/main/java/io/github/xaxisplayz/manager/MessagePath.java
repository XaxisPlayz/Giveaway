package io.github.xaxisplayz.manager;

public enum MessagePath {
    GIVEAWAY_STARTED("messages.giveaway.started", "&aA giveaway has started! &7&nClick here to enter!"),
    GIVEAWAY_ENDING_SOON("messages.giveaway.ending_soon", "&aGiveaway ends in 10 seconds! &7&nClick here to enter!"),
    GIVEAWAY_NO_ONGOING_GIVEAWAYS("messages.giveaway.no_ongoing_giveaways", "&4There is no ongoing giveaways right now!"),
    GIVEAWAY_ALREADY_ENTERED("messages.giveaway.already_entered", "&4You are already in this giveaway!"),
    GIVEAWAY_ENTERED("messages.giveaway.entered", "&aYou have entered the giveaway. Winners are announced in %d second(s)"),
    GUI_TITLE("messages.gui.gui_title","&aGiveaway Items!"),
    SUCCESS_SET_ITEMS("messages.success_set_items", "&aSuccessfully set the items!"),
    INVENTORY_FULL("messages.player_inventory_full","&4Your inventory was full so another winner will be chosen!"),
    GIVEAWAY_WON("messages.giveaway.giveaway_won","&aCongratulations! You have won the giveaway!"),
    GIVEAWAY_COUNTDOWN_TIME("system.giveaway.count_down_time", "400"),
    GIVEAWAY_COOLDOWN_TIME("system.giveaway.cooldown_time", "14400000"),
    ;
    private final String path;
    private final String defaultValue;

    MessagePath(String path, String defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
