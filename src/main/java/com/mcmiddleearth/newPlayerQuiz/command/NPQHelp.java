/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 * NPQplugin is a  player quiz management Bukkit plugin
 *  Copyright (C) 2015 MCME
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
package com.mcmiddleearth.newPlayerQuiz.command;

import com.mcmiddleearth.newPlayerQuiz.NewPlayerQuizPlugin;
import com.mcmiddleearth.newPlayerQuiz.PluginData;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Eriol_Eandur
 */
public class NPQHelp  extends AbstractCommand {

    public NPQHelp(String permissionNodes) {
        super(0,false,permissionNodes);
    }

    @Override
    protected void execute(CommandSender cs, String... args) {
        try{
            NewPlayerQuizPlugin.getPluginInstance().reloadConfig();
            PluginData.load();
            PluginData.getMessageUtil().sendInfoMessage(cs, "Quiz data loaded.");
        }
        catch(Exception e) {
            PluginData.getMessageUtil().sendErrorMessage(cs, "There was an error while loading quizData");
        }
    }
}
