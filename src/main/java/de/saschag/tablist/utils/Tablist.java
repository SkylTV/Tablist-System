package de.saschag.tablist.utils;

import de.saschag.tablist.main.Core;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Objects;

public class Tablist {

    public static void setTablist(Player player){
        ArrayList<String> group = new ArrayList<>();
        Core.getCore().getCache().getCache().forEach((name, data) ->{
            if(group.isEmpty()){
                Permission perm = new Permission(data.getPermission(), PermissionDefault.FALSE);
                if(player.hasPermission(perm)){
                    group.add(name);
                }
            }
        });
        Thread thread = new Thread(() -> {
            for(Player target : Bukkit.getOnlinePlayers()){
                if(target != player) return;
                final Scoreboard sb = player.getScoreboard();
                if(group.isEmpty()){
                    sb.getTeams().forEach(team -> team.removePlayer(player));
                    return;
                }
                String team;
                int place = Core.getCore().getCache().getCache().get(group.get(0)).getWeight();
                if(place > 100) return;
                if(place < 10){
                    team = "0" + place + group.get(0);
                }else if(place > 10 && place < 100) {
                    team = "" + place + group.get(0);
                }else{
                    return;
                }
                if(sb.getTeam(team) == null){
                    sb.registerNewTeam(team);
                }
                Objects.requireNonNull(sb.getTeam(team)).setPrefix(ChatColor.translateAlternateColorCodes('&', Core.getCore().getCache().getCache().get(group.get(0)).getTabPrefix()));
                Objects.requireNonNull(sb.getTeam(team)).addPlayer(player);
            }
            for(Player target : Bukkit.getOnlinePlayers()){
                if(target == player) return;
                final Scoreboard sb = player.getScoreboard();
                if(group.isEmpty()){
                    sb.getTeams().forEach(team -> team.removePlayer(player));
                    return;
                }
                String team;
                int place = Core.getCore().getCache().getCache().get(group.get(0)).getWeight();
                if(place > 100) return;
                if(place < 10){
                    team = "0" + place + group.get(0);
                }else if(place > 10 && place < 100) {
                    team = "" + place + group.get(0);
                }else{
                    return;
                }
                if(sb.getTeam(team) == null){
                    sb.registerNewTeam(team);
                }
                Objects.requireNonNull(sb.getTeam(team)).setPrefix(ChatColor.translateAlternateColorCodes('&', Core.getCore().getCache().getCache().get(group.get(0)).getTabPrefix()));

                Objects.requireNonNull(sb.getTeam(team)).addPlayer(player);
            }
            Thread.currentThread().stop();
        });
        thread.start();
    }


}
