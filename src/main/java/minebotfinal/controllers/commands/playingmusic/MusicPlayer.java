package minebotfinal.controllers.commands.playingmusic;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class MusicPlayer {

    VoiceChannelConnection connection;
    TrackScheduler trackScheduler;
    AudioPlayerManager playerManager;

    public MusicPlayer(VoiceChannel voiceChannel) {
        playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);

        AudioPlayer player = playerManager.createPlayer();

        trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);





        AudioManager audioManager = voiceChannel.getGuild().getAudioManager();
        audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
        connection = new VoiceChannelConnection(voiceChannel, audioManager);
    }

    public void loadItems(String identifier) {
        playerManager.loadItem(identifier, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                trackScheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    trackScheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                System.out.println("no matches for identifier: " + identifier);
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                System.out.println("load failed: " + throwable.getMessage());
            }
        });
    }

}
