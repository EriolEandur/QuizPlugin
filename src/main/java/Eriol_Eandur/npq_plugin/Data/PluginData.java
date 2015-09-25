/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Eriol_Eandur.npq_plugin.Data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PluginData {
    
    private static Set<QuestionData> questions;
    
    private static World world;
    
    private static Set<Player> playerInConversation;

    private static Plugin plugin;
    
   
    public static void initPluginData(Plugin pplugin){
        plugin = pplugin;
        questions = new HashSet<QuestionData>();
        playerInConversation = new HashSet<Player>();
        loadFromFile();
    }
    
    public static void addPlayerToConversation(Player player){
        playerInConversation.add(player);
    }
    
    public static void removePlayerFromConversation(Player player){
        playerInConversation.remove(player);
    }
    
    public static boolean isPlayerInConversation(Player player){
        return playerInConversation.contains(player);
    }
    
    public static QuestionData questionFor(Location location){
        if(world==location.getWorld()){
            for(QuestionData question: questions){
                Location qLoc = question.getQuestionLocation();
                if(isInside(location.getBlockX(),qLoc.getBlockX(),question.getXSize())
                   && isInside(location.getBlockZ(),qLoc.getBlockZ(),question.getZSize())){
                    return question;
                }
            }
        }
        return null;
    }
 
    private static boolean isInside(int pBlock, int qBlock, int qSize){
        return qBlock-qSize<pBlock && qBlock+qSize>pBlock;
    }

    public static void loadFromFile(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("NewPlayerQuiz.json"));
            String input = "";
            int test =0;
            while(reader.ready()){
                test++;
                input = input+reader.readLine();
            }
            reader.close();
           JSONObject jObject = (JSONObject) new JSONParser().parse(input);
            String worldName = (String) jObject.get("World");
            world = Bukkit.getWorld(worldName);
            if(world == null){
                world = Bukkit.getWorlds().get(0);
                plugin.getLogger().info("No world found with name "+worldName);
            }
            HashSet<QuestionData> newQuestions = new HashSet<QuestionData>();
            JSONArray jArray = (JSONArray) jObject.get("Questions");
            for(int i = 0; i<jArray.size();i++){
                JSONObject jQuestion = (JSONObject) jArray.get(i);
                QuestionData question = new QuestionData();
                question.setQuestionText(getString(jQuestion,"Question Text"));
                question.setSuccessText(getString(jQuestion,"Success Text"));
                question.setFailText(getString(jQuestion,"Fail Text"));
                question.setXSize(getInteger(jQuestion,"X Size"));
                question.setZSize(getInteger(jQuestion,"Z Size"));
                question.setQuestionLocation(getLocation(jQuestion,"Question Location"));
                question.setSuccessLocation(getLocation(jQuestion,"Success Location"));
                question.setFailLocation(getLocation(jQuestion,"Fail Location"));
                JSONArray jAnswers = (JSONArray) jQuestion.get("Answers");
                ArrayList<Boolean> answers = new ArrayList<Boolean>();
                ArrayList<String> answerTexts = new ArrayList<String>();
                if(jAnswers!=null){
                    for(int j = 0; j<jAnswers.size(); j++){
                        JSONObject jAnswer = (JSONObject) jAnswers.get(j);
                        answers.add(getBoolean(jAnswer,"Correct"));
                        answerTexts.add(getString(jAnswer,"Text"));
                    }
                }
                question.setAnswerTexts(answerTexts);
                question.setAnswers(answers);
                newQuestions.add(question);
            }
            questions = newQuestions;
        } catch (FileNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
        }

    }
    
    private static boolean getBoolean(JSONObject jObject, String key){
        Object input = jObject.get(key);
        if(input==null){
            plugin.getLogger().info("No Boolean found with key: "+ key);
            return false;
        }
        plugin.getLogger().info("Boolean "+ (Boolean) input+" found with key: "+ key);
        return (Boolean) input;
    }
    
    private static int getInteger(JSONObject jObject, String key){
        Object input = jObject.get(key);
        if(input==null){
            plugin.getLogger().info("No Integer found with key: "+ key);
            return 0;
        }
        plugin.getLogger().info("Integer "+((Long) input).intValue()+" found with key: "+ key);
        return ((Long) input).intValue();
    }
    
    private static String getString(JSONObject jObject, String key){
        Object input = jObject.get(key);
        if(input==null){
            plugin.getLogger().info("No String found with key: "+ key);
            return "";
        }
        plugin.getLogger().info("String "+(String) input+" found with key: "+ key);
        return (String) input;
    }
    
    private static Location getLocation(JSONObject object, String key){
        JSONObject jObject = (JSONObject) object.get(key);
        if(jObject==null){
            plugin.getLogger().info("No Object found with key: "+ key);
            return new Location(world, 0,0,0);
        }
        Location loc =  new Location(world,  ((Long)jObject.get("X")).intValue(), 
                                             ((Long) jObject.get("Y")).intValue(), 
                                             ((Long) jObject.get("Z")).intValue());
        if(loc == null){
            plugin.getLogger().info("No valid Location found with key: "+ key);
            return new Location(world, 0,0,0);
        }
        plugin.getLogger().info("Location "+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+" with key: "+ key);
        return loc;
    }
    
}
