/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.utils.MessageUtil;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Eriol_Eandur
 */
public class NPQCommandExecutor implements CommandExecutor {
    
    @Getter
    private final Map <String, AbstractCommand> commands = new LinkedHashMap <>();
    
    public NPQCommandExecutor() {
        addCommandHandler("load", new NPQLoad("newplayerquiz"));
        addCommandHandler("debug", new NPQDebug("newplayerquiz"));
        addCommandHandler("delay", new NPQDelay("newplayerquiz"));
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if(!label.equalsIgnoreCase("npq")) {
            return false;
        }
        if(args == null || args.length == 0) {
            sendNoSubcommandErrorMessage(cs);
            return true;
        }
        if(commands.containsKey(args[0].toLowerCase())) {
            commands.get(args[0].toLowerCase()).handle(cs, Arrays.copyOfRange(args, 1, args.length));
        } else {
            sendSubcommandNotFoundErrorMessage(cs);
        }
        return true;
    }

    private void sendNoSubcommandErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "You're missing subcommand name for this command.");
    }
    
    private void sendSubcommandNotFoundErrorMessage(CommandSender cs) {
        MessageUtil.sendErrorMessage(cs, "Subcommand not found.");
    }
    
    private void addCommandHandler(String name, AbstractCommand handler) {
        commands.put(name, handler);
    }

}
