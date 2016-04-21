/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.data;

import com.mcmiddleearth.newPlayerQuiz.NewPlayerQuizPlugin;
import com.mcmiddleearth.newPlayerQuiz.conversations.QuestionConversationFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
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
    
    @Getter
    @Setter
    private static QuestionConversationFactory questionFactory;
    

    private static Set<QuestionData> questions;
    private static Set<InformationData> informations;
    private static Set<TeleportationData> teleportations;
    
    @Getter
    private static World world;
    
    private static Set<Player> playerInConversation;

    private static Plugin plugin;
    
    @Setter
    @Getter
    private static int welcomeDelay = 30;
    
    @Setter
    private static boolean debug = false;
    
    @Setter
    private static Player logReceiver = null;
   
    private static final File quizDataDir = NewPlayerQuizPlugin.getPluginInstance().getDataFolder();
    private static final File quizDataFile = new File(quizDataDir+ File.separator + "QuizData.json");
    
    public static void initPluginData(Plugin pplugin){
        plugin = pplugin;
        questions = new HashSet<>();
        playerInConversation = new HashSet<>();
        try {
            loadFromFile(null);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(PluginData.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    private static LocationData dataFor(Location location, Set locationData){
        if(world==location.getWorld()){
            for(Object dataObject: locationData){
                LocationData data = (LocationData) dataObject;
                Location qLoc = data.getLocation();
                if(isInside(location.getBlockX(),qLoc.getBlockX(),data.getXSize())
                   && isInside(location.getBlockZ(),qLoc.getBlockZ(),data.getZSize())){
                    if(data instanceof TeleportationData) {
                        log("found target world: "+((TeleportationData)data).getTargetLocation().getWorld().getName());
                    }
                    return data;
                }
            }
        }
        return null;
    }
    
    public static QuestionData questionFor(Location location) {
        return (QuestionData) dataFor(location, questions);
    }
 
    public static InformationData infoFor(Location location) {
        return (InformationData) dataFor(location, informations);
    }
 
    public static TeleportationData teleportFor(Location location) {
        return (TeleportationData) dataFor(location, teleportations);
    }
 
    private static boolean isInside(int pBlock, int qBlock, int qSize){
        return qBlock-qSize<pBlock && qBlock+qSize>pBlock;
    }

    public static void loadFromFile(String filename) throws FileNotFoundException, ParseException{
        File file;
        if(filename == null) {
            file=quizDataFile;
        }
        else{
            file = new File(quizDataDir, filename+".json");
        }
        try {
            Scanner reader = new Scanner(file);
            String input = "";
            while(reader.hasNext()){
                input = input+reader.nextLine();
            }
            reader.close();
            JSONObject jObject = (JSONObject) new JSONParser().parse(input);
            String worldName = (String) jObject.get("World");
            world = Bukkit.getWorld(worldName);
            if(world == null){
                world = Bukkit.getWorlds().get(0);
                log("No world found with name "+worldName);
            }
            else {
                log("Quiz World: "+world.getName());
            }
            
            informations = new HashSet<>();
            JSONArray jArray = (JSONArray) jObject.get("Informations");
            if(jArray != null)
                for (Object infoObject : jArray) {
                    JSONObject jInfo = (JSONObject) infoObject;
                    InformationData info = new InformationData();
                    info.setInfoText(getString(jInfo,"Information Text"));
                    info.setXSize(getInteger(jInfo,"X Size"));
                    info.setZSize(getInteger(jInfo,"Z Size"));
                    info.setLocation(getLocation(jInfo,"Location"));
                    informations.add(info);
                }
            
            teleportations = new HashSet<>();
            jArray = (JSONArray) jObject.get("Teleportations");
            if(jArray != null)
                for (Object teleportObject : jArray) {
                    JSONObject jTeleport = (JSONObject) teleportObject;
                    TeleportationData teleport = new TeleportationData();
                    teleport.setXSize(getInteger(jTeleport,"X Size"));
                    teleport.setZSize(getInteger(jTeleport,"Z Size"));
                    teleport.setLocation(getLocation(jTeleport,"Location"));
                    String targetWorldName = (String) jTeleport.get("Target World");
                    log("targetWorldName: "+ targetWorldName);
                    World targetWorld = Bukkit.getWorld(targetWorldName);
                    if(targetWorld == null){
                        targetWorld = world;
                        log("No target world found with name: "+targetWorldName);
                        List<World> worldList= Bukkit.getWorlds();
                        log("Worlds: ");
                        for(World wrld: worldList) {
                            log(wrld.getName());
                        }
                    }
                    else {
                        log("Target World: "+targetWorld.getName());
                    }
                    Location targetLoc = getLocation(jTeleport,"Target Location");
                    targetLoc.setWorld(targetWorld);
                    teleport.setTargetLocation(targetLoc);
                    String welcomeMessage = (String) jTeleport.get("Broadcast");
                    teleport.setWelcomeMessage(welcomeMessage);
                    log("Welcome: "+welcomeMessage);
                    teleportations.add(teleport);
                    log("Saved target world: "+teleport.getTargetLocation().getWorld().getName());
                }
            
            questions = new HashSet<>();
            jArray = (JSONArray) jObject.get("Questions");
            for (Object questionObject : jArray) {
                JSONObject jQuestion = (JSONObject) questionObject;
                QuestionData question = new QuestionData();
                question.setQuestionText(getString(jQuestion,"Question Text"));
                question.setSuccessText(getString(jQuestion,"Success Text"));
                question.setFailText(getString(jQuestion,"Fail Text"));
                question.setXSize(getInteger(jQuestion,"X Size"));
                question.setZSize(getInteger(jQuestion,"Z Size"));
                question.setLocation(getLocation(jQuestion,"Question Location"));
                question.setSuccessLocation(getLocation(jQuestion,"Success Location"));
                question.setFailLocation(getLocation(jQuestion,"Fail Location"));
                JSONArray jAnswers = (JSONArray) jQuestion.get("Answers");
                ArrayList<Boolean> answers = new ArrayList<>();
                ArrayList<String> answerTexts = new ArrayList<>();
                if(jAnswers!=null){
                    for (Object answer : jAnswers) {
                        JSONObject jAnswer = (JSONObject) answer;
                        answers.add(getBoolean(jAnswer,"Correct"));
                        answerTexts.add(getString(jAnswer,"Text"));
                    }
                }
                question.setAnswerTexts(answerTexts);
                question.setAnswers(answers);
                questions.add(question);
            }
        } catch (FileNotFoundException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
            throw ex;
        } catch (ParseException ex) {
            plugin.getLogger().log(Level.SEVERE, null, ex);
            throw ex;
        }
    }
    
    private static boolean getBoolean(JSONObject jObject, String key){
        Object input = jObject.get(key);
        if(input==null){
            log("No Boolean found with key: "+ key);
            return false;
        }
        log("Boolean "+ (Boolean) input+" found with key: "+ key);
        return (Boolean) input;
    }
    
    private static int getInteger(JSONObject jObject, String key){
        Object input = jObject.get(key);
        if(input==null){
            log("No Integer found with key: "+ key);
            return 0;
        }
        log("Integer "+((Long) input).intValue()+" found with key: "+ key);
        return ((Long) input).intValue();
    }
    
    private static String getString(JSONObject jObject, String key){
        Object input = jObject.get(key);
        if(input==null){
            log("No String found with key: "+ key);
            return "";
        }
        log("String "+(String) input+" found with key: "+ key);
        return (String) input;
    }
    
    private static Location getLocation(JSONObject object, String key){
        JSONObject jObject = (JSONObject) object.get(key);
        if(jObject==null){
            log("No Object found with key: "+ key);
            return new Location(world, 0,0,0);
        }
        Location loc =  new Location(world,  ((Long)jObject.get("X")).intValue(), 
                                             ((Long) jObject.get("Y")).intValue(), 
                                             ((Long) jObject.get("Z")).intValue());
        log("Location "+loc.getBlockX()+" "+loc.getBlockY()+" "+loc.getBlockZ()+" with key: "+ key);
        return loc;
    }
    
    public static void log(String info){
        if(debug)
            if(logReceiver==null) {
                plugin.getLogger().info(info);
            }
            else {
                logReceiver.sendMessage("."+info+".");
            }
    }
    
    public static void debugLocations(Player player) {
        player.sendMessage("World: "+world.getName());
        for(QuestionData data: questions) {
            player.sendMessage("Question: " + data.getLocation().getBlockX()+" "
                                            + data.getLocation().getBlockY()+" "
                                            + data.getLocation().getBlockZ());
        }
        for(InformationData data: informations) {
            player.sendMessage("Information: " + data.getLocation().getBlockX()+" "
                                            + data.getLocation().getBlockY()+" "
                                            + data.getLocation().getBlockZ());
        }
        for(TeleportationData data: teleportations) {
            player.sendMessage("Teleportation: " + data.getLocation().getBlockX()+" "
                                            + data.getLocation().getBlockY()+" "
                                            + data.getLocation().getBlockZ());
        }
    }
}
