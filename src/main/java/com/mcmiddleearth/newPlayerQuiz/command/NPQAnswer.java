/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.PluginData;
import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 *
 * @author Eriol_Eandur
 */
class NPQAnswer extends AbstractCommand {

    public NPQAnswer(String permissionNodes) {
        super(0,true,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        if(args.length<2) {
            return;
        }
        Player player = (Player) cs;
        QuestionData data = PluginData.getQuestionData(args[0]);
        if(data==null) {
            return;
        }
        PluginData.clearChat(player);
        if(!PluginData.hasQuizScoreboard(player)) {
            PluginData.showQuizScoreboard(player);
        }
        if(data.getAnswerCode().equals(args[1])) {
            player.teleport(PluginData.calculateTargetLocation(data.getSuccessTarget(), 
                                                 player.getLocation(), 
                                                 data.isSuccessKeepOrientation()),
                            PlayerTeleportEvent.TeleportCause.PLUGIN);
            if(data.getNextStepSuccess()!=0) {
                PluginData.getQuizScoreboard(player).setCurrentStep(data.getNextStepSuccess());
            } else {
                PluginData.getQuizScoreboard(player).nextStep();
            }
            PluginData.log(data.getSuccessTarget().getWorld().getName());
            data.getSuccessMessage().send(player);
            data.sendBroadcastMessageAndTitle(player);
            if(data.isResetSuccessChances()) {
                PluginData.resetChances(player);
            }
        } else if(PluginData.hasAnotherTry(player)) {
            PluginData.wrongAnswer(player);
            player.teleport(PluginData.calculateTargetLocation(data.getRetryTarget(), 
                                                 player.getLocation(), 
                                                 data.isRetryKeepOrientation()),
                            PlayerTeleportEvent.TeleportCause.PLUGIN);
            if(data.getNextStepRetry()!=0 && PluginData.hasQuizScoreboard(player)) {
                PluginData.getQuizScoreboard(player).setCurrentStep(data.getNextStepRetry());
            }
            PluginData.log(data.getRetryTarget().getWorld().getName());
            data.getRetryMessage().send(player);
            if(data.isResetRetryChances()) {
                PluginData.resetChances(player);
            }
        } else {
            player.teleport(PluginData.calculateTargetLocation(data.getFailTarget(), 
                                                 player.getLocation(), 
                                                 data.isFailKeepOrientation()),
                            PlayerTeleportEvent.TeleportCause.PLUGIN);
            data.getFailMessage().send(player);
            if(PluginData.hasQuizScoreboard(player)) {
                PluginData.getQuizScoreboard(player).setCurrentStep(data.getNextStepFail());
            }
            PluginData.log(data.getFailTarget().getWorld().getName());
            if(data.isResetFailChances()) {
                PluginData.resetChances(player);
            }
        }
        if(PluginData.hasQuizScoreboard(player)) {
            PluginData.getQuizScoreboard(player).setSecondChances(PluginData.getSecondChances(player));
        }
    }
    
}
