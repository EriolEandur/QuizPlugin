/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz;

import com.mcmiddleearth.newPlayerQuiz.command.NPQCommandExecutor;
import com.mcmiddleearth.newPlayerQuiz.conversations.QuestionConversationFactory;
import com.mcmiddleearth.newPlayerQuiz.data.PluginData;
import com.mcmiddleearth.newPlayerQuiz.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class NewPlayerQuizPlugin extends JavaPlugin{
    
    @Getter
    private static NewPlayerQuizPlugin pluginInstance;
    
    @Override
    public void onEnable() {
        pluginInstance = this;
        PluginData.initPluginData(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        PluginData.setQuestionFactory(new QuestionConversationFactory(this));
        getCommand("npq").setExecutor(new NPQCommandExecutor());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	sender.sendMessage(cmd.getName());
        if((sender instanceof Player) && cmd.getName().equalsIgnoreCase("debugnpq")) {
            if(args.length==0){
                PluginData.debugLocations((Player) sender);
            }
            else if (args[0].equalsIgnoreCase("world")) {
                sender.sendMessage("World: "+(((Player)sender).getLocation().getWorld()==PluginData.getWorld() ? "true" : "false"));
                return true;
            }
            else if (args[0].equalsIgnoreCase("here")) {
                sender.sendMessage("Question "+(PluginData.questionFor(((Player)sender).getLocation()) == null ? "false" : "true"));
                sender.sendMessage("Information "+(PluginData.infoFor(((Player)sender).getLocation()) == null ? "false" : "true"));
                sender.sendMessage("Teleportation "+(PluginData.teleportFor(((Player)sender).getLocation()) == null ? "false" : "true"));
                return true;
            }
            else if (args[0].equalsIgnoreCase("delay") && args.length>1) {
                try {
                    PluginData.setWelcomeDelay(Integer.parseInt(args[1]));
                }
                catch(NumberFormatException e) {
                    sender.sendMessage("Invalid number format, try: /debugnpq delay #welcomeDelay");
                }
                return true;
            }
        }
        return false;
    }

}
