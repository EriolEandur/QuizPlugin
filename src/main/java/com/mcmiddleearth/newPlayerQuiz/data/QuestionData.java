/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 * NPQplugin is a  player quiz management Bukkit plugin
 *  Copyright (C) 2016 MCME
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package com.mcmiddleearth.newPlayerQuiz.data;

import com.mcmiddleearth.newPlayerQuiz.PluginData;
import com.mcmiddleearth.pluginutil.message.FancyMessage;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

/**
 *
 * @author Eriol_Eandur
 */
public class QuestionData extends LocationData{
    
    @Getter
    private FancyMessage quizMessage,retryMessage,successMessage,failMessage;
    
    @Getter
    private Location retryTarget, successTarget, failTarget;
    
    @Getter
    @Setter
    private boolean retryKeepOrientation, successKeepOrientation, failKeepOrientation, 
                    resetSuccessChances, resetRetryChances, resetFailChances;
    
    @Getter
    @Setter
    private String placeName, answerCode;
    
    @Getter
    @Setter
    private int nextStepSuccess, nextStepRetry, nextStepFail;
    
    
    public void setSuccessTarget(String data) {
        successTarget = locationFromString(data);
    }

    public void setRetryTarget(String data) {
        retryTarget = locationFromString(data);
    }

    public void setFailTarget(String data) {
        failTarget = locationFromString(data);
    }
    
    public void setSuccessMessage(ConfigurationSection data) {
        successMessage = messageFromConfig(data, PluginData.getSuccessColor());
    }

    public void setFailMessage(ConfigurationSection data) {
        failMessage = messageFromConfig(data,PluginData.getFailColor());
    }

    public void setRetryMessage(ConfigurationSection data) {
        retryMessage = messageFromConfig(data,PluginData.getRetryColor());
    }

    public void setQuizMessage(ConfigurationSection data) {
        quizMessage = messageFromConfig(data,PluginData.getQuestionColor());
    }

}
