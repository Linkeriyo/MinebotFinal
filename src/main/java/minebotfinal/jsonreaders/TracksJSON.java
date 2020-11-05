/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal.jsonreaders;

import minebotfinal.models.Track;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static minebotfinal.jsonreaders.JSON.readJson;

public class TracksJSON {
    
    List<Track> trackList = new ArrayList<>();

    public TracksJSON(String path) throws IOException {
        JSONObject obj;
        JSONArray tracks;
        
        
        obj = new JSONObject(readJson(path));
        tracks = obj.getJSONArray("track");

        for (Object track : tracks) {
            JSONObject trackObject = (JSONObject) track;
            trackList.add(
                    new Track(
                            trackObject.getString("id"),
                            trackObject.getString("name"),
                            trackObject.getString("imageUrl")
                    )
            );
        }
    }

    /**
     * Returns a random track from the list provided in the cars.json file
     *
     * @return A String containing the name and the image of a random car.
     */
    public String randomTrack() {
        Track track;
        do {
            track = trackList.get((int) (Math.random() * this.trackList.size()));
        } while (track.getImageUrl() == null);

        return track.getName() + "\n" + track.getImageUrl();
    }
    
    public Track getTrackById(String id) {
        for (Track track : trackList) {
            if (track.getId().equals(id)) {
                return track;
            }
        }
        return null;
    }
}
