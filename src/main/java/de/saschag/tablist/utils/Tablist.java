package de.saschag.tablist.utils;

import de.saschag.tablist.main.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Tablist {

    public static void setTablist(Player player){
        ArrayList<String> group = new ArrayList<>();
        PermissionUser user = PermissionsEx.getUser(player);
        List<String> permissions = user.getPermissions(player.getWorld().getName());
        Core.getCore().getCache().getCache().forEach((name, data) ->{
            if(group.isEmpty()){
                if(data.getPermission() != null){
                    if(permissions.contains(data.getPermission())){
                        group.add(name);
                    }
                }
            }
        });
        Thread thread = new Thread(() -> {
            for(Player target : Bukkit.getOnlinePlayers()){
                    final Scoreboard sb = player.getScoreboard();
                    if(group.isEmpty()){
                        sb.getTeams().forEach(team -> team.removePlayer(player));
                        return;
                    }
                    String team;
                    int place = Core.getCore().getCache().getCache().get(group.get(0)).getWeight();
                    if(place > 100) return;
                    if(place < 10){
                        team = "00" + place + group.get(0);
                    }else if(place > 10 && place < 100) {
                        team = "0" + place + group.get(0);
                    }else{
                        return;
                    }
                    if(sb.getTeam(team) == null){
                        sb.registerNewTeam(team);
                    }
                    Objects.requireNonNull(sb.getTeam(team)).setPrefix(ChatColor.translateAlternateColorCodes('&', Core.getCore().getCache().getCache().get(group.get(0)).getTabPrefix()));
                    String color = ChatColor.getLastColors(Core.getCore().getCache().getCache().get(group.get(0)).getTabPrefix().replace('&', '§'));
                    String unformat = color.replace("§", "");
                    Objects.requireNonNull(sb.getTeam(team)).setColor(Core.getCore().translate(unformat));
                    Objects.requireNonNull(sb.getTeam(team)).addPlayer(player);
                    target.setScoreboard(sb);
            }
            Thread.currentThread().stop();
        });
        thread.start();
    }

    public static void startAutoUpdate(){
        int time = Core.getCore().getConfigManager().get().getInt("config.updatePerSeconds");
        int id;
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Core.getCore().getMain(), new Runnable() {
            @Override
            public void run() {
                for(Player all : Bukkit.getOnlinePlayers())
                    setTablist(all);
            }
        }, time*20L, time*20L);
        Core.getCore().setId(id);
    }


}
