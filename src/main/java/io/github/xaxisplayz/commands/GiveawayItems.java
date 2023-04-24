package io.github.xaxisplayz.commands;

import io.github.xaxisplayz.Main;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveawayItems implements CommandExecutor {

    private final Main plugin;

    public GiveawayItems(Main plugin) {
        this.plugin = plugin;
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player p && p.hasPermission("giveaway.admin")) {
            Inventory inv = Bukkit.createInventory(null, 6 * 9, Main.GUI_TITLE);
            List<ItemStack> items = plugin.getConfigItems();
            if(!items.isEmpty()) {
                for (ItemStack item : items) {
                    inv.addItem(item);
                }
            }
            p.openInventory(inv);
        }
        return true;
    }
}
