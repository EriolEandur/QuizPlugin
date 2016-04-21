/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.conversations;

import com.mcmiddleearth.newPlayerQuiz.data.PluginData;
import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationAbandonedListener;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.InactivityConversationCanceller;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class QuestionConversationFactory implements ConversationAbandonedListener{
    
    public final static int CS_QUESTION = 0,
                            CS_ANSWER = 1,
                            CS_INPUT = 2,
                            CS_SUCCESS = 3,
                     CS_FAIL = 4;
    
    private final ConversationFactory factory;
    
    public QuestionConversationFactory(Plugin plugin){
        factory = new ConversationFactory(plugin)
                .withModality(true)
                .withEscapeSequence("!cancel")
                .withPrefix(new NPQConversationPrefix())
                .withFirstPrompt(new QuestionPrompt())
                .withTimeout(40)
                //.thatExcludesNonPlayersWithMessage("You must be a player to send this command")
                .addConversationAbandonedListener(this);
        
    }
    
    public void startConversation(QuestionData question, Player player){
        Conversation conversation = factory.buildConversation(player);
        ConversationContext context = conversation.getContext();
        context.setSessionData("data", question);
        context.setSessionData("ansIndex", 0);
        context.setSessionData("success", true);
        context.setSessionData("Stat", CS_QUESTION);
        conversation.begin();
    }
    
    @Override
    public void conversationAbandoned(ConversationAbandonedEvent abandonedEvent) {
Logger.getGlobal().info("abandonded");
        if (!(abandonedEvent.getCanceller() instanceof InactivityConversationCanceller)) {
            abandonedEvent.getContext().getForWhom().sendRawMessage(ChatColor.AQUA + "Question cancelled, please step on the question marker again.");
        } else {
            abandonedEvent.getContext().getForWhom().sendRawMessage(ChatColor.AQUA + "Question timed out, please step on the question marker again.");
        }
        PluginData.removePlayerFromConversation((Player) abandonedEvent.getContext().getForWhom());
    }
    
    public static QuestionData getQuestionData(ConversationContext context){
        return (QuestionData) context.getSessionData("data");
    }
    
    public static int getAnswerIndex(ConversationContext context){
        return (Integer) context.getSessionData("ansIndex");
    }

}
