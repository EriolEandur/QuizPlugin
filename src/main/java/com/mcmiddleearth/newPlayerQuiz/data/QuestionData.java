/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.data;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class QuestionData extends LocationData{
    
    @Getter
    @Setter
    private String questionText, successText, failText;
    
    @Getter
    @Setter
    private ArrayList<String> answerTexts;
    
    @Getter
    @Setter
    private ArrayList<Boolean> answers;
    
    @Getter
    @Setter
    private Location failLocation, successLocation;
    

}
