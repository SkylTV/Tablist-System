package de.saschag.tablist.commands;

import de.saschag.tablist.cache.Cache;
import de.saschag.tablist.config.ConfigManager;
import de.saschag.tablist.main.Core;
import de.saschag.tablist.utils.Tablist;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TabCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ConfigManager configManager = Core.getCore().getConfigManager();
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.reload").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                sender.sendMessage(Core.getCore().getPrefix() + "§aDas Plugin wird neugestartet");
                Core.getCore().log(Core.getCore().getPrefix() + "§eReloading Plugin");
                Core.getCore().getCache().loadGroups();
                for(Player all : Bukkit.getOnlinePlayers()){
                    Tablist.setTablist(all);
                }
            }else if(args[0].equalsIgnoreCase("save")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.save").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                sender.sendMessage(Core.getCore().getPrefix() + "§aCache wird gespeichert");
                Core.getCore().log(Core.getCore().getPrefix() + "§eSaving Plugin-Cache");
                Core.getCore().getCache().saveGroups();
            }else{
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname>- §6Erstelle eine neue Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab save - §6Sicher den Cache des Plugins");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> <Replacement> - §6Bearbeite die angegebene groupe mit den Kategorieren <permission|placement|tabprefix|chatprefix>");
            }
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("remove")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.removeGroup").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                String group = args[1];
                if(!Core.getCore().getCache().getCache().containsKey(group)){
                    sender.sendMessage(Core.getCore().getPrefix() + "§cDiese Gruppe existiert nicht.");
                    return false;
                }
                Core.getCore().getCache().getCache().remove(group);
                sender.sendMessage(Core.getCore().getPrefix() + "§aGruppe erfolgreich entfernt.");
            }else if(args[0].equalsIgnoreCase("info")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.info").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                String group = args[1];
                if(!Core.getCore().getCache().getCache().containsKey(group)){
                    sender.sendMessage(Core.getCore().getPrefix() + "§cDiese Gruppe existiert nicht.");
                    return false;
                }
                sender.sendMessage(Core.getCore().getPrefix() + "§6Gruppenname: §b" + group);
                sender.sendMessage(Core.getCore().getPrefix() + "§6Permission: §b" + Core.getCore().getCache().getCache().get(group).getPermission());
                sender.sendMessage(Core.getCore().getPrefix() + "§6Platzierung: §b" + Core.getCore().getCache().getCache().get(group).getWeight());
                sender.sendMessage(Core.getCore().getPrefix() + "§6Tablist-Prefix: §r" + Core.getCore().getCache().getCache().get(group).getTabPrefix());
                sender.sendMessage(Core.getCore().getPrefix() + "§6Chat-Prefix: §r" + Core.getCore().getCache().getCache().get(group).getChatPrefix());
            }else if(args[0].equalsIgnoreCase("create")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.createGroup").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                if(Core.getCore().getCache().getCache().containsKey(args[1])){
                    sender.sendMessage(Core.getCore().getPrefix() + "§cDiese Gruppe existiert bereits!");
                    return false;
                }
                Core.getCore().getCache().getCache().put(args[1], new Cache.Data("0", false));
                sender.sendMessage(Core.getCore().getPrefix() + "§aStarte Setup");
                Core.getCore().getCache().setSetup(sender, 1, args[1]);
            }else {
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname> - §6Erstelle eine neue Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab save - §6Sicher den Cache des Plugins");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> <Replacement> - §6Bearbeite die angegebene Gruppe mit den Kategorien <permission|placement|tabprefix|chatprefix>");
            }
        }else if(args.length == 4){
            if(args[0].equalsIgnoreCase("edit")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.editGroup").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                String name = args[1];
                String category = args[2];
                String entry = args[3];
                if(!Core.getCore().getCache().getCache().containsKey(name)){
                    sender.sendMessage(Core.getCore().getPrefix() + "§cDiese Gruppe existiert nicht.");
                    return false;
                }
                if(category.equalsIgnoreCase("permission")){
                   Core.getCore().getCache().getCache().get(name).setPermission(entry);
                   sender.sendMessage(Core.getCore().getPrefix() + "§aPermission für §6" + name + "§a wurde erfolgreich auf §6" + entry + "§a aktuellisiert");
                   return false;
                }else if(category.equalsIgnoreCase("placement")){
                    int weight;
                    try {
                        weight = Integer.parseInt(entry);
                    }catch (NumberFormatException e){
                        sender.sendMessage(Core.getCore().getPrefix() + "§cBitte gebe eine richtige Platzierung an!");
                        return false;
                    }
                    Core.getCore().getCache().getCache().get(name).setWeight(weight);
                    sender.sendMessage(Core.getCore().getPrefix() + "§aPlatzierung für §6" + name + "§a wurde erfolgreich auf §6" + weight + "§a aktuellisiert");
                    return false;
                }else if(category.equalsIgnoreCase("chatprefix")){
                    Core.getCore().getCache().getCache().get(name).setChatPrefix(entry);
                    sender.sendMessage(Core.getCore().getPrefix() + "§aChat-Prefix für §6" + name + "§a wurde erfolgreich auf §6" + entry + "§a aktuellisiert");
                    return false;
                }else if(category.equalsIgnoreCase("tabprefix")){
                    Core.getCore().getCache().getCache().get(name).setTabPrefix(entry);
                    sender.sendMessage(Core.getCore().getPrefix() + "§aTablist-Prefix für §6" + name + "§a wurde erfolgreich auf §6" + entry + "§a aktuellisiert");
                    return false;
                }else {
                    sender.sendMessage(Core.getCore().getPrefix() + "§7Gebe eine richtige Kategorie ein §6<permission|placement|chatprefix|tabprefix>");
                    return false;
                }
            }else {
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname> - §6Erstelle eine neue Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab save - §6Sicher den Cache des Plugins");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> <Replacement> - §6Bearbeite die angegebene Gruppe mit den Kategorien <permission|placement|tabprefix|chatprefix>");
            }
        }else {
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname> - §6Erstelle eine neue Gruppe");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab save - §6Sicher den Cache des Plugins");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> <Replacement> - §6Bearbeite die angegebene groupe mit den Kategorieren <permission|placement|tabprefix|chatprefix>");
        }
        return false;
    }
}
