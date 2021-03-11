package minebotfinal.controllers.commands.playingmusic;

import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceChannelConnection {

    VoiceChannel channel;
    AudioManager audioManager;

    public VoiceChannelConnection(VoiceChannel channel, AudioManager audioManager) {
        this.channel = channel;
        this.audioManager = audioManager;
        audioManager.openAudioConnection(channel);
    }

    public void disconnect() {
        audioManager.closeAudioConnection();
    }

}
