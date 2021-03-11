package minebotfinal.controllers.commands.playingmusic;

import minebotfinal.controllers.MinebotFinal;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MusicListener extends ListenerAdapter {

    String prefix;
    MusicPlayer musicPlayer;

    public MusicListener() {
        this.prefix = MinebotFinal.getPrefix();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        super.onGuildMessageReceived(event);

        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith(prefix + "play")) {
            GuildVoiceState voiceState = msg.getMember().getVoiceState();
            int substringValue = prefix.length() + 4;

            if (voiceState.inVoiceChannel()) {
                if (musicPlayer == null) {
                    musicPlayer = new MusicPlayer(voiceState.getChannel());
                }
                musicPlayer.loadItems(msg.getContentRaw().substring(substringValue));
            }
        }
    }
}
