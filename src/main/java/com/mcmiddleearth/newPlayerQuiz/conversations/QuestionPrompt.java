/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.conversations;

import com.mcmiddleearth.newPlayerQuiz.data.QuestionData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class QuestionPrompt extends MessagePrompt{

    @Override
    protected Prompt getNextPrompt(ConversationContext cc) {
        if(QuestionConversationFactory.getQuestionData(cc).getAnswerTexts().size()>0){
            cc.setSessionData("Stat", QuestionConversationFactory.CS_ANSWER);
            return new AnswerPrompt();
        }
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext cc) {
        QuestionData data = QuestionConversationFactory.getQuestionData(cc);
        return data.getQuestionText();
    }


}