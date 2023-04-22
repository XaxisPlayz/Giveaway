package io.github.xaxisplayz.commands;

import io.github.xaxisplayz.Main;
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

        if(sender instanceof Player player){

            if(plugin.getGiveawayManager() == null){
                player.sendMessage(Main.colorize("&4There is no ongoing giveaways right now!"));
                return true;
            }

            if(plugin.getGiveawayManager().getPlayers().contains(player)) {
                player.sendMessage(Main.colorize("&4You are already in this giveaway!"));
                return true;
            }
            plugin.getGiveawayManager().getPlayers().add(player);
            player.sendMessage(Main.colorize("&aYou have entered the giveaway. Winners are announced in "+plugin.getGiveawayManager().i + " second(s)"));
        }

        return true;
    }
}
