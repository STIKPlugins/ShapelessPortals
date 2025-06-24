package me.stokmenn.shapelessportals;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShapelessPortals extends JavaPlugin {

    @Override
    public void onEnable() {
        Config.init(this);

        Bukkit.getPluginManager().registerEvents(new IgnitePortalListener(this), this);
        getCommand("shapelessportals").setExecutor(new ReloadCommand(this));
    }
}
