/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eriol_Eandur.npq_plugin;

import Eriol_Eandur.npq_plugin.Data.InformationData;
import Eriol_Eandur.npq_plugin.conversations.QuestionConversationFactory;
import Eriol_Eandur.npq_plugin.Data.PluginData;
import Eriol_Eandur.npq_plugin.Data.QuestionData;
import Eriol_Eandur.npq_plugin.Data.TeleportationData;
import Eriol_Eandur.npq_plugin.conversations.NPQConversationPrefix;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class NPQPlugin extends JavaPlugin implements Listener{
    
    private QuestionConversationFactory questionFactory;
    
    @Getter
    private static NPQPlugin pluginInstance;
    
    @Override
    public void onEnable() {
        pluginInstance = this;
        PluginData.initPluginData(this);
        getServer().getPluginManager().registerEvents(this, this);
        questionFactory = new QuestionConversationFactory(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if ((sender instanceof ConsoleCommandSender) && cmd.getName().equalsIgnoreCase("reloadnpq")) { 
            PluginData.loadFromFile();
            return true;
        }
        if((sender instanceof Player) && cmd.getName().equalsIgnoreCase("debug")) {
            if(args.length==0){
                PluginData.debugLocations((Player) sender);
            }
            else if (args[0].equalsIgnoreCase("world")) {
                sender.sendMessage("World: "+(((Player)sender).getLocation().getWorld()==PluginData.getWorld() ? "true" : "false"));
            }
            else if (args[0].equalsIgnoreCase("here")) {
                sender.sendMessage("Question "+(PluginData.questionFor(((Player)sender).getLocation()) == null ? "false" : "true"));
                sender.sendMessage("Information "+(PluginData.infoFor(((Player)sender).getLocation()) == null ? "false" : "true"));
                sender.sendMessage("Teleportation "+(PluginData.teleportFor(((Player)sender).getLocation()) == null ? "false" : "true"));
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerMoves(PlayerMoveEvent event){
        QuestionData question = PluginData.questionFor(event.getTo());
        if(question!=null) {
            //    && PluginData.questionFor(event.getFrom())!=question){
            Player player = event.getPlayer();
            if(!PluginData.isPlayerInConversation(player)){
                PluginData.addPlayerToConversation(player);
                questionFactory.startConversation(question, player);
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
            }
        }
    }

}
