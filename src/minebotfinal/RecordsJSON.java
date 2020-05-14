/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import minebotfinal.types.Car;
import minebotfinal.types.Record;
import minebotfinal.types.Track;
import org.json.JSONArray;
import org.json.JSONObject;

public class RecordsJSON {
    
    List<Record> recordList = new ArrayList<>();
    String path;

    public RecordsJSON(String path) throws FileNotFoundException, IOException {
        this.path = path;
        JSONObject obj;
        JSONArray records;

        obj = new JSONObject(MinebotFinal.readJson(this.path));
        records = obj.getJSONArray("record");

        for (Object record : records) {
            JSONObject recordObject = (JSONObject) record;
            
            Track track = new TracksJSON("files/tracks.json").getTrackById(recordObject.getString("trackId"));
            Car car = new CarsJSON("files/cars.json").getCarById(recordObject.getString("carId"));
            
            recordList.add(
                    new Record(
                            track,
                            recordObject.getString("time"),
                            car,
                            recordObject.getString("authorId")
                    )
            );
        }
    }

    public List<Record> getRecordList() {
        return recordList;
    }
    
    public void addRecord(Track track, String time, Car car, String userId) {
        recordList.add(
                new Record(track, time, car, userId)
        );
    }
    
    public void addRecord(Record record) {
        recordList.add(record);
    }
    
    public void writeList() {
        try {
            JSONArray records = new JSONArray();
            
            for (Record record : recordList) {
                JSONObject recordObject = new JSONObject();
                recordObject.put("trackId", record.getTrack().getId());
                recordObject.put("time", record.getTime());
                recordObject.put("carId", record.getCar().getId());
                recordObject.put("authorId", record.getUserId());
                
                records.put(recordObject);
            }
            
            JSONObject obj = new JSONObject();
            obj.put("record", records);
            obj.write(new FileWriter(new File(this.path))).flush();
        } catch (IOException ex) {
            Logger.getLogger(RecordsJSON.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
