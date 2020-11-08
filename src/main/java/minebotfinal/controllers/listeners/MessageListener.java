package minebotfinal.controllers.listeners;

import minebotfinal.controllers.MinebotFinal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class MessageListener extends ListenerAdapter {

    final String prefix;
    Guild minecrafters;
    Role carepicha, ogCarepicha;

    public MessageListener(String prefix) {
        this.prefix = prefix;

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String content = msg.getContentRaw();
        JDA jda = event.getJDA();
        minecrafters = jda.getGuildById(MinebotFinal.minecraftersGuildId);
        carepicha = jda.getRoleById(MinebotFinal.carepichaRoleId);
        ogCarepicha = jda.getRoleById(MinebotFinal.ogCarepichaRoleId);

        // If the message is not from this bot and the message starts with the specified prefix...
        if (!msg.getAuthor().getId().equals(jda.getSelfUser().getId()) && content.startsWith(prefix)) {
            // This is the content of the message without the prefix.
            String command = content.substring(prefix.length());

            // This an array with each word in the command.
            String[] args = command.split(" ");

            switch (args[0]) {
                case "ping":
                    msg.getTextChannel().sendMessage("pong!").queue();
                    break;

                case "pong":
                    msg.getTextChannel().sendMessage("ping!").queue();
                    break;

                case "ruleta":
                    List<Member> members = minecrafters.getMembersWithRoles(ogCarepicha);
                    members.addAll(minecrafters.getMembersWithRoles(carepicha));
                    int member = (int) (Math.random() * members.size());
                    boolean gone = (int) (Math.random() * 2) == 0;
                    msg.getChannel().sendMessage("¿es " + members.get(member).getUser().getName() + " el próximo en irse de minecrafters?").queue();
                    if (gone) {
                        msg.getChannel().sendMessage("sii").queue();
                    } else {
                        msg.getChannel().sendMessage("noo").queue();
                    }
                    break;
            }
        }
    }
}