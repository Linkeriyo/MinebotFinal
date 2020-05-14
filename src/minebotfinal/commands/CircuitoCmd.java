/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal.commands;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import minebotfinal.TracksJSON;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 *
 * @author linke
 */
public class CircuitoCmd extends Command {


    @Override
    public void doCommand(MessageChannel channel) {
        try {
            TracksJSON track = new TracksJSON("files/tracks.json");
            channel.sendMessage("toma tu circuito: " + track.randomTrack()).queue();
        } catch (FileNotFoundException ex) {
            channel.sendMessage("no puedo").queue();
        } catch (IOException ex) {
            Logger.getLogger(CircuitoCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  
    @Override
    public void help(String[] args, MessageChannel channel) {
        String help = "`Devuelve un circuito increible`";
        channel.sendMessage(help).queue();
    }
}
