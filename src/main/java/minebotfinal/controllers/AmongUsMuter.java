package minebotfinal.controllers;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmongUsMuter implements Runnable {

    final String muteCodepoint = "U+1F507",
            unmuteCodepoint = "U+1F50A",
            crossCodepoint = "U+274C";
    Message receivedMessage,
            sentMessage;
    User host;
    VoiceChannel voiceChannel;
    TextChannel textChannel;
    JDA jda;
    ReactionListener rl;

    public AmongUsMuter(Message receivedMessage) throws NullPointerException {
        this.receivedMessage = receivedMessage;
        this.textChannel = receivedMessage.getTextChannel();
        this.jda = receivedMessage.getJDA();
        this.voiceChannel = receivedMessage.getMember().getVoiceState().getChannel();
        this.host = receivedMessage.getAuthor();
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
        if (sentMessage != null) {
            sentMessage.addReaction(crossCodepoint).complete();
            sentMessage.addReaction(muteCodepoint).complete();

            rl = new ReactionListener();
            jda.addEventListener(rl);
        } else {
            textChannel.sendMessage("algo ha ido mal").queue();
        }
    }

    private class ReactionListener extends ListenerAdapter {
        private void muteAllInChannel() {
            List<Member> members = voiceChannel.getMembers();
            for (Member member : members) {
                member.mute(true).complete();
            }
        }

        private void unmuteAllInChannel() {
            List<Member> members = voiceChannel.getMembers();
            for (Member member : members) {
                member.mute(false).complete();
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
                        reaction.removeReaction(jda.getSelfUser()).complete();
                        muteAllInChannel();
                        sentMessage.addReaction(unmuteCodepoint).complete();
                        break;
                    case unmuteCodepoint:
                        reaction.removeReaction(jda.getSelfUser()).complete();
                        unmuteAllInChannel();
                        sentMessage.addReaction(muteCodepoint).complete();
                        break;
                }
            } else if (!evt.getUser().equals(host)
                    && !evt.getUser().equals(jda.getSelfUser())) {
                textChannel.sendMessage("no eres el host amigo").queue();
            }
        }
    }

}
