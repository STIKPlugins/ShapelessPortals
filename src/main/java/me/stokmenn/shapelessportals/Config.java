package me.stokmenn.shapelessportals;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private static JavaPlugin plugin;
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    public static boolean onlyPlayerCanIgnite;

    public static boolean isPermissionRequired;
    public static String permission;

    public static int maxWidth;
    public static int maxHeight;

    public static int minPortalSize;

    public static final Set<Material> portalFrameMaterials = new HashSet<>();

    public static Component noPermissionToReload;
    public static Component successfullyReloaded;

    public static void init(JavaPlugin plugin) {
        Config.plugin = plugin;
        plugin.saveDefaultConfig();
        reload();
    }

    public static void reload() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();

        onlyPlayerCanIgnite = config.getBoolean("onlyPlayerCanIgnite", false);

        isPermissionRequired = config.getBoolean("isPermissionRequired", false);
        permission = config.getString("permission", "");

        maxWidth = config.getInt("maxWidth", 21);
        maxHeight = config.getInt("maxHeight", 21);

        minPortalSize = config.getInt("minPortalSize", 1);

        portalFrameMaterials.clear();
        for (String materialName : config.getStringList("portalFrameMaterials")) {
            Material material = Material.getMaterial(materialName);

            if (material == null) {
                plugin.getLogger().warning("Unknown material: " + materialName);
            } else {
                portalFrameMaterials.add(material);
            }
        }

        noPermissionToReload = miniMessage.deserialize(config.getString(
                "noPermissionToReload", "<red>✘ <white>You don't have permission!"));
        successfullyReloaded = miniMessage.deserialize(config.getString(
                "successfullyReloaded", "<green>✔ <white>Config reloaded!"));
    }
}