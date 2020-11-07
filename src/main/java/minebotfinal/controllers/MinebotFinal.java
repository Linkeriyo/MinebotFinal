package minebotfinal.controllers;


import minebotfinal.controllers.commands.AmongUsMuter;
import minebotfinal.exceptions.ServerExclusiveCommandException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static minebotfinal.jsonreaders.JSON.readJson;

public class MinebotFinal {
    JDA jda;
    String token, prefix;
    JSONObject config;
    Guild minecrafters;
    Role carepicha, ogCarepicha;

    public MinebotFinal() throws LoginException {
        try {
            config = new JSONObject(readJson("files/config.json"));
            token = config.get("token").toString();
            prefix = config.get("prefix").toString();

            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new MessagesListener(prefix))
                    .build();
            jda.awaitReady();

            minecrafters = jda.getGuildById("580421667336224769");
            assert minecrafters != null;
            carepicha = minecrafters.getRoleById("580426709581692928");
            ogCarepicha = minecrafters.getRoleById("708345730460549122");
        } catch (FileNotFoundException ex) {
            System.err.println("No se ha encontrado el archivo files/config.json");
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }

        new Thread(new InputListener()).start();
    }

    // Listener for incoming messages.
    private class MessagesListener extends ListenerAdapter {

        final String prefix;

        public MessagesListener(String prefix) {
            this.prefix = prefix;
        }

        @Override
        // Will be executed when a message is received.
        public void onMessageReceived(MessageReceivedEvent event) {
            Message msg = event.getMessage();
            String content = msg.getContentRaw();

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

                    case "amongus":
                        try {
                            new AmongUsMuter(msg).run();
                        } catch (PermissionException | ServerExclusiveCommandException ignored) {
                        }
                }
            }
        }
    }

    private class InputListener implements Runnable {

        @Override
        public void run() {
            Scanner scan = new Scanner(System.in, "ISO_8859_1").useDelimiter("\n").useLocale(new Locale("es", "ES"));

            while (true) {
                String input = scan.next();
                String[] args = input.split(" ");

                List<TextChannel> textChannels = Objects.requireNonNull(jda.getGuildById("580421667336224769")).getTextChannels();

                boolean channelFound = false;
                for (TextChannel channel : textChannels) {
                    if (args[0].equals(channel.getName())) {
                        channelFound = true;
                        input = input.substring(args[0].length());
                        channel.sendMessage(input).queue();
                        System.out.println("#" + args[0] + " -> " + input);
                    }
                }

                if (!channelFound) {
                    System.out.println("No se ha encontrado el canal " + args[0] + ".");
                }
            }
        }
    }

    public JDA getJda() {
        return jda;
    }

    public String getPrefix() {
        return prefix;
    }

    public static void main(String[] args) {
        try {
            MinebotFinal bot = new MinebotFinal();
        } catch (LoginException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
