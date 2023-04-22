package io.github.xaxisplayz.commands;

import io.github.xaxisplayz.Main;
import io.github.xaxisplayz.manager.GiveawayManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ManualGiveaway implements CommandExecutor {

    private final Main plugin;

    public ManualGiveaway(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("ManualGiveaway").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(commandSender instanceof Player p && p.hasPermission("giveaway.admin")){

            ItemStack item = p.getInventory().getItemInMainHand();

            new GiveawayManager(plugin, item);
        }

        return true;
    }
}
