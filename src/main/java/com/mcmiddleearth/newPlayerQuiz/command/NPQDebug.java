/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.PluginData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Eriol_Eandur
 */
class NPQDebug extends AbstractCommand {

    public NPQDebug(String permissionNodes) {
        super(0,false,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        if(args.length==0){
            PluginData.setLogReceiver(null);
            PluginData.getMessageUtil().sendInfoMessage(cs,"All Debug output will be sent to console.");
            return;
        }
        if(cs instanceof Player) {
            if (args[0].equalsIgnoreCase("here")) {
                PluginData.getMessageUtil().sendInfoMessage(cs,"Question "+(PluginData.questionFor(((Player)cs).getLocation()) == null ? "false" : "true"));
                PluginData.getMessageUtil().sendInfoMessage(cs,"Teleportation "+(PluginData.teleportFor(((Player)cs).getLocation()) == null ? "false" : "true"));
                return;
            }
        }
        if (args[0].equalsIgnoreCase("on")) {
            PluginData.setDebug(true);
            PluginData.getMessageUtil().sendInfoMessage(cs,"Debug on.");
            return;
            }
        if (args[0].equalsIgnoreCase("off")) {
            PluginData.setDebug(false);
            PluginData.getMessageUtil().sendInfoMessage(cs,"Debug off.");
            return;
        }
        if(args[0].equalsIgnoreCase("me")) {
            if(cs instanceof Player) {
                PluginData.setLogReceiver((Player)cs);
                PluginData.getMessageUtil().sendInfoMessage(cs,"All Debug output will be sent to you.");
            }
            else {
                PluginData.setLogReceiver(null);
                PluginData.getMessageUtil().sendInfoMessage(cs,"All Debug output will be sent to console.");
            }
            return;
        }
   }

}
