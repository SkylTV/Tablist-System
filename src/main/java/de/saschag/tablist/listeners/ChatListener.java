package de.saschag.tablist.listeners;

import de.saschag.tablist.cache.Cache;
import de.saschag.tablist.main.Core;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import java.util.ArrayList;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String message = event.getMessage();
        if(!Core.getCore().getCache().getSetup().containsKey(player)) return;
        if(!Core.getCore().getCache().getSetup().get(player).isChat()) return;
        Cache.Setup setup = Core.getCore().getCache().getSetup().get(player);
        if(setup.getSetup() == 0)return;
        event.setCancelled(true);
        if(message.contains("cancel")){
            Core.getCore().getCache().getCache().remove(setup.getName());
            Core.getCore().getCache().getSetup().remove(player);
            player.sendMessage(Core.getCore().getPrefix() + "§cSetup erfolgreich abgebrochen");
        }
        if(setup.getSetup() == 1){
            if(message.equalsIgnoreCase("") || message.equalsIgnoreCase(" ")) {
                player.sendMessage(Core.getCore().getPrefix() + "§cBitte gebe eine richtige Permission ein");
                return;
            }
            setup.setPermission(message);
            player.sendMessage(Core.getCore().getPrefix() + "§aPermission §e" + message + " §aerfolgreich gesetzt.");
            Core.getCore().getCache().setSetup(player, 2, setup.getName());
        } else if(setup.getSetup() == 2){
            if(message.equalsIgnoreCase("") || message.equalsIgnoreCase(" ") || !Core.getCore().isStringNumeric(message)) {
                player.sendMessage(Core.getCore().getPrefix() + "§cBitte gebe eine richtige Zahl an");
                return;
            }
            setup.setWeight(Integer.parseInt(message));
            player.sendMessage(Core.getCore().getPrefix() + "§aPlatzierung §e" + message + " §aerfolgreich gesetzt.");
            Core.getCore().getCache().setSetup(player, 3, setup.getName());
        }else if(setup.getSetup() == 3){
            if(message.equalsIgnoreCase("") || message.equalsIgnoreCase(" ")) {
                player.sendMessage(Core.getCore().getPrefix() + "§cGebe eine richtige Prefix ein");
                return;
            }
            setup.setTabPrefix(message);
            player.sendMessage(Core.getCore().getPrefix() + "§aPlatzierung §e" + message + " §aerfolgreich gesetzt.");
            Core.getCore().getCache().setSetup(player, 4, setup.getName());
        }else if(setup.getSetup() == 4){
            if(message.equalsIgnoreCase("") || message.equalsIgnoreCase(" ")) {
                player.sendMessage(Core.getCore().getPrefix() + "§cGebe eine richtige Prefix ein");
                return;
            }
            setup.setChatPrefix(message);
            player.sendMessage(Core.getCore().getPrefix() + "§aPlatzierung §e" + message + " §aerfolgreich gesetzt.");
            Core.getCore().getCache().getCache().get(setup.getName()).setPermission(setup.getPermission());
            Core.getCore().getCache().getCache().get(setup.getName()).setWeight(setup.getWeight());
            Core.getCore().getCache().getCache().get(setup.getName()).setTabPrefix(setup.getTabPrefix());
            Core.getCore().getCache().getCache().get(setup.getName()).setChatPrefix(setup.getChatPrefix());
            Core.getCore().getCache().getCache().get(setup.getName()).setWorking(true);
            Core.getCore().getCache().getCache().get(setup.getName()).setSplit((setup.getPermission() +";" + setup.getWeight() +";" + setup.getTabPrefix() +";" + setup.getChatPrefix() +"").split(";"));
            Core.getCore().getCache().getSetup().remove(player);
            player.sendMessage(Core.getCore().getPrefix() + "§aSetup erfolgreich beendet.");
        }
    }

    @EventHandler
    public void onPrefixChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        ArrayList<String> group = new ArrayList<>();
        Core.getCore().getCache().getCache().forEach((name, data) ->{
            if(group.isEmpty()){
                Permission perm = new Permission(data.getPermission(), PermissionDefault.FALSE);
                if(player.hasPermission(perm)){
                    group.add(name);
                }
            }
        });
        if(group.isEmpty()) return;
        String prefix = ChatColor.translateAlternateColorCodes('&', Core.getCore().getCache().getCache().get(group.get(0)).getChatPrefix().replace("%spieler%", "%s")) + event.getMessage();
        event.setFormat(prefix);
    }

}
