/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.NewPlayerQuizPlugin;
import com.mcmiddleearth.newPlayerQuiz.PluginData;
import com.mcmiddleearth.pluginutil.message.MessageUtil;
import org.bukkit.command.CommandSender;

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
            NewPlayerQuizPlugin.getPluginInstance().reloadConfig();
            PluginData.load();
            PluginData.getMessageUtil().sendInfoMessage(cs, "Quiz data loaded.");
        }
        catch(Exception e) {
            PluginData.getMessageUtil().sendErrorMessage(cs, "There was an error while loading quizData");
        }
    }
}
