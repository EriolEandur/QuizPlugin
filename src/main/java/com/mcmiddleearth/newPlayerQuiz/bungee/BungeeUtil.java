/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mcmiddleearth.newPlayerQuiz.bungee;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mcmiddleearth.pluginutil.PluginUtilsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 *
 * @author Eriol_Eandur
 */
public class BungeeUtil implements PluginMessageListener {
    
    private static String channelName;
    
    public BungeeUtil(PluginUtilsPlugin plugin) {
        channelName = "BungeeCord";
        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channelName);
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(plugin, channelName, this);
    }
    
    public static void teleportPlayer(Player player, String server, String world, Location location) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(Subchannel.CONNECT.getName());
        out.writeUTF(server);
        out.writeUTF(player.getName());
        out.writeUTF(world);
        out.writeUTF(location.getX()+";"+location.getY()+";"+location.getZ()+";"+location.getYaw()+";"+location.getPitch());
        player.sendPluginMessage(PluginUtilsPlugin.getInstance(), channelName, out.toByteArray());
        /*new BukkitRunnable() {
            @Override
            public void run() {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF(server);
                player.sendPluginMessage(PluginUtilsPlugin.getInstance(), channelName, out.toByteArray());
            }
        }.runTaskLater(PluginUtilsPlugin.getInstance(), 10);*/
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
//Logger.getGlobal().info("Pugin Message! "+player);
        if (!channel.equals(channelName)) {
          return;
        }
//Logger.getGlobal().info("BungeeCord!");
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals(Subchannel.TPPOS.getName())) {
//Logger.getGlobal().info("TPPOS!");
            String playerData = in.readUTF();
            String worldData = in.readUTF();
            String[] locData = in.readUTF().split(";");
            Player source = Bukkit.getPlayer(playerData);
            World world = Bukkit.getWorld(worldData);
            if(world!=null) {
                Location location = new Location(world,Double.parseDouble(locData[0]),
                                                     Double.parseDouble(locData[1]),
                                                     Double.parseDouble(locData[2]),
                                                     Float.parseFloat(locData[3]),
                                                     Float.parseFloat(locData[4]));
                source.teleport(location);
//Logger.getGlobal().info("Teleport to!"+location);
            }
        } else if (subchannel.equals(Subchannel.TP.getName())) {
//Logger.getGlobal().info("TP!");
            String sourceData = in.readUTF();
            String name = in.readUTF();
            Player source = Bukkit.getPlayer(sourceData);
            Player destination = Bukkit.getPlayer(name);
            if(destination!=null) {
                source.teleport(destination);
//Logger.getGlobal().info("Teleport to!"+destination);
            }
        }
    }

}
