package de.saschag.tablist.main;

import de.saschag.tablist.cache.Cache;
import de.saschag.tablist.config.FileManager;
import de.saschag.tablist.utils.Tablist;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormatSymbols;

public class Core {

    public static Core core;
    private final Main main;
    private final Manager manager;
    private String prefix;
    private String noPermission;
    private final FileManager.Config configManager;
    private  Cache cache;
    private int id;

    public Core(Main main) {
        core = this;
        this.main = main;
        this.manager = new Manager();
        configManager = new FileManager(main).getConfig("config.yml");
        if(!new File(main.getDataFolder() + "/config.yml").exists()){
            configManager.copyDefaults(true).saveDefaultConfig();
        }
    }

    public void enableCore() {
        manager.registerListeners();
        manager.registerCommands();
        this.prefix = configManager.getString("messages.prefix").formatColors();
        this.noPermission = configManager.getString("messages.nopermission").format();
        this.cache = new Cache();
        cache.loadGroups();
        if(configManager.get().getBoolean("config.autoUpdate"))
        Tablist.startAutoUpdate();
    }

    public int getId() {
        return id;
    }

    public void reload(){
        configManager.reload();
        cache.saveGroups();
        cache.loadGroups();
        this.prefix = configManager.getString("messages.prefix").formatColors();
        this.noPermission = configManager.getString("messages.nopermission").format();
        for(Player all : Bukkit.getOnlinePlayers()){
            Tablist.setTablist(all);
        }
        if(configManager.get().getBoolean("config.autoUpdate"))
            Tablist.startAutoUpdate();
        else
            Bukkit.getScheduler().cancelTask(Core.getCore().getId());
    }

    public void setId(int id) {
        this.id = id;
    }

    public void disableCore(){
        cache.saveGroups();
    }


    public Main getMain() {
        return main;
    }


    public void log(final String message){
        Bukkit.getConsoleSender().sendMessage(message);
    }


    public String getNoPermission() {
        return noPermission;
    }

    public Cache getCache() {
        return cache;
    }
    public boolean isStringNumeric( String str )
    {
        DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
        char localeMinusSign = currentLocaleSymbols.getMinusSign();

        if ( !Character.isDigit( str.charAt( 0 ) ) && str.charAt( 0 ) != localeMinusSign ) return false;

        boolean isDecimalSeparatorFound = false;
        char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

        for ( char c : str.substring( 1 ).toCharArray() )
        {
            if ( !Character.isDigit( c ) )
            {
                if ( c == localeDecimalSeparator && !isDecimalSeparatorFound )
                {
                    isDecimalSeparatorFound = true;
                    continue;
                }
                return false;
            }
        }
        return true;
    }

    public String getPrefix() {
        return prefix;
    }

    public FileManager.Config getConfigManager() {
        return configManager;
    }

    public static Core getCore() {
        return core;
    }

    public static void setCore(Core core) {
        Core.core = core;
    }
}