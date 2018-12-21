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
package com.mcmiddleearth.newPlayerQuiz.data;

import com.mcmiddleearth.newPlayerQuiz.NewPlayerQuizPlugin;
import com.mcmiddleearth.newPlayerQuiz.PluginData;
import com.mcmiddleearth.pluginutil.TitleUtil;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import com.mcmiddleearth.pluginutil.message.MessageType;
import com.mcmiddleearth.pluginutil.message.config.FancyMessageConfigUtil;
import com.mcmiddleearth.pluginutil.message.config.MessageParseException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Eriol_Eandur
 */
public abstract class LocationData {
    
    @Getter
    private Location location;
    
    @Getter
    private int xSize, ySize, zSize;
    
    public void setSize(int[] size) {
        try{
            xSize = size[0];
            ySize = size[1];
            zSize = size[2];
        } catch(Exception e) {}
    }
    
    @Getter
    @Setter
    private int broadcastDelay;
    
    @Getter
    @Setter
    private String broadcastMessage, welcomeTitle, welcomeSubtitle;
    
    
    public void setLocation(String data) {
        location = locationFromString(data);
    }
    
    public void setSize(String data) {
        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(" ");
        try{
            xSize = scanner.nextInt();
            ySize = scanner.nextInt();
            zSize = scanner.nextInt();
        } catch(Exception e) {
            xSize = 1;
            ySize = 2;
            zSize = 3;
        }
    }
    
    protected static Location locationFromString(String data) {
        Scanner scanner = new Scanner(data);
        scanner.useDelimiter(" ");
        try{
            Location loc = new Location(Bukkit.getWorld(scanner.next()),
                                scanner.nextLong(),
                                scanner.nextLong(),
                                scanner.nextLong());
            try{
                loc.setYaw(scanner.nextInt());
                loc.setPitch(scanner.nextInt());
                return loc;
            } catch(Exception e) {
                return loc;
            }
        } catch(Exception e) {
            return Bukkit.getWorlds().get(0).getSpawnLocation();
        }
    }
    
    public boolean isInside(Location location){
        Location qLoc = this.location;
        return location.getWorld().equals(qLoc.getWorld())
               && isInside(location.getBlockX(),qLoc.getBlockX(),this.getXSize())
               && isInside(location.getBlockY(),qLoc.getBlockY(),this.getYSize())
               && isInside(location.getBlockZ(),qLoc.getBlockZ(),this.getZSize());
    }
    
    private static boolean isInside(int pBlock, int qBlock, int qSize){
        return qBlock-qSize<pBlock && qBlock+qSize>pBlock;
    }

    public void sendBroadcastMessageAndTitle(final Player player) {
        if(!broadcastMessage.equals("")) {
            PluginData.removeQuizScoreboard(player);
            if(!PluginData.hasFinishedQuiz(player)) {
                PluginData.setFinishedQuiz(player);
                TitleUtil.showTitle(player, welcomeTitle, welcomeSubtitle, 20, 200, 20);
                BukkitRunnable welcomeTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        int playerIndex = broadcastMessage.indexOf("_@p_");
                        String send = broadcastMessage.substring(0, playerIndex)
                                      + player.getName()
                                      + broadcastMessage.substring(playerIndex+4);
                        Bukkit.broadcastMessage(send);
                }};
                welcomeTask.runTaskLater(NewPlayerQuizPlugin.getPluginInstance(), broadcastDelay);
            }
        }
    }
    
    protected FancyMessage messageFromConfig(ConfigurationSection data, ChatColor baseColor) {
        try {
            FancyMessage message = FancyMessageConfigUtil.newFromConfig(data, PluginData.getMessageUtil(), MessageType.WHITE, baseColor);
            message.setRunDirect();
            return message;
        } catch (MessageParseException ex) {
            Logger.getLogger(QuestionData.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
            FancyMessage message = new FancyMessage(MessageType.WHITE,PluginData.getMessageUtil());
            message.addSimple("Error while reading from config.");
            return message;
        }
    }



}
