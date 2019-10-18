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
package com.mcmiddleearth.newPlayerQuiz.scoreboard;

import java.util.logging.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 *
 * @author Eriol_Eandur
 */
public class QuizScoreboard {
    
    @Getter
    protected final Scoreboard scoreboard;
    
    private final Objective quizStepObjective;
    
    private final Score secondChancesScore;
    
    private int currentStep, numberOfSteps;
    
    private final String displayName = ChatColor.GOLD+"Quiz - Step (";
    
    public QuizScoreboard(Player player, int secondChances, int steps) {
        currentStep = 1;
        numberOfSteps = steps;
        scoreboard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        quizStepObjective = scoreboard.registerNewObjective("QuizStep", "dummy");
        setDisplayName();
        secondChancesScore = quizStepObjective.getScore(ChatColor.YELLOW+"Second chances:");
        secondChancesScore.setScore(secondChances);
        quizStepObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }
    
    public void nextStep() {
        currentStep++;
        setDisplayName();
    }
    
    private void setDisplayName() {
        quizStepObjective.setDisplayName(displayName+currentStep+"/"+numberOfSteps+")");
    }
    
    public void setCurrentStep(int step) {
        currentStep = step;
        setDisplayName();
    }
    
    public void setSecondChances(int chances) {
        secondChancesScore.setScore(chances);
    }
    
}
