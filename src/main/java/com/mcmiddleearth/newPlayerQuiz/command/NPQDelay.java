/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.PluginData;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Eriol_Eandur
 */
class NPQDelay extends AbstractCommand {

    public NPQDelay(String permissionNodes) {
        super(1,true,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        try {
            PluginData.setDefaultBroadcastDelay(Integer.parseInt(args[0]));
            sendDelayMessage(cs);
        }
        catch(NumberFormatException e) {
            PluginData.getMessageUtil().sendErrorMessage(cs,"Invalid number format, try: /npq delay #welcomeDelay");
        }
    }

    private void sendDelayMessage(CommandSender cs) {
        PluginData.getMessageUtil().sendInfoMessage(cs,"Delay for welcome broadcast set.");
    }

}
