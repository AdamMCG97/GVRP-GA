package tech.amcg.gvrp;

import java.util.ArrayList;
//superclass for all other varieties of locations
public class Location {
    int latitude;
    int longitude;
    String name;

    public Location() {
    }
    //construct instance
    public Location(String name, int x, int y){
        this.latitude = x;
        this.longitude = y;
        this.name = name;
    }
    //return related characteristics
    public int getLat(){
        return this.latitude;
    }

    public int getLong(){
        return this.longitude;
    }

    public String getName(){
        return this.name;
    }
    //calculate distance between this instance and another location
    public double distanceTo(Location location){
        double distance;
            int xDistance = Math.abs(getLat() - location.getLat());
            int yDistance = Math.abs(getLong() - location.getLong());
            distance = Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
        return distance;
    }

    public int getLoad(){
        return 0;
    }
    //calculate distance multiplied with cumulative load
    public double loadDistanceTo(double distance, ArrayList<Location> route){
        int totalLoad = 0;
        double loadDistance;
        int locationIndex = route.indexOf(this);
        if(route.lastIndexOf(this) != route.indexOf(this)){
            locationIndex = route.lastIndexOf(this);
        }
        //add code to make sure it gets right one if duplicate
        for(int i = locationIndex; i < RouteManager.numberOfLocations(); i++){

            totalLoad = totalLoad + route.get(i).getLoad();
        }
        //System.out.println(totalLoad);
        loadDistance = distance * totalLoad;
        return loadDistance;
    }

    public boolean isDepot(){
        return false;
    }

    @Override
    public String toString() {
        return " " + getName() + " " + getLat() + ", " + getLong()+ " ";
    }
}