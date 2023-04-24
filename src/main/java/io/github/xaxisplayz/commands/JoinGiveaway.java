package io.github.xaxisplayz.commands;

import io.github.xaxisplayz.Main;
import io.github.xaxisplayz.manager.LangConfig;
import io.github.xaxisplayz.manager.MessagePath;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinGiveaway implements CommandExecutor {

    private final Main plugin;

    public JoinGiveaway(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player player) {

            LangConfig lang = plugin.getLang();

            if (plugin.getGiveawayManager() == null) {
                player.sendMessage(lang.getString(MessagePath.GIVEAWAY_NO_ONGOING_GIVEAWAYS));
                return true;
            }

            if (plugin.getGiveawayManager().getPlayers().contains(player)) {
                player.sendMessage(lang.getString(MessagePath.GIVEAWAY_ALREADY_ENTERED));
                return true;
            }

            plugin.getGiveawayManager().getPlayers().add(player);
            player.sendMessage(lang.getString(MessagePath.GIVEAWAY_ENTERED, plugin.getGiveawayManager().i / 20));
        }

        return true;
    }
}
