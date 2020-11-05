/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal.models;

/**
 *
 * @author linke
 */
public class Record {

    int ID;
    String time, userId;
    Car car;
    Track track;
    
    public Record(Track track, String time, Car car, String userId) {
        this.track = track;
        this.time = time;
        this.car = car;
        this.userId = userId;
        this.ID++;
    }

    public int getID() {
        return ID;
    }

    public Track getTrack() {
        return track;
    }

    public String getTime() {
        return time;
    }

    public Car getCar() {
        return car;
    }

    public String getUserId() {
        return userId;
    }
}
