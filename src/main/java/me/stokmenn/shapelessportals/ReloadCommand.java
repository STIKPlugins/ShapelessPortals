package me.stokmenn.shapelessportals;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String @NotNull [] args) {
        if (!sender.hasPermission("shapelessPortals.reload")) {
            sender.sendMessage(Config.noPermissionToReload);
            return false;
        }
        Bukkit.getAsyncScheduler().runNow(plugin, task -> Config.reload());

        sender.sendMessage(Config.successfullyReloaded);
        return true;
    }
}
