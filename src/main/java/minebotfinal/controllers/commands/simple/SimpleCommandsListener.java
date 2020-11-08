package minebotfinal.controllers.commands.simple;

import minebotfinal.controllers.MinebotFinal;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.OffsetDateTime;
import java.util.List;

public class SimpleCommandsListener extends ListenerAdapter {

    String prefix;

    public SimpleCommandsListener(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        String content = msg.getContentRaw();
        JDA jda = event.getJDA();

        // If the message is not from this bot and the message starts with the specified prefix...
        if (!msg.getAuthor().getId().equals(jda.getSelfUser().getId()) && content.startsWith(prefix)) {
            // This is the content of the message without the prefix.
            String command = content.substring(prefix.length());

            // This an array with each word in the command.
            String[] args = command.split(" ");

            MessageEmbed embed;
            switch (args[0]) {
                case "ping":
                    msg.getChannel().sendMessage("pong!").queue();
                    break;

                case "pong":
                    msg.getChannel().sendMessage("ping!").queue();
                    break;

                case "request":
                    String request = command.substring(args[0].length());
                    User author = msg.getAuthor();
                    embed = new EmbedBuilder()
                            .setDescription(request)
                            .setTimestamp(OffsetDateTime.parse("2020-11-08T19:29:00.000Z"))
                            .setAuthor(author.getName(), null, author.getAvatarUrl())
                            .build();

                    jda.openPrivateChannelById("154268434090164226").complete().sendMessage(embed).queue();
                    msg.getChannel().sendMessage("propuesta enviada").queue();
                    break;

                case "help":
                    embed = new EmbedBuilder()
                            .setTitle("lista de comandos (BETA)")
                            .setDescription("un poco de comprensi�n, a�n me estoy desarrollando\npor favor reportad cualquier problema a mi padre <@154268434090164226> ")
                            .setFooter("gracias", "https://mui.today/__export/1583420503184/sites/mui/img/2019/12/02/bad-bunny.jpg_1899857922.jpg")
                            .addField(prefix + "help", "> creo que te imaginas lo que hace", false)
                            .addField(prefix + "ruleta", "> decido qui�n es el pr�ximo en irse de minecrafters", false)
                            .addField(prefix + "amongus", "> selecciono el canal en el que est�s y env�o un mensaje. usa las reacciones" +
                                    "\n> en ese mensaje para mutear / desmutear a todo el canal o salir del modo" +
                                    "\n> among us" +
                                    "\n> **solo carepichas y og carepichas**", false)
                            .addField(prefix + "request", "> envia una propuesta a <@154268434090164226>, usad esto porfa", false)
                            .build();

                    msg.getChannel().sendMessage(embed).queue();
            }
        }
    }
}