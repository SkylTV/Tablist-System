package de.saschag.tablist.main;

import de.saschag.tablist.cache.Cache;
import de.saschag.tablist.config.FileManager;
import de.saschag.tablist.utils.Tablist;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

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
    public ChatColor translate(String id){
        HashMap<String, ChatColor> colors = new HashMap<>();
        colors.put("0", ChatColor.BLACK);
        colors.put("1", ChatColor.DARK_BLUE);
        colors.put("2", ChatColor.DARK_GREEN);
        colors.put("3", ChatColor.DARK_AQUA);
        colors.put("4", ChatColor.DARK_RED);
        colors.put("5", ChatColor.DARK_PURPLE);
        colors.put("6", ChatColor.GOLD);
        colors.put("7", ChatColor.GRAY);
        colors.put("8", ChatColor.DARK_GRAY);
        colors.put("9", ChatColor.BLUE);
        colors.put("a", ChatColor.GREEN);
        colors.put("b", ChatColor.AQUA);
        colors.put("c", ChatColor.RED);
        colors.put("d", ChatColor.LIGHT_PURPLE);
        colors.put("e", ChatColor.YELLOW);
        colors.put("f", ChatColor.WHITE);
        return colors.get(id);
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