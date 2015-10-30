/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eriol_Eandur.npq_plugin.Data;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

public class QuestionData {
    
    @Getter
    @Setter
    private String questionText, successText, failText;
    
    @Getter
    @Setter
    ArrayList<String> answerTexts;
    
    @Getter
    @Setter
    ArrayList<Boolean> answers;
    
    @Getter
    @Setter
    Location questionLocation, failLocation, successLocation;
    
    @Getter
    @Setter
    private int xSize,zSize;

}
