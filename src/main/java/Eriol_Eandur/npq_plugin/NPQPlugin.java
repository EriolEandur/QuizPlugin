/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eriol_Eandur.npq_plugin;

import Eriol_Eandur.npq_plugin.conversations.QuestionConversationFactory;
import Eriol_Eandur.npq_plugin.Data.PluginData;
import Eriol_Eandur.npq_plugin.Data.QuestionData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Ivan1pl
 */
public class NPQPlugin extends JavaPlugin implements Listener{
    
    private QuestionConversationFactory questionFactory;
    
    @Override
    public void onEnable() {
        PluginData.initPluginData(this);
        getServer().getPluginManager().registerEvents(this, this);
        questionFactory = new QuestionConversationFactory(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("reloadnpq")) { 
            PluginData.loadFromFile();
            return true;
        }
        return false;
    }
    

    @EventHandler
    public void onPlayerMoves(PlayerMoveEvent event){
        QuestionData question = PluginData.questionFor(event.getTo());
        if(question!=null 
                && PluginData.questionFor(event.getFrom())!=question){
            Player player = event.getPlayer();
            if(!PluginData.isPlayerInConversation(player)){
                PluginData.addPlayerToConversation(player);
                questionFactory.startConversation(question, player);
            }
        }
    }

}