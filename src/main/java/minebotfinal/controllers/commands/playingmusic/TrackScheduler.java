package minebotfinal.controllers.commands.playingmusic;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.ArrayList;
import java.util.List;

public class TrackScheduler extends AudioEventAdapter {
    AudioPlayer player;
    List<AudioTrack> trackList;

    public TrackScheduler(AudioPlayer player) {
        super();
        this.player = player;
        this.trackList = new ArrayList<>();
    }

    public void queue(AudioTrack track) {
        trackList.add(track);
    }

    public int getQueueLength() {
        return trackList.size();
    }
}
