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

import com.mcmiddleearth.newPlayerQuiz.PluginData;
import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import com.mcmiddleearth.newPlayerQuiz.data.TeleportData;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Eriol_Eandur
 */
public class PlayerListener implements Listener{

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
    /*}
    
    @EventHandler
    public void onPlayerMoves(PlayerMoveEvent event){
        if(movedWithinBlock(event.getFrom(),event.getTo())) {
            return;
        }
        if(!PluginData.isQuizWorld(event.getTo().getWorld())) {
            if(PluginData.getQuizScoreboard(event.getPlayer())!=null) {
                PluginData.removeQuizScoreboard(event.getPlayer());
            }
            return;
        }*/
        Player player = event.getPlayer();
        QuestionData question = PluginData.questionFor(event.getPlayer().getLocation());
        if(question!=null) {
            /*if(question.isInside(event.getFrom())) {
                return;
            }*/
            PluginData.clearChat(player);
            question.getQuizMessage().send(player);
        }
        TeleportData teleport = PluginData.teleportFor(event.getPlayer().getLocation());
        if(teleport!= null) {
            /*if(teleport.isInside(event.getFrom())) {
                return;
            }*/
            //if(teleport!=PluginData.teleportFor(event.getFrom())) {
                Location target = teleport.getTargetLocation();
                event.getPlayer().teleport(PluginData.calculateTargetLocation(target, 
                                                                           player.getLocation(), 
                                                                           teleport.isKeepOrientation()),
                                           PlayerTeleportEvent.TeleportCause.PLUGIN);
                PluginData.log(teleport.getTargetLocation().getWorld().getName());
                FancyMessage message = teleport.getMessage();
                if(message!=null) {
                    message.send(player);
                }
                teleport.sendBroadcastMessageAndTitle(player);
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
