/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.conversations;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.entity.Player;

public class NPQConversationPrefix implements ConversationPrefix{
    
    @Override
    public String getPrefix(ConversationContext context){
        int status = (Integer) context.getSessionData("Stat");
        String pname = ((Player)context.getForWhom()).getName();
        switch(status){
            case QuestionConversationFactory.CS_ANSWER:
                return ChatColor.GOLD+"[New Player Quiz] ";
            case QuestionConversationFactory.CS_INPUT:
                return ChatColor.GRAY+"<"+pname+"> "+ChatColor.WHITE;
            case QuestionConversationFactory.CS_QUESTION:
                return ChatColor.GOLD+"[New Player Quiz] "+ChatColor.YELLOW;
            case QuestionConversationFactory.CS_SUCCESS:
                return ChatColor.GOLD+"[New Player Quiz] "+ChatColor.GREEN;
            case QuestionConversationFactory.CS_FAIL:
                return ChatColor.GOLD+"[New Player Quiz] "+ChatColor.RED;
            default:
                return "";
        }
    }
}
