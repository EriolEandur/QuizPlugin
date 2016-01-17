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
class NPQDebug extends AbstractCommand {

    public NPQDebug(String permissionNodes) {
        super(0,false,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        if(cs instanceof Player) {
            if(args.length==0){
                PluginData.debugLocations((Player) cs);
                return;
            }
            if (args[0].equalsIgnoreCase("world")) {
                MessageUtil.sendInfoMessage(cs,"World: "+(((Player)cs).getLocation().getWorld()==PluginData.getWorld() ? "true" : "false"));
                return;
            }
            if (args[0].equalsIgnoreCase("here")) {
                MessageUtil.sendInfoMessage(cs,"Question "+(PluginData.questionFor(((Player)cs).getLocation()) == null ? "false" : "true"));
                MessageUtil.sendInfoMessage(cs,"Information "+(PluginData.infoFor(((Player)cs).getLocation()) == null ? "false" : "true"));
                MessageUtil.sendInfoMessage(cs,"Teleportation "+(PluginData.teleportFor(((Player)cs).getLocation()) == null ? "false" : "true"));
                return;
            }
        }
        if (args[0].equalsIgnoreCase("on")) {
            PluginData.setDebug(true);
            MessageUtil.sendInfoMessage(cs,"Debug on.");
            return;
            }
        if (args[0].equalsIgnoreCase("off")) {
            PluginData.setDebug(false);
            MessageUtil.sendInfoMessage(cs,"Debug off.");
            return;
        }
        if(args[0].equalsIgnoreCase("me")) {
            if(cs instanceof Player) {
                PluginData.setLogReceiver((Player)cs);
                MessageUtil.sendInfoMessage(cs,"All Debug output will be sent to you.");
            }
            else {
                PluginData.setLogReceiver(null);
                MessageUtil.sendInfoMessage(cs,"All Debug output will be sent to console.");
            }
        }
    }

}
