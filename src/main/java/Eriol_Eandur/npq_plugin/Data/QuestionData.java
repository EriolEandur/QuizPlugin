/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eriol_Eandur.npq_plugin.Data;

import java.util.ArrayList;
//import lombok.Getter;
//import lombok.Setter;
import org.bukkit.Location;

public class QuestionData {
    
    //@Getter
    //@Setter
    private String questionText, successText, failText;
    
    //@Getter
    //@Setter
    ArrayList<String> answerTexts;
    
    //@Getter
    //@Setter
    ArrayList<Boolean> answers;
    
    //@Getter
    //@Setter
    Location questionLocation, failLocation, successLocation;
    
    //@Getter
    //@Setter
    private int xSize,zSize;

    public String getQuestionText() {
        return questionText;
    }

    public String getSuccessText() {
        return successText;
    }

    public String getFailText() {
        return failText;
    }

    public ArrayList<String> getAnswerTexts() {
        return answerTexts;
    }

    public ArrayList<Boolean> getAnswers() {
        return answers;
    }

    public Location getQuestionLocation() {
        return questionLocation;
    }

    public Location getFailLocation() {
        return failLocation;
    }

    public Location getSuccessLocation() {
        return successLocation;
    }

    public int getXSize() {
        return xSize;
    }

    public int getZSize() {
        return zSize;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setSuccessText(String successText) {
        this.successText = successText;
    }

    public void setFailText(String failText) {
        this.failText = failText;
    }

    public void setAnswerTexts(ArrayList<String> answerTexts) {
        this.answerTexts = answerTexts;
    }

    public void setAnswers(ArrayList<Boolean> answers) {
        this.answers = answers;
    }

    public void setQuestionLocation(Location questionLocation) {
        this.questionLocation = questionLocation;
    }

    public void setFailLocation(Location failLocation) {
        this.failLocation = failLocation;
    }

    public void setSuccessLocation(Location successLocation) {
        this.successLocation = successLocation;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
    }

    public void setZSize(int zSize) {
        this.zSize = zSize;
    }
    
    
}
