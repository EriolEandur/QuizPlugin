/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 * NPQplugin is a  player quiz management Bukkit plugin
 *  Copyright (C) 2015
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
public class TeleportData extends LocationData{
    
    @Getter
    private Location targetLocation;
    
    @Setter
    @Getter
    private boolean keepOrientation;
    
    @Getter
    private FancyMessage message;
    
    @Getter
    private String server;
    
    public void setTargetLocation(String data) {
        targetLocation = locationFromString(data);
    }
        
    public void setMessage(ConfigurationSection data) {
        message = messageFromConfig(data, PluginData.getTeleportColor());
    }

    public void setTargetServer(String server) {
        this.server = server;
    }

    
}
