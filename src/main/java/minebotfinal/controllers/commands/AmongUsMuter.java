package minebotfinal.controllers.commands;

import minebotfinal.exceptions.ServerExclusiveCommandException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmongUsMuter extends Command implements Runnable {
    final String muteCodepoint = "U+1F507",
            unmuteCodepoint = "U+1F50A",
            crossCodepoint = "U+274C";
    Message receivedMessage,
            sentMessage;
    User host;
    TextChannel textChannel;
    VoiceChannel voiceChannel;
    JDA jda;
    ReactionListener rl;

    public AmongUsMuter(Message receivedMessage) throws NullPointerException, PermissionException, ServerExclusiveCommandException {
        // Command exclusive variables.
        serverExclusive = true;
        requiredPermissions = new Permission[]{
                Permission.MANAGE_EMOTES,
                Permission.MESSAGE_MANAGE,
                Permission.VOICE_MUTE_OTHERS
        };

        // Guild check
        this.receivedMessage = receivedMessage;
        this.messageChannel = receivedMessage.getChannel();
        checkServerExclusiveMessage();

        this.textChannel = receivedMessage.getTextChannel();
        this.currentGuild = receivedMessage.getGuild();
        if (!checkPermissions().isEmpty()) {
            sendPermissionsMessage();
            throw new PermissionException("Permissions missing.");
        }
        this.jda = receivedMessage.getJDA();
        this.voiceChannel = receivedMessage.getMember().getVoiceState().getChannel();
        this.host = receivedMessage.getAuthor();
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
            Member member = evt.getMember();

            MessageReaction reaction = evt.getReaction();
            MessageReaction.ReactionEmote emote = reaction.getReactionEmote();

            if (evt.getMessageId().equals(sentMessage.getId())
                    && !member.getUser().equals(jda.getSelfUser())
                    && member.getVoiceState().getChannel().equals(voiceChannel)
                    && member.getUser().equals(host)) {
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
                AmongUsMuter.this.textChannel.sendMessage("no eres el host amigo").queue();
            }
        }
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
            this.textChannel.sendMessage("no estas conectado a un canal de voz de " + this.textChannel.getGuild().getName() + " >:(").queue();
            return;
        }
        this.textChannel.sendMessage("canal seleccionado: " + voiceChannel.getName()
                + "\nanfitrion: " + host.getName()
        ).complete();
        sentMessage = findLastMessageBySelf(this.textChannel);
        sentMessage.addReaction(crossCodepoint).complete();
        sentMessage.addReaction(muteCodepoint).complete();

        rl = new ReactionListener();
        jda.addEventListener(rl);
    }
}
