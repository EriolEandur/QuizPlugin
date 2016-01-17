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
class NPQDelay extends AbstractCommand {

    public NPQDelay(String permissionNodes) {
        super(1,true,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        try {
            PluginData.setWelcomeDelay(Integer.parseInt(args[0]));
            sendDelayMessage(cs);
        }
        catch(NumberFormatException e) {
            MessageUtil.sendErrorMessage(cs,"Invalid number format, try: /npq delay #welcomeDelay");
        }
    }

    private void sendDelayMessage(CommandSender cs) {
        MessageUtil.sendInfoMessage(cs,"Delay for welcome broadcast set.");
    }

}
