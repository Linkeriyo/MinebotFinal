package minebotfinal.controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.EmoteManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmongUsMuter implements Runnable {

    final String muteCodepoint = "U+1F507",
            unmuteCodepoint = "U+1F50A",
            crossCodepoint = "U+274C";
    Message recievedMessage,
            sentMessage;
    User host;
    VoiceChannel voiceChannel;
    TextChannel textChannel;
    JDA jda;
    ReactionListener rl;

    public AmongUsMuter(Message recievedMessage) throws NullPointerException {
        this.recievedMessage = recievedMessage;
        this.textChannel = recievedMessage.getTextChannel();
        this.jda = recievedMessage.getJDA();
        this.voiceChannel = recievedMessage.getMember().getVoiceState().getChannel();
        this.host = recievedMessage.getAuthor();
    }


    /**
     * Finds the last message sent by this bot in a text channel.
     *
     * @param textChannel the text channel to search in.
     * @return the last message sent by this bot in the textChannel.
     */
    private Message findLastMessageBySelf(TextChannel textChannel) {
        MessageHistory history = textChannel.getHistory();
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
                history.retrievePast(10).complete();
            }
            if (history.getRetrievedHistory().get(i).getAuthor() == jda.getSelfUser()) {
                return history.getRetrievedHistory().get(i);
            }
        }
        return null;
    }

    @Override
    public void run() {
        if (voiceChannel == null) {
            textChannel.sendMessage("no estas conectado a un canal de voz de " + textChannel.getGuild().getName() + " >:(").queue();
            return;
        }
        textChannel.sendMessage("canal seleccionado: " + voiceChannel.getName()
                + "\nanfitrion: " + host.getName()
        ).complete();
        sentMessage = findLastMessageBySelf(textChannel);
        sentMessage.addReaction(crossCodepoint).complete();
        sentMessage.addReaction(muteCodepoint).complete();

        rl = new ReactionListener();
        jda.addEventListener(rl);
    }

    private class ReactionListener extends ListenerAdapter {
        private void muteAllInChannel() {
            List<Member> members = voiceChannel.getMembers();
            for (Member member : members) {
                member.mute(true).queue();
            }
        }

        private void unmuteAllInChannel() {
            List<Member> members = voiceChannel.getMembers();
            for (Member member : members) {
                member.mute(false).queue();
            }
        }

        @Override
        public void onMessageReactionAdd(@NotNull MessageReactionAddEvent evt) {
            MessageReaction reaction = evt.getReaction();
            MessageReaction.ReactionEmote emote = reaction.getReactionEmote();

            if (evt.getMessageId().equals(sentMessage.getId())
                    && !evt.getMember().getUser().equals(jda.getSelfUser())
                    && evt.getMember().getVoiceState().getChannel().equals(voiceChannel)
                    && evt.getMember().getUser().equals(host)) {
                reaction.removeReaction(evt.getUser()).queue();

                // Finalize among us mode.
                if (emote.getAsCodepoints().toUpperCase().equals(crossCodepoint)) {
                    unmuteAllInChannel();
                    jda.removeEventListener(rl);
                    sentMessage.delete().queue();
                    return;
                }

                switch (emote.getAsCodepoints().toUpperCase()) {
                    case muteCodepoint:
                        muteAllInChannel();
                        reaction.removeReaction(jda.getSelfUser()).queue();
                        sentMessage.addReaction(unmuteCodepoint).queue();
                        break;
                    case unmuteCodepoint:
                        unmuteAllInChannel();
                        reaction.removeReaction(jda.getSelfUser()).queue();
                        sentMessage.addReaction(muteCodepoint).queue();
                        break;
                }
            } else if (!evt.getMember().getUser().equals(host)) {
                textChannel.sendMessage("no eres el host amigo").queue();
            }
        }
    }

}
