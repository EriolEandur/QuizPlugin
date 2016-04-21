/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class LocationData {
    
    @Getter
    @Setter
    private Location location;
    
    @Getter
    @Setter
    private int xSize,zSize;

}
