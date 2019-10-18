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
package com.mcmiddleearth.newPlayerQuiz.listener;

import com.mcmiddleearth.connect.Channel;
import com.mcmiddleearth.connect.util.ConnectUtil;
import com.mcmiddleearth.newPlayerQuiz.PluginData;
import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import com.mcmiddleearth.newPlayerQuiz.data.TeleportData;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Eriol_Eandur
 */
public class PlayerListener implements Listener{

    //@EventHandler
    //public void onInteract(PlayerInteractEvent event) {
    //*}
    private Map<UUID,Long> informed = new HashMap<>();
    
    @EventHandler
    public void onPlayerMoves(PlayerMoveEvent event){
        Map<UUID,Long> remove = new HashMap<>();
        informed.forEach((uuid,time) -> {
            if(time+2000<System.currentTimeMillis()) {
                remove.put(uuid,time);
            }});
        remove.forEach((uuid,time)-> informed.remove(uuid,time));
        if(movedWithinBlock(event.getFrom(),event.getTo())) {
            return;
        }
        if(!PluginData.isQuizWorld(event.getTo().getWorld())) {
            if(PluginData.getQuizScoreboard(event.getPlayer())!=null) {
                PluginData.removeQuizScoreboard(event.getPlayer());
            }
            return;
        }//*/
        Player player = event.getPlayer();
        QuestionData question = PluginData.questionFor(event.getPlayer().getLocation());
        Long lastTime = informed.get(player.getUniqueId());
        if(question!=null 
                && (lastTime==null || lastTime+2000<System.currentTimeMillis())) {
            /*if(question.isInside(event.getFrom())) {
                return;
            }*/
            PluginData.clearChat(player);
            question.getQuizMessage().send(player);
            informed.put(player.getUniqueId(), System.currentTimeMillis());
        }
        TeleportData teleport = PluginData.teleportFor(event.getPlayer().getLocation());
        if(teleport!= null) {
            /*if(teleport.isInside(event.getFrom())) {
                return;
            }*/
            //if(teleport!=PluginData.teleportFor(event.getFrom())) {
                String server = teleport.getServer();
                Location target = teleport.getTargetLocation();
                target = PluginData.calculateTargetLocation(target, 
                                    player.getLocation(), 
                                    teleport.isKeepOrientation());
                FancyMessage message = teleport.getMessage();
                if(message!=null) {
                    message.send(player);
                }
                if(server.equals("")) {
                    event.getPlayer().teleport(target,
                                               PlayerTeleportEvent.TeleportCause.PLUGIN);
                    PluginData.log(teleport.getTargetLocation().getWorld().getName());
                    teleport.sendBroadcastMessageAndTitle(player);
                } else {
                    //bungee
                    PluginData.removeQuizScoreboard(player);
                    if(!PluginData.hasFinishedQuiz(player)) {
                        PluginData.setFinishedQuiz(player);
                        ConnectUtil.sendTitle(player, server, player.getName(),
                                teleport.getWelcomeTitle(),teleport.getWelcomeSubtitle(),
                                /*ChatColor.GOLD+"Welcome",ChatColor.GREEN+"to Middle-earth" ,*/ 20,200,20,8000);
                        ConnectUtil.sendMessage(player, server, Channel.ALL, 
                                teleport.getBroadcastMessage().replace("_@p_", player.getName()),
                                /*ChatColor.GOLD+"["+ChatColor.DARK_RED+"Broadcast"+ChatColor.GOLD+"] "
                                +ChatColor.GREEN+player.getName()+ChatColor.BLUE+ChatColor.BOLD
                                +"just joined! Welcome to Middle-earth!",*/15000);
                    }
Logger.getGlobal().info("Teleport "+player.getName()+" to "+server);
                    ConnectUtil.teleportPlayer(player, server, 
                                               "world", 
                                               teleport.getTargetLocation());
                }
            //}
        }
    }

    @EventHandler
    public void onExitNewPlayerWorld(PlayerChangedWorldEvent event) {
        if(PluginData.isQuizWorld(event.getFrom())) {
            if(PluginData.getQuizScoreboard(event.getPlayer())!=null) {
                PluginData.removeQuizScoreboard(event.getPlayer());
            }
        }
    }
    
    @EventHandler
    public void playerQuit(PlayerQuitEvent event) {
        PluginData.removeQuizScoreboard(event.getPlayer());
    }
    
    private boolean movedWithinBlock(Location from, Location to) {
        return from.getWorld().equals(to.getWorld())
                && from.getBlockX() == to.getBlockX()
                && from.getBlockY() == to.getBlockY()
                && from.getBlockZ() == to.getBlockZ();
    }
}
