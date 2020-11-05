/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minebotfinal.jsonreaders;

import minebotfinal.models.Car;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static minebotfinal.jsonreaders.JSON.readJson;

public class CarsJSON {
    
    List<Car> carList = new ArrayList<>();

    public CarsJSON(String path) throws IOException {
        JSONObject obj;
        JSONArray cars;
        
        
        obj = new JSONObject(readJson(path));
        cars = obj.getJSONArray("car");

        for (Object car : cars) {
            JSONObject carObject = (JSONObject) car;
            carList.add(
                    new Car(
                            carObject.getString("id"),
                            carObject.getString("name"),
                            carObject.getString("imageUrl")
                    )
            );
        }
    }

    /**
     * Returns a random track from the list provided in the cars.json file
     *
     * @return A String containing the name and the image of a random car.
     */
    public String randomCar() {
        Car car;
        do {
            car = carList.get((int) (Math.random() * this.carList.size()));
        } while (car.getImageUrl() == null);

        return car.getName() + "\n" + car.getImageUrl();
    }

    public Car getCarById(String id) {
        for (Car car : carList) {
            if (car.getId().equals(id)) {
                return car;
            }
        }
        return null;
    }
}
