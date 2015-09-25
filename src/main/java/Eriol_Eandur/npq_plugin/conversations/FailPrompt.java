/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eriol_Eandur.npq_plugin.conversations;

import Eriol_Eandur.npq_plugin.Data.QuestionData;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;

public class FailPrompt extends MessagePrompt{
 
    @Override
    protected Prompt getNextPrompt(ConversationContext cc) {
        return Prompt.END_OF_CONVERSATION;
    }

    @Override
    public String getPromptText(ConversationContext cc) {
        QuestionData data = QuestionConversationFactory.getQuestionData(cc);
        return data.getFailText();
    }

}
