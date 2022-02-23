package de.saschag.tablist.main;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Core.getCore().enableCore();

    }

    @Override
    public void onLoad() {
        Core.setCore(new Core(this));
    }

    @Override
    public void onDisable() {
        Core.getCore().disableCore();
    }
}
