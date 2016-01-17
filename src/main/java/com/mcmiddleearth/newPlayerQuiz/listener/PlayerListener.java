/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 * NPQplugin is a  player quiz management Bukkit plugin
 *  Copyright (C) 2015
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

import com.mcmiddleearth.newPlayerQuiz.NewPlayerQuizPlugin;
import com.mcmiddleearth.newPlayerQuiz.data.InformationData;
import com.mcmiddleearth.newPlayerQuiz.data.PluginData;
import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import com.mcmiddleearth.newPlayerQuiz.data.TeleportationData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Eriol_Eandur
 */
public class PlayerListener implements Listener{

        @EventHandler
    public void onPlayerMoves(PlayerMoveEvent event){
        QuestionData question = PluginData.questionFor(event.getTo());
        if(question!=null) {
            Player player = event.getPlayer();
            if(!PluginData.isPlayerInConversation(player)){
                PluginData.addPlayerToConversation(player);
                PluginData.getQuestionFactory().startConversation(question, player);
            }
        }
        InformationData infoTo = PluginData.infoFor(event.getTo());
        if(infoTo!= null) {
            if(infoTo!=PluginData.infoFor(event.getFrom())) {
                event.getPlayer().sendMessage(ChatColor.GOLD+"[New Player Quiz] "+ChatColor.YELLOW
                                             +infoTo.getInfoText());
            }
        }
        TeleportationData teleport = PluginData.teleportFor(event.getTo());
        if(teleport!= null) {
            if(teleport!=PluginData.teleportFor(event.getFrom())) {
                event.getPlayer().teleport(teleport.getTargetLocation(), 
                                           PlayerTeleportEvent.TeleportCause.PLUGIN);
                PluginData.log(teleport.getTargetLocation().getWorld().getName());
                final String newPlayerName = event.getPlayer().getName();
                final String welcome = teleport.getWelcomeMessage();
                if(welcome != null) {
                    BukkitRunnable welcomeTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            int playerIndex = welcome.indexOf("@p");
                            String send = welcome.substring(0, playerIndex)+newPlayerName+welcome.substring(playerIndex+2);
                            Bukkit.broadcastMessage(send);
                    }};
                    welcomeTask.runTaskLater(NewPlayerQuizPlugin.getPluginInstance(), PluginData.getWelcomeDelay());
                }
            }
        }
    }

}
