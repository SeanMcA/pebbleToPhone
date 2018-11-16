package com.example.sitting_room.pebbletophone;



import java.util.ArrayList;
import com.example.sitting_room.pebbletophone.Subject;

public class Coordinates implements Subject{
    private static double currentLatitude;
    private static double currentLongitude;
    private static double accuracy;
    private static ArrayList observers;

    public Coordinates(){
        observers = new ArrayList();
    }



    public void setCoordinates(double lat, double lng, double accuracy){
        //Log.i("TAG","Coordinates - setCoordinates started");
        this.currentLatitude = lat;
        this.currentLongitude = lng;
        this.accuracy = accuracy;
        notifyObservers();
    }

    public static double getLatitude() {
        return currentLatitude;
    }

    public static double getLongitude() {
        return currentLongitude;
    }

    public static double getAccuracy() {
        return accuracy;
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {

    }

    @Override
    public void notifyObservers() {
        for(int i = 0; i < observers.size(); i++){
            Observer observer = (Observer) observers.get(i);
            observer.update(currentLatitude, currentLongitude, accuracy);
        }
    }
}//Coordiantes


