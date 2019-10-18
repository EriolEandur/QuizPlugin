/*
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
package com.mcmiddleearth.newPlayerQuiz;

import com.mcmiddleearth.newPlayerQuiz.command.NPQCommandExecutor;
import com.mcmiddleearth.newPlayerQuiz.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Eriol_Eandur
 */
public class NewPlayerQuizPlugin extends JavaPlugin{
    
    @Getter
    private static NewPlayerQuizPlugin pluginInstance;
    
    @Override
    public void onEnable() {
        pluginInstance = this;
        PluginData.load();
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getCommand("npq").setExecutor(new NPQCommandExecutor());
    }

}
