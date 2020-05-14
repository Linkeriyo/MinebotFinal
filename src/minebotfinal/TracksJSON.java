/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal;

import minebotfinal.MinebotFinal;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import minebotfinal.types.Track;
import org.json.JSONArray;
import org.json.JSONObject;

public class TracksJSON {
    
    List<Track> trackList = new ArrayList<>();

    public TracksJSON(String path) throws FileNotFoundException, IOException {
        JSONObject obj;
        JSONArray tracks;
        
        
        obj = new JSONObject(MinebotFinal.readJson(path));
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
