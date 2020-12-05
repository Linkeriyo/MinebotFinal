package minebotfinal.controllers.commands;

import minebotfinal.controllers.StringSplitter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PollListener extends ListenerAdapter {

    String prefix;

    public PollListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        System.out.println(event.getMessage().getContentRaw());

        if (event.getMessage().getContentRaw().startsWith(prefix + "poll")) {
            Message msg = event.getMessage();
            User author = event.getAuthor();
            List<String> content = new StringSplitter(event.getMessage().getContentRaw()).split();
            String pollString = content.get(2);
            List<String> options = new ArrayList<>();
            for (int i = 3; i < content.size(); i++) {
                if (!content.get(i).isEmpty()) options.add(content.get(i));
            }
            EmbedBuilder embedBuilder = new EmbedBuilder()
                    .setDescription(pollString)
                    .setTimestamp(new Date().toInstant())
                    .setAuthor(author.getName(), null, author.getAvatarUrl());

            for (int i = 0; i < options.size(); i++) {
                embedBuilder.addField("opción " + (i + 1), options.get(i), true);
            }

            MessageEmbed embed = embedBuilder.build();


            //TODO: añadir reacción por cada una de las opciones
            msg.getTextChannel().sendMessage(embed).queue(message -> {
                for (int i = 0; i < options.size(); i++) {
                    message.addReaction((i+1) + "??").queue();
                }
            });
        }
    }
}
