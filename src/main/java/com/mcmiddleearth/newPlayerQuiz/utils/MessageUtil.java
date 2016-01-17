/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.utils;

import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Ivanpl
 */

public class MessageUtil {
    
    private static final String PREFIX   = "[NewPlayerQuiz] ";
    private static final String NOPREFIX = "    ";
    
    public static void sendErrorMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.RED + PREFIX + message);
        } else {
            sender.sendMessage(PREFIX + message);
        }
    }
    
    public static void sendInfoMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.AQUA + PREFIX + message);
        } else {
            sender.sendMessage(PREFIX + message);
        }
    }
    
    public static void sendNoPrefixInfoMessage(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(ChatColor.AQUA + NOPREFIX + message);
        } else {
            sender.sendMessage(NOPREFIX + message);
        }
    }
        
    public static void showTitle(String player, String color, String title, String subtitle) {
        Logger.getGlobal().info("showTitle");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+player+" times 20 20 20");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+player+" subtitle "+"\""+subtitle+"\"");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "title "+player+" title "+"{text:\""+title+"\",color:"+color+"}");
    }
    
}
