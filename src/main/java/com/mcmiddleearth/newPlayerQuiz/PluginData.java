/*
 * NPQplugin is a  player quiz management Bukkit plugin
 *  Copyright (C) 2016 MCME
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.mcmiddleearth.newPlayerQuiz;

import com.mcmiddleearth.newPlayerQuiz.data.LocationData;
import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import com.mcmiddleearth.newPlayerQuiz.data.TeleportData;
import com.mcmiddleearth.newPlayerQuiz.scoreboard.QuizScoreboard;
import com.mcmiddleearth.pluginutil.NumericUtil;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Eriol_Eandur
 */
public class PluginData {
    
    @Getter
    private static final MessageUtil messageUtil = new MessageUtil();
    
    private static final Set<UUID> finishedQuizPlayers = new HashSet<>();
    
    @Getter
    private static final List<World> quizWorlds = new ArrayList<>();
    
    private static final List<QuestionData> quizPlaces = new ArrayList<>();
    
    private static final List<TeleportData> teleportPlaces = new ArrayList<>();
    
    private static final Map<UUID, Integer> chancesLeft = new HashMap<>();
    
    private static final Map<UUID, QuizScoreboard> scoreboards = new HashMap<>();
    
    private static int numberOfSecondChances, numberOfSteps;
    
    //Dirty: same quiz area box is used for all worlds.
    private static final int[] quizAreaBox = new int[6];

    @Getter
    private static ChatColor questionColor, retryColor, failColor, teleportColor, successColor;
    
    @Setter
    private static boolean debug = false;
    
    @Setter
    private static Player logReceiver = null;
    
    @Setter
    private static int defaultBroadcastDelay=100;
    
    private static final int numberOfChatLines = 30;
    
    private static final File finishedPlayerFile = new File(NewPlayerQuizPlugin.getPluginInstance().getDataFolder(),
                                                            "finishedPlayerList.uid");
   
    static {
        if(!finishedPlayerFile.exists()) {
            try {
                finishedPlayerFile.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static void load() {
        loadFinishedPlayers();
        FileConfiguration config = NewPlayerQuizPlugin.getPluginInstance().getConfig();
        messageUtil.setPluginName("NewPlayerQuiz");
        numberOfSecondChances = config.getInt("numberOfSecondChances",2);
        numberOfSteps = config.getInt("numberOfSteps",7);
        String[] boxTemp = config.getString("quizAreaBox","-1367 -1325 29 59 1137 1334").split(" ");
        for(int i = 0; i< boxTemp.length; i++) {
            quizAreaBox[i] = NumericUtil.getInt(boxTemp[i]);
        }
        List<String> worldNames = config.getStringList("worlds");
        for(String name : worldNames) {
            World world = Bukkit.getWorld(name);
            if(world!=null) {
                quizWorlds.add(world);
            }
        }
        questionColor = getColor(config.getString("questionColor","§e"));
        successColor = getColor(config.getString("successColor","§a"));
        retryColor = getColor(config.getString("retryColor","§c"));
        failColor = getColor(config.getString("failColor","§4"));
        teleportColor = getColor(config.getString("teleportColor","§e"));
        List teleportPlacesConfig 
                = config.getList("teleportPlaces", new ArrayList<ConfigurationSection>());
        teleportPlaces.clear();
        for(Object object: teleportPlacesConfig) {
            MemoryConfiguration tempConfig = new MemoryConfiguration();
            tempConfig.createSection("data",(LinkedHashMap<String,Object>) object);
            ConfigurationSection teleportPlaceConfig = tempConfig.getConfigurationSection("data");
            TeleportData data = new TeleportData();
            data.setLocation(teleportPlaceConfig.getString("location",""));
            data.setSize(teleportPlaceConfig.getString("size",""));
            data.setMessage(teleportPlaceConfig);
            data.setTargetLocation(teleportPlaceConfig.getString("target",""));
            data.setTargetServer(teleportPlaceConfig.getString("server",""));
            data.setKeepOrientation(teleportPlaceConfig.getBoolean("keepOrientation",true));
            setBroadcastAndTitle(data, teleportPlaceConfig);
            teleportPlaces.add(data);
        }
        List quizPlacesConfig 
                = config.getList("quizPlaces", new ArrayList<LinkedHashMap<String,Object>>());
        quizPlaces.clear();
        for(Object object: quizPlacesConfig) {
            MemoryConfiguration tempConfig = new MemoryConfiguration();
            tempConfig.createSection("data",(LinkedHashMap<String,Object>) object);
            ConfigurationSection quizPlaceConfig = tempConfig.getConfigurationSection("data");
            QuestionData data = new QuestionData();
            data.setLocation(quizPlaceConfig.getString("location",""));
            data.setSize(quizPlaceConfig.getString("size",""));
            data.setPlaceName(quizPlaceConfig.getString("name",""));
            data.setAnswerCode(quizPlaceConfig.getString("correct",""));
            setBroadcastAndTitle(data, quizPlaceConfig);
            data.setQuizMessage(quizPlaceConfig);
           ConfigurationSection subConfig = quizPlaceConfig.getConfigurationSection("success");
            if(subConfig!=null) {
                data.setResetSuccessChances(subConfig.getBoolean("resetChances", false));
                data.setSuccessKeepOrientation(subConfig.getBoolean("keepOrientation",true));
                data.setNextStepSuccess(subConfig.getInt("nextStep",0));
                data.setSuccessTarget(subConfig.getString("target"));
                data.setSuccessMessage(subConfig);
            }

            subConfig = quizPlaceConfig.getConfigurationSection("retry");
            if(subConfig!=null) {
                data.setResetRetryChances(subConfig.getBoolean("resetChances", false));
                data.setRetryKeepOrientation(subConfig.getBoolean("keepOrientation",true));
                data.setNextStepRetry(subConfig.getInt("nextStep",0));
                data.setRetryTarget(subConfig.getString("target"));
                data.setRetryMessage(subConfig);
            }
            
            subConfig = quizPlaceConfig.getConfigurationSection("fail");
            if(subConfig!=null) {
                data.setResetFailChances(subConfig.getBoolean("resetChances", true));
                data.setFailKeepOrientation(subConfig.getBoolean("keepOrientation",true));
                data.setNextStepFail(subConfig.getInt("nextStep",1));
                data.setFailTarget(subConfig.getString("target"));
                data.setFailMessage(subConfig);
            }
            
            quizPlaces.add(data);
        }
    }
    
    private static ChatColor getColor(String colorCode) {
        ChatColor color = ChatColor.getByChar(colorCode.charAt(1));
        if(color!=null) {
            return color;
        } 
        return ChatColor.WHITE;
    }
    
    private static void setBroadcastAndTitle(LocationData data, ConfigurationSection config) {
        data.setBroadcastMessage(config.getString("broadcast",""));
        data.setBroadcastDelay(config.getInt("broadcastDelay",defaultBroadcastDelay));
        data.setWelcomeTitle(config.getString("welcomeTitle",""));
        data.setWelcomeSubtitle(config.getString("welcomeSubtitle",""));
    }
 
    public static boolean isInQuizArea(Player player) {
        Location loc = player.getLocation();
        return quizAreaBox[0]<loc.getX() && quizAreaBox[1]>loc.getX()
            && quizAreaBox[1]<loc.getY() && quizAreaBox[3]>loc.getY()     
            && quizAreaBox[4]<loc.getZ() && quizAreaBox[5]>loc.getZ();     
    }
    
    public static QuestionData questionFor(Location location) {
        return (QuestionData) dataFor(location, quizPlaces);
    }
 
    public static TeleportData teleportFor(Location location) {
        return (TeleportData) dataFor(location, teleportPlaces);
    }
 
    private static LocationData dataFor(Location location, List locationData){
        for(Object dataObject: locationData){
            LocationData data = (LocationData) dataObject;
            Location qLoc = data.getLocation();
            if(data.isInside(location)) {
                if(data instanceof TeleportData) {
                    //log("found target world: "+((TeleportData)data).getTargetLocation().getWorld().getName());
                }
                return data;
            }
        }
        return null;
    }
    
    public static QuestionData getQuestionData(String name) {
        for(QuestionData data: quizPlaces) {
            if(data.getPlaceName().equals(name)) {
                return data;
            }
        }
        return null;
    }
    
    public static boolean hasAnotherTry(Player player) {
        return getSecondChances(player)>0;
    }
    
    public static void wrongAnswer(Player player) {
        if(!chancesLeft.containsKey(player.getUniqueId())) {
            resetChances(player);
        }
        chancesLeft.put(player.getUniqueId(), chancesLeft.get(player.getUniqueId())-1);
    }
    
    public static int getSecondChances(Player player) {
        if(!chancesLeft.containsKey(player.getUniqueId())) {
            resetChances(player);
        }
        return chancesLeft.get(player.getUniqueId());
    }
    
    public static void resetChances(Player player) {
        chancesLeft.put(player.getUniqueId(), numberOfSecondChances);
    }
    
    public static boolean hasFinishedQuiz(Player player) {
        return finishedQuizPlayers.contains(player.getUniqueId());
    }
    
    public static void setFinishedQuiz(Player player) {
        chancesLeft.remove(player.getUniqueId());
        if(!hasFinishedQuiz(player)) {
            final UUID uuid = player.getUniqueId();
            finishedQuizPlayers.add(uuid);
            new BukkitRunnable() {
                @Override
                public void run() {
                    try(FileWriter fw = new FileWriter(finishedPlayerFile,true);
                        PrintWriter writer = new PrintWriter(fw)) {
                        writer.println(uuid);
                    } catch (IOException ex) {
                        Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }.runTaskAsynchronously(NewPlayerQuizPlugin.getPluginInstance());
        }
    }
    
    private static void loadFinishedPlayers() {
        try(Scanner scanner = new Scanner(finishedPlayerFile)) {
            finishedQuizPlayers.clear();
            while(scanner.hasNext()) {
                finishedQuizPlayers.add(UUID.fromString(scanner.next()));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Location calculateTargetLocation(Location target, Location playerLocation, boolean keepOrientation) {
        Location result = target.clone();
        result.setX(target.getBlockX()+0.5);
        result.setY(target.getBlockY()+0.5);
        result.setZ(target.getBlockZ()+0.5);
        if(keepOrientation){
            result.setPitch(playerLocation.getPitch());
            result.setYaw(playerLocation.getYaw());
        }
        return result;
    }

    public static void clearChat(Player player) {
        String message = "";
        for(int i=0; i<numberOfChatLines;i++) {
            message = message + ChatColor.DARK_GRAY+"_\n";
        }
        player.sendMessage(message);
    }
    
    public static void showQuizScoreboard(Player player) {
        scoreboards.put(player.getUniqueId(), new QuizScoreboard(player,numberOfSecondChances, numberOfSteps));
    }
    
    public static void removeQuizScoreboard(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        scoreboards.remove(player.getUniqueId());
    }
    
    public static QuizScoreboard getQuizScoreboard(Player player) {
        return scoreboards.get(player.getUniqueId());
    }
    
    public static boolean hasQuizScoreboard(Player player) {
        return scoreboards.containsKey(player.getUniqueId());
    }
    public static boolean isQuizWorld(World world) {
        return quizWorlds.contains(world);
    }
    
    public static void log(String info){
        if(debug)
            if(logReceiver==null) {
                NewPlayerQuizPlugin.getPluginInstance().getLogger().info(info);
            }
            else {
                logReceiver.sendMessage("."+info+".");
            }
    }
    


}
