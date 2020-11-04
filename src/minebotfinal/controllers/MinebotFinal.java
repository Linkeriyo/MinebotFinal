package minebotfinal.controllers;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public MinebotFinal() throws LoginException {
        try {
            config = new JSONObject(readJson("files/config.json"));
            token = config.get("token").toString();
            prefix = config.get("prefix").toString();

            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new MessagesListener(prefix))
                    .build();
            jda.awaitReady();

        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }

        Thread console = new Thread(new InputListener());

        console.start();
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

                    case "ruleta":
                        try {
                            List<Member> members = msg.getGuild().getMembersWithRoles(jda.getRoleById("580426709581692928"));
                            int member = (int) (Math.random() * members.size());
                            boolean gone = (int) (Math.random() * 2) == 0;
                            msg.getChannel().sendMessage("¿es " + members.get(member).getUser().getName() + " el próximo en irse de minecrafters?").queue();
                            if (gone) {
                                msg.getChannel().sendMessage("sii").queue();
                            } else {
                                msg.getChannel().sendMessage("noo").queue();
                            }
                        } catch (IllegalStateException e) {
                            msg.getChannel().sendMessage("este comando solo se puede usar en minecrafters mi pana").queue();
                        }
                        break;

                    case "mute":
                        if (msg.getMember().getId().equals("154268434090164226")) {
                            List<Member> members = msg.getMember().getVoiceState().getChannel().getMembers();
                            for (Member member : members) {
                                member.mute(true).queue();
                            }
                            msg.getTextChannel().sendMessage("muteados jeje").queue();
                        } else {
                            msg.getTextChannel().sendMessage("no, tú no").queue();
                        }
                        break;

                    case "unmute":
                        if (msg.getMember().getId().equals("154268434090164226")) {
                            List<Member> members = msg.getMember().getVoiceState().getChannel().getMembers();
                            for (Member member : members) {
                                member.mute(false).queue();
                            }
                            msg.getTextChannel().sendMessage("desmuteados los pibes").queue();
                        } else {
                            msg.getTextChannel().sendMessage("no, tú no").queue();
                        }
                        break;

                    case "amongus":
                        if (msg.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                            new AmongUsMuter(msg).run();
                        }
                }
            }
        }
    }

    private class InputListener implements Runnable {

        @Override
        public void run() {
            Scanner scan = new Scanner(System.in, StandardCharsets.ISO_8859_1).useDelimiter("\n").useLocale(new Locale("es", "ES"));

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
            bot.jda.awaitReady();

            String input = "";
            Scanner in = new Scanner(System.in);
            
/*            List<Message> musicMessages = bot.bot.getTextChannelById("710581989748768889").getHistoryFromBeginning(100).complete().getRetrievedHistory();
            
            System.out.println(musicMessages.toString());
            
            for (Message msg : musicMessages) {
                if (!msg.getAuthor().getId().equals("547905866255433758")) {
                    msg.delete().queue();
                }
            }*/

        } catch (LoginException | InterruptedException ex) {
            Logger.getLogger(MinebotFinal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
