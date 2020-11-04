package minebotfinal.controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class AmongUsMuter implements Runnable {

    Message recievedMessage,
            sentMessage;
    VoiceChannel voiceChannel;
    TextChannel textChannel;
    JDA jda;

    public AmongUsMuter(Message recievedMessage) throws NullPointerException {
        this.recievedMessage = recievedMessage;
        this.voiceChannel = recievedMessage.getMember().getVoiceState().getChannel();
        this.textChannel = recievedMessage.getTextChannel();
        this.jda = recievedMessage.getJDA();
    }

    @Override
    public void run() {
        sentMessage = new MessageBuilder()
                .setContent("canal seleccionado: " + voiceChannel.getName())
                .build();
        textChannel.sendMessage(sentMessage + "\n id: " + sentMessage.getId()).queue();
    }


}
