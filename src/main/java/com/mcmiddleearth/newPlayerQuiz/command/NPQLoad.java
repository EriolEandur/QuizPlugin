/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.data.PluginData;
import com.mcmiddleearth.newPlayerQuiz.utils.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
class NPQLoad extends AbstractCommand {

    public NPQLoad(String permissionNodes) {
        super(0,false,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        try{
            if(args.length<1) {
                PluginData.loadFromFile(null);
            }
            else {
                PluginData.loadFromFile(args[0]);
            }
        MessageUtil.sendInfoMessage(cs, "Quiz data loaded.");
        }
        catch(Exception e) {
            MessageUtil.sendErrorMessage(cs, "There was an error while loading quizData");
        }
    }
}
