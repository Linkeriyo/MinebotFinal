/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal.commands;

import java.util.ArrayList;
import java.util.List;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;

/**
 *
 * @author linke
 */
public class AdiosCmd extends Command {

    public void checkCommand(String[] args, Message msg, JDA bot) {
        if (args.length < 2) {
            selfKick(msg);
        } else {
            kickMentions(msg);
        }
    }

    public void selfKick(Message msg) {
        try {
            msg.getGuild().kick(msg.getAuthor().getId(), "te has autokickeado, adios").queue();
            msg.getChannel().sendMessage("abueno adios master").queue();
        } catch (HierarchyException e) {
            msg.getChannel().sendMessage("no puedo manin").queue();
        }
    }

    public void kickMentions(Message msg) {
        List<Role> authorRoles = msg.getMember().getRoles();
        boolean authorIsCarepicha = false;
        for (Role role : authorRoles) {
            if (role.getId().equals("580426709581692928")) {
                authorIsCarepicha = true;
            }
        }
        
        if (authorIsCarepicha) {
            List<Member> toKick = msg.getMentionedMembers();
            Guild guild = msg.getGuild();
            List<Member> notKicked = new ArrayList<>();
            for (Member member : toKick) {
                List<Role> memberRoles = member.getRoles();
                boolean userIsCarepicha = false;
                for (Role role : memberRoles) {
                    if (role.getId().equals("580426709581692928")) {
                        userIsCarepicha = true;
                    }
                }
                if (!userIsCarepicha) {
                    guild.kick(member.getId(), "te ha kickeado " + msg.getAuthor().getName()).queue();
                } else {
                    notKicked.add(member);
                }
            }
            String messageContent = "bro";
            for (Member member : notKicked) {
                messageContent = messageContent.concat(", " + member.getAsMention());
            }
            messageContent = messageContent.concat(" son carepichas y no se les puede echar.");
            msg.getChannel().sendMessage(messageContent).queue();
        }
    }
    
}
