package com.p000ison.dev.blockregionblock;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author p000ison
 */
public class BlockRegionBlock extends JavaPlugin implements Listener {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private SettingsManager settingsManager;

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        settingsManager = new SettingsManager(this);


        if (getWorldGuard() == null) {
            logger.log(Level.SEVERE, "WorldGuard not found! Disabling...");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    public WorldGuardPlugin getWorldGuard() {
        Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }

        return (WorldGuardPlugin) plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock() == null || event.getPlayer() == null || "null".equals(getRegionAtLoc(event.getBlock().getLocation()))) {
            return;
        }
        if (isBlocked(event.getBlock(), getRegionAtLoc(event.getBlock().getLocation()))) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Du darfst hier nicht abbaun!");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlock() == null || event.getPlayer() == null || "null".equals(getRegionAtLoc(event.getBlock().getLocation()))) {
            return;
        }
        if (isBlocked(event.getBlock(), getRegionAtLoc(event.getBlock().getLocation()))) {
            if (!event.getPlayer().isOp()) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Du darfst hier nichts setzen!");
            }
        }
    }

    public boolean isInRegion(String region, Block block) {
        Vector vec = new Vector(block.getX(), block.getY(), block.getZ());
        return this.getWorldGuard().getRegionManager(block.getWorld()).getRegion(region).contains(vec);
    }

    public boolean isBlocked(Block block, String region) {
        if (this.getConfig().getIntegerList(region).contains(block.getTypeId()) && isInRegion(region, block)) {
            return true;
        }
        return false;
    }

    public String getRegionAtLoc(Location loc) {
        ApplicableRegionSet set = getWorldGuard().getRegionManager(loc.getWorld()).getApplicableRegions(new Vector(loc.getX(), loc.getY(), loc.getZ()));
        for (ProtectedRegion each : set) {
            return each.getId();
        }
        return "null";
    }

    /**
     * @return the settingsManager
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
