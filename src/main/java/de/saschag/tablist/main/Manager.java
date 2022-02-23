package de.saschag.tablist.main;

import de.saschag.tablist.commands.TabCommand;
import de.saschag.tablist.listeners.ChatListener;
import de.saschag.tablist.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

public class Manager {

    private final Main main;
    private final PluginManager pluginManager;

    public Manager() {
        main = Core.getCore().getMain();
        pluginManager = Bukkit.getPluginManager();
    }

    public void registerCommands() {
        main.getCommand("tab").setExecutor(new TabCommand());
    }

    public void registerListeners() {
        pluginManager.registerEvents(new ChatListener(), main);
        pluginManager.registerEvents(new JoinListener(), main);
    }
}