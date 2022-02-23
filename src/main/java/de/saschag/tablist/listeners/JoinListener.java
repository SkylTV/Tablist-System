package de.saschag.tablist.listeners;


import de.saschag.tablist.utils.Tablist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Tablist.setTablist(event.getPlayer());
    }

}
