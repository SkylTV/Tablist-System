package de.saschag.tablist.main;

import de.saschag.tablist.cache.Cache;
import de.saschag.tablist.config.ConfigManager;
import org.bukkit.Bukkit;

import java.text.DecimalFormatSymbols;

public class Core {

    public static Core core;
    private final Main main;
    private final Manager manager;
    private final String prefix;
    private final String noPermission;
    private final ConfigManager configManager;
    private final Cache cache;

    public Core(Main main) {
        core = this;
        this.main = main;
        this.manager = new Manager();
        configManager = new ConfigManager(main);
        configManager.copyDefaults();
        this.prefix = configManager.getString("messages.prefix").formatColors();
        this.noPermission = configManager.getString("messages.noPermission").format();
        this.cache = new Cache();
    }

    public void enableCore() {
        manager.registerListeners();
        manager.registerCommands();
        cache.loadGroups();
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

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public static Core getCore() {
        return core;
    }

    public static void setCore(Core core) {
        Core.core = core;
    }
}