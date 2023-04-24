package io.github.xaxisplayz.commands;

import io.github.xaxisplayz.Main;
import io.github.xaxisplayz.config.CustomConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinGiveaway implements CommandExecutor {

    private final Main plugin;

    public JoinGiveaway(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("JoinGiveaway").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            CustomConfig lang = plugin.getLang();

            if (plugin.getGiveawayManager() == null) {
                player.sendMessage(lang.getString(MessagePath.NO_ONGOING_GIVEAWAY));
                return true;
            }

            if (plugin.getGiveawayManager().getPlayers().contains(player)) {
                player.sendMessage(lang.getString(MessagePath.ALREADY_IN_GIVEAWAY));
                return true;
            }

            plugin.getGiveawayManager().getPlayers().add(player);
            player.sendMessage(lang.getString(MessagePath.ENTERED_GIVEAWAY, plugin.getGiveawayManager().i));
        }

        return true;
    }
}
