package com.p000ison.dev.blockregionblock;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author p000ison
 */
public final class SettingsManager {

    private BlockRegionBlock plugin;
    private File main;
    private FileConfiguration config;

    /**
     *
     */
    public SettingsManager(BlockRegionBlock plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
        main = new File(getPlugin().getDataFolder() + File.separator + "config.yml");
        load();
    }

    /**
     * Load the configuration
     */
    public void load() {
        boolean exists = (main).exists();

        if (exists) {
            try {
                getConfig().options().copyDefaults(true);
                getConfig().load(main);
            } catch (Exception ex) {
                plugin.getLogger().log(Level.WARNING, "Loading the config failed!:{0}", ex.getMessage());
            }
        } else {
            getConfig().options().copyDefaults(true);

        }
        
        save();
    }

    public void save() {
        try {
            getConfig().save(main);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Saving the config failed!:{0}", ex.getMessage());
        }
    }

    /**
     * @return the plugin
     */
    public BlockRegionBlock getPlugin() {
        return plugin;
    }

    /**
     * @return the config
     */
    public FileConfiguration getConfig() {
        return config;
    }
}