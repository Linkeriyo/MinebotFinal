package minebotfinal.controllers.commands.poll;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class PollListener extends ListenerAdapter {

    String prefix;

    public PollListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(prefix + "poll")) {
            Message msg = event.getMessage();
            JDA jda = event.getJDA();
            User author = event.getAuthor();
            String pollString = event.getMessage().getContentRaw().substring(prefix.length() + 4);

            MessageEmbed embed = new EmbedBuilder()
                    .setDescription(pollString)
                    .setTimestamp(new Date().toInstant())
                    .setAuthor(author.getName(), null, author.getAvatarUrl())
                    .build();

            msg.getTextChannel().sendMessage(embed).queue(message -> {
                message.addReaction("U+1F44D").queue();
                message.addReaction("U+1F44E").queue();
            });
        }

    }
}
