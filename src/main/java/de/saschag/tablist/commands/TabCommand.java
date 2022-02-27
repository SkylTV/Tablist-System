package de.saschag.tablist.commands;

import de.saschag.tablist.cache.Cache;
import de.saschag.tablist.config.FileManager;
import de.saschag.tablist.config.FormatableString;
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
        FileManager.Config configManager = Core.getCore().getConfigManager();
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.reload").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                sender.sendMessage(Core.getCore().getPrefix() + "§aDas Plugin wird neugestartet");
                Core.getCore().reload();
            }else if(args[0].equalsIgnoreCase("list")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.list").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                sender.sendMessage(Core.getCore().getPrefix() + "§6Aktuelle Gruppen:");
                Core.getCore().getCache().getCache().keySet().forEach(string ->sender.sendMessage(Core.getCore().getPrefix() + "§2" + string));
            }else{
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname>- §6Erstelle eine neue Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab info <Groupname> - §6Sehe Informationen der Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab list - §6Sehe alle vorhandenen Gruppen");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> - §6Bearbeite die angegebene groupe mit den Kategorieren <permission|placement|tabprefix|chatprefix>");
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
                configManager.reload();
                configManager.set("groups." + group, null);
                configManager.save();
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
                sender.sendMessage(Core.getCore().getPrefix() + "§6Tablist-Prefix: §r" + new FormatableString(Core.getCore().getCache().getCache().get(group).getTabPrefix()).formatColors());
                sender.sendMessage(Core.getCore().getPrefix() + "§6Chat-Prefix: §r" + new FormatableString(Core.getCore().getCache().getCache().get(group).getChatPrefix()).formatColors());
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
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname>- §6Erstelle eine neue Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab info <Groupname> - §6Sehe Informationen der Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab list - §6Sehe alle vorhandenen Gruppen");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> - §6Bearbeite die angegebene groupe mit den Kategorieren <permission|placement|tabprefix|chatprefix>");
            }
        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("edit")){
                if(!sender.hasPermission(configManager.getString("permissions.permission*").getString()) || !sender.hasPermission(configManager.getString("permissions.editGroup").getString())){
                    sender.sendMessage(Core.getCore().getNoPermission());
                    return false;
                }
                String name = args[1];
                String category = args[2];
                if(!Core.getCore().getCache().getCache().containsKey(name)){
                    sender.sendMessage(Core.getCore().getPrefix() + "§cDiese Gruppe existiert nicht.");
                    return false;
                }
                if(category.equalsIgnoreCase("permission"))
                Core.getCore().getCache().setSetup(sender, 5, args[1]);
                else if(category.equalsIgnoreCase("placement"))
                    Core.getCore().getCache().setSetup(sender, 6, args[1]);
                else if(category.equalsIgnoreCase("tabprefix"))
                    Core.getCore().getCache().setSetup(sender, 7, args[1]);
                else if(category.equalsIgnoreCase("chatprefix"))
                    Core.getCore().getCache().setSetup(sender, 8, args[1]);
                else
                    sender.sendMessage(Core.getCore().getPrefix() + "§cBitte gebe eine richtige Kategorie ein: §e<permission|placement|tabprefix|chatprefix>");
            }else {
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname>- §6Erstelle eine neue Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab info <Groupname> - §6Sehe Informationen der Gruppe");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab list - §6Sehe alle vorhandenen Gruppen");
                sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> - §6Bearbeite die angegebene groupe mit den Kategorieren <permission|placement|tabprefix|chatprefix>");
            }
        }else {
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab create <Groupname>- §6Erstelle eine neue Gruppe");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab remove <Groupname> - §6Entferne eine vorhandene Gruppe");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab info <Groupname> - §6Sehe Informationen der Gruppe");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab reload - §6Lade das Plugin neu");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab list - §6Sehe alle vorhandenen Gruppen");
            sender.sendMessage(Core.getCore().getPrefix() + "§e/Tab edit <Groupname> <Category> - §6Bearbeite die angegebene groupe mit den Kategorieren <permission|placement|tabprefix|chatprefix>");
        }
        return false;
    }
}
