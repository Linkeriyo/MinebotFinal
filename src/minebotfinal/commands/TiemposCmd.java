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
import minebotfinal.CarsJSON;
import minebotfinal.RecordsJSON;
import minebotfinal.TracksJSON;
import minebotfinal.types.Car;
import minebotfinal.types.Record;
import minebotfinal.types.Track;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;



/**
 *
 * @author linke
 */
public class TiemposCmd extends Command {
    
    RecordsJSON records;
    JDA bot;
    public void checkCommand(String[] args, Message msg, JDA bot) {
        this.bot = bot;
        
        switch (args[1]) {
            case "add":
                this.add(args, msg);
                break;
            case "show":
                this.show(args, msg, bot);
                break;
            default:
                this.doCommand(msg.getChannel());
                break;
        }
    }
    
    @Override
    public void doCommand(MessageChannel channel) {
        channel.sendMessage("tienes que utilizarlo así:\n `<prefijo> tiempos <add/show>`").queue();
    }
    
    public void add(String[] args, Message msg) {
        try {
            TracksJSON tracks = new TracksJSON("files/tracks.json");
            Track track = tracks.getTrackById(args[2]);
            
            CarsJSON cars = new CarsJSON("files/cars.json");
            Car car = cars.getCarById(args[4]);
            
            String authorId = msg.getAuthor().getId();
            
            Record record = new Record(track, args[3], car, authorId);
            records = new RecordsJSON("files/records.json");
            records.addRecord(record);
            records.writeList();
            
            msg.getChannel().sendMessage("tiempo guardado!").queue();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TiemposCmd.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TiemposCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void show(String[] args, Message msg, JDA bot) {
        try {
            records = new RecordsJSON("files/records.json");
            String messageContent = "";
            TracksJSON tracks = new TracksJSON("files/tracks.json");
            CarsJSON cars = new CarsJSON("files/cars.json");
            
            for (Record record : records.getRecordList()) {
                Track track = tracks.getTrackById(record.getTrack().getId());
                Car car = cars.getCarById(record.getCar().getId());
                String username = bot.getUserById(record.getUserId()).getName();
                messageContent = messageContent.concat(track.getName()+ " - " + record.getTime() + " - " + car.getName() + " - " + username + "\n");
            }
            msg.getChannel().sendMessage(new EmbedBuilder().setDescription(messageContent).build()).queue();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TiemposCmd.class.getName()).log(Level.SEVERE, null, ex);
            msg.getChannel().sendMessage("no se ha encontrado el archivo <@154268434090164226>").queue();
        } catch (IOException ex) {
            Logger.getLogger(TiemposCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
