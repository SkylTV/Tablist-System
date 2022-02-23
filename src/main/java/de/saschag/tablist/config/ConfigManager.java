package de.saschag.tablist.config;

import de.saschag.tablist.main.Main;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class ConfigManager {

    private final Main main;
    private final FileConfiguration fileConfiguration;

    public ConfigManager(final Main main){
        this.main = main;
        this.fileConfiguration = main.getConfig();
    }

    public ConfigManager copyDefaults(){
        fileConfiguration.options().copyDefaults(true);
        save();
        return this;
    }

    public void save(){

        main.saveConfig();
    }

    public FileConfiguration getFileConfiguration(){
        return fileConfiguration;
    }

    public FormatableString getString(final String path){
        return new FormatableString(fileConfiguration.getString(path));
    }

    public Integer getInt(final String path){
        return fileConfiguration.getInt(path);
    }

    public Boolean getBoolean(final String path){
        return fileConfiguration.getBoolean(path);
    }

    public Double getDouble(final String path){
        return fileConfiguration.getDouble(path);
    }

    public List<String> getStringList(final String path){
        return fileConfiguration.getStringList(path);
    }

    public ItemStack getItemStack(final String path){
        return fileConfiguration.getItemStack(path);
    }

    public void setString(final String path, final String value){
        fileConfiguration.set(path, value);
        save();
    }

    public void setInt(final String path, final Integer value){
        fileConfiguration.set(path, value);
        save();
    }

    public void setBoolean(final String path, final Boolean value){
        fileConfiguration.set(path, value);
        save();
    }

    public void setDouble(final String path, final Double value){
        fileConfiguration.set(path, value);
        save();
    }

    public void setStringList(final String path, final List<String> value){
        fileConfiguration.set(path, value);
        save();
    }

    public void setItemStack(final String path, final ItemStack value){
        fileConfiguration.set(path, value);
        save();
    }
    public void setLocation(final String path, Location location){
        setDouble(path + ".x", location.getX());
        setDouble(path + ".y", location.getY());
        setDouble(path + ".z", location.getZ());
        setInt(path + ".yaw", (int)location.getYaw());
        setInt(path + ".pitch", (int)location.getPitch());
        setString(path + ".world", Objects.requireNonNull(location.getWorld()).getName());
        save();
    }



    public boolean exist(final String path){
        return fileConfiguration.isSet(path);
    }


}
