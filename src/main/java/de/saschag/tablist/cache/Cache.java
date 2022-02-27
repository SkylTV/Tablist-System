package de.saschag.tablist.cache;

import de.saschag.tablist.config.FileManager;
import de.saschag.tablist.config.FormatableString;
import de.saschag.tablist.main.Core;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Objects;

public class Cache {

    public FileManager.Config configManager;
    public HashMap<String, Data> cache;
    public HashMap<CommandSender, Setup> setup;
    public Cache(){
        configManager = Core.getCore().getConfigManager();
        cache = new HashMap<>();
        setup = new HashMap<>();
    }

    public void loadGroups(){
        if(configManager.get().getConfigurationSection("groups")== null || Objects.requireNonNull(configManager.get().getConfigurationSection("groups")).getKeys(false).isEmpty())
            return;
        Objects.requireNonNull(configManager.get().getConfigurationSection("groups")).getKeys(false).forEach(string -> cache.put(string, new Data(configManager.getString("groups." + string).getString(), checkWorking(configManager.getString("groups." + string).getString()))));
        Core.getCore().log(Core.getCore().getPrefix() + "§aGroups Cache loaded");
    }

    public boolean checkWorking(String configLine){
        if(!configLine.contains(";")) return false;
        if(configLine.split(";").length != 3) return false;
        for(String line : configLine.split(";")){
            if(line.equalsIgnoreCase(" ") ||line.equalsIgnoreCase(""))return false;
        }
        return true;
    }
    public void saveGroups(){
        configManager.reload();
        configManager.set("groups", null);
        configManager.save();
        if(cache == null || cache.isEmpty())
            return;

        cache.forEach((string, data) ->{
            if(data.getSplit() != null && data.getSplit().length != 0 && data.getPermission() != null && data.getWeight() != 0 && data.getChatPrefix() != null && data.getTabPrefix() != null){
                configManager.set("groups." + string, data.getPermission() + ";" + data.getWeight() + ";" + data.getTabPrefix().replace("§", "&") + ";" + data.getChatPrefix().replace("§", "&"));
                configManager.save();
            }
        });
        Core.getCore().log(Core.getCore().getPrefix() + "§aGroups Cache saved");
    }

    public HashMap<CommandSender, Setup> getSetup() {
        return setup;
    }
    public void setSetup(CommandSender sender, int step, String group){
        if(step > 9) return;
        if(step < 1) return;
        if(!setup.containsKey(sender))
            setup.put(sender, new Setup(group, step));
        setup.get(sender).setSetup(step);
        if(step == 1){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die Permission für die Gruppe §b" + group + "§6 ein. Beispiel: §egroup." + group);
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit 'cancel' abbrechen");
        }else if(step == 2){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die Platzierung in der Tablist für die Gruppe §b" + group + "§6 ein. Beispiel: §e2");
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else if(step == 3){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die Prefix für die Tablist ein für die Gruppe §b" + group + "§6 Beispiel: §e" + group + " §8| ");
            sender.sendMessage(Core.getCore().getPrefix() + "§6Du kannst auch Colorcodes benutzen wie beispielsweise '&7Test'");
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else if(step == 4){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die Prefix für den Chat ein für die Gruppe §b" + group + "§6. Beispiel: §7" + group + " §8| §7%spieler% §8>");
            sender.sendMessage(Core.getCore().getPrefix() + "§6Benutze %spieler% um an dieser stelle den Spielernamen anzeigen zu lassen.");
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else if(step == 5){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die änderung der Permission für die Gruppe §b" + group + "§6 ein. Beispiel: §egroup." + group);
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else if(step == 6){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die änderung der Platzierung für die Gruppe §b" + group + "§6 ein. Beispiel: §e1");
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else if(step == 7){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die änderung der Tablist-Prefix für die Gruppe §b" + group + "§6 ein. Beispiel: §e" + group + " §8| ");
            sender.sendMessage(Core.getCore().getPrefix() + "§6Du kannst auch Colorcodes benutzen wie beispielsweise '&7Test'");
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else if(step == 8){
            sender.sendMessage(Core.getCore().getPrefix() + "§6Gebe nun im Chat die änderung der Chat-Prefix für die Gruppe §b" + group + "§6 ein. Beispiel: §7" + group + " §8| §7%spieler% §8>");
            sender.sendMessage(Core.getCore().getPrefix() + "§6Benutze %spieler% um an dieser stelle den Spielernamen anzeigen zu lassen.");
            sender.sendMessage(Core.getCore().getPrefix() + "§4Du kannst jederzeit im Chat mit 'cancel' abbrechen");
        }else return;
        setup.get(sender).setChat(true);
    }


    public HashMap<String, Data> getCache() {
        return cache;
    }



    public static class Data {
        private String permission;
        private int weight;
        private String tabPrefix;
        private String chatPrefix;
        private String[] split;
        private boolean working;


        public Data(String configLine, boolean working){
            split = configLine.split(";");
            this.working = working;
            if(!configLine.equalsIgnoreCase( "0")){
                permission = split[0];
                weight = Integer.parseInt(split[1]);
                tabPrefix = new FormatableString(split[2]).formatColors();
                chatPrefix = new FormatableString(split[3]).formatColors();
            }
        }



        public void setWorking(boolean working) {
            this.working = working;
        }

        public String getTabPrefix() {
            return tabPrefix;
        }

        public int getWeight() {
            return weight;
        }

        public String getChatPrefix() {
            return chatPrefix;
        }

        public String getPermission() {
            return permission;
        }

        public String[] getSplit() {
            return split;
        }

        public void setTabPrefix(String tabPrefix) {
            this.tabPrefix = tabPrefix;
        }

        public void setChatPrefix(String chatPrefix) {
            this.chatPrefix = chatPrefix;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }

        public void setSplit(String[] split) {
            this.split = split;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }


    }
    public static class Setup {
        private String permission;
        private final String name;
        private int weight;
        private String tabPrefix;
        private String chatPrefix;
        private int setup;
        private boolean chat;

        public Setup(String group, int setup){
            this.setup = setup;
            this.name = group;
            this.chat = false;
        }

        public String getTabPrefix() {
            return tabPrefix;
        }

        public int getWeight() {
            return weight;
        }

        public String getChatPrefix() {
            return chatPrefix;
        }

        public String getPermission() {
            return permission;
        }

        public void setChat(boolean chat) {
            this.chat = chat;
        }

        public boolean isChat() {
            return chat;
        }

        public void setTabPrefix(String tabPrefix) {
            this.tabPrefix = tabPrefix;
        }

        public void setChatPrefix(String chatPrefix) {
            this.chatPrefix = chatPrefix;
        }

        public void setPermission(String permission) {
            this.permission = permission;
        }



        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String getName() {
            return name;
        }


        public int getSetup() {
            return setup;
        }

        public void setSetup(int setup) {
            this.setup = setup;
        }
    }
}
