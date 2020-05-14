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
import net.dv8tion.jda.api.entities.MessageChannel;

/**
 *
 * @author linke
 */
public class CarroCmd extends Command {

    @Override
    public void doCommand(MessageChannel channel) {
        try {
            CarsJSON cars = new CarsJSON("files/cars.json");
            channel.sendMessage("toma tu carro: " + cars.randomCar()).queue();
        } catch (FileNotFoundException ex) {
            channel.sendMessage("no puedo").queue();
        } catch (IOException ex) {
            Logger.getLogger(CarroCmd.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void help(String[] args, MessageChannel channel) {
        String help = "`Devuelve un carro de puta madre`";
        channel.sendMessage(help).queue();
    }
}
