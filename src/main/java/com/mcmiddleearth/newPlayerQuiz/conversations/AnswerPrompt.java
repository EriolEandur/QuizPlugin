/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.conversations;

import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.conversations.BooleanPrompt;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

public class AnswerPrompt extends BooleanPrompt{

    @Override
    protected Prompt acceptValidatedInput(ConversationContext cc, boolean answer) {
        QuestionData data = QuestionConversationFactory .getQuestionData(cc);
        int ansIndex = QuestionConversationFactory.getAnswerIndex(cc);
        if(data.getAnswers().get(ansIndex)!=answer){
            cc.setSessionData("success", false);
        }
        ansIndex++;
        if(ansIndex<data.getAnswers().size()){
            cc.setSessionData("ansIndex", ansIndex);
            cc.setSessionData("Stat", QuestionConversationFactory.CS_ANSWER);
            return new AnswerPrompt();
        }
        else {
            Player player = (Player) cc.getForWhom();
            if((Boolean) cc.getSessionData("success")){
Logger.getGlobal().info("Success teleport");
                cc.setSessionData("Stat", QuestionConversationFactory.CS_SUCCESS);
                player.teleport(getOrientedLocation(data.getSuccessLocation(),player));
                return new SuccessPrompt();
            }
            else {
Logger.getGlobal().info("Fail teleport");
                cc.setSessionData("Stat", QuestionConversationFactory.CS_FAIL);
                player.teleport(getOrientedLocation(data.getFailLocation(),player));
                return new FailPrompt();
            }
        }
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext cc, String answer) {
        if(answer.equalsIgnoreCase("true") || answer.equalsIgnoreCase("yes"))
            return acceptValidatedInput(cc, true);
        else
            return acceptValidatedInput(cc, false);
    }

    @Override
    public String getPromptText(ConversationContext cc) {
        cc.setSessionData("Stat", QuestionConversationFactory.CS_INPUT);
        return QuestionConversationFactory.getQuestionData(cc).getAnswerTexts()
                .get(QuestionConversationFactory.getAnswerIndex(cc));
    }
    
    @Override
    protected boolean isInputValid(ConversationContext context, String answer){
        return answer.equalsIgnoreCase("no") 
            || answer.equalsIgnoreCase("yes")
            || answer.equalsIgnoreCase("false")
            || answer.equalsIgnoreCase("true");
    }
    
    @Override
    protected String getFailedValidationText(ConversationContext context, String invalidInput){
        context.setSessionData("Stat", QuestionConversationFactory.CS_ANSWER);
        return ChatColor.GOLD+"[New Player Quiz] "+ ChatColor.RED
                +"Type in 'yes' or 'true' if the answer is correct, otherwise type 'no' or 'false'.";
    }
    
    private Location getOrientedLocation(Location loc, Player player){
        Location location = loc.clone();
        location.setPitch(player.getLocation().getPitch());
        location.setYaw(player.getLocation().getYaw());
        return location;
    }
}
