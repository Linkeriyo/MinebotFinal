/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import java.lang.reflect.Method;
import net.dv8tion.jda.api.entities.Message;

/**
 *
 * @author linke
 */
public abstract class Command {

    public void checkCommand(String[] args, Message msg) {
        if (args.length > 1) {
            try {
                Object thisClass = getClass().newInstance();
                Method method = getClass().getMethod(args[1], String[].class, MessageChannel.class);
                method.invoke(thisClass, args, msg.getChannel());
            } catch (NoSuchMethodException ex) {
                msg.getChannel().sendMessage("no existe el metodo").queue();
            } catch (Exception ex) {
                msg.getChannel().sendMessage("usalo bien").queue();
            }
        } else {
            doCommand(msg.getChannel());
        }
    }
    
    public void doCommand(MessageChannel channel) {
        System.out.println("cuando te pasas");
    }
    
    public void help(String[] args, MessageChannel channel) {
        String help = "bro momento";
        channel.sendMessage(help).queue();
    }
    
}
