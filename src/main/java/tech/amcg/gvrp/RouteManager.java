package tech.amcg.gvrp;

import java.util.*;
//manages route requirements
public class RouteManager {
    //maintains list of required destinations, depots and charging points and returns other useful information
    private static ArrayList<Location> destinationLocations = new ArrayList<Location>();

    private static ArrayList<Location> depot = new ArrayList<Location>();

    private static ArrayList<Location> chargingList = new ArrayList<Location>();

    public static void setDepot(Location location) {
        //placeholders are not included in list of depots
        if(!(location.getName() == "Depot Placeholder 1" || location.getName() == "Depot Placeholder 2")) {
            depot.add(location);
        }
    }

    public static int depotListSize() {
        return depot.size();
    }

    public static int chargingListSize() {
        return chargingList.size();
    }

    public static int destinationsListSize() {
        return destinationLocations.size();
    }

    public static Location getDepot(int index) {
        return (Location)depot.get(index);
    }

    public static void addLocation(Location location) {
        destinationLocations.add(location);
      //  if(location.isDepot()) {
          //  setDepot(location);
       // }
    }

    public static Location getLocation(int index){
        return (Location)destinationLocations.get(index);
    }

    public static int numberOfLocations(){
        return destinationLocations.size();
    }

    public static void addChargingPoint(Location location) {
        if(!(location.getName() == "Depot Placeholder 1" || location.getName() == "Depot Placeholder 2")) {
            chargingList.add(location);
        }
    }

    public static Location getChargingPoint(int index) {
        return chargingList.get(index);
    }

    public static Location nearestChargingPoint(Location location) {
        Location nearestChargingPoint = getChargingPoint(0);
        double chargingPointDistance = Double.MAX_VALUE;
        if (chargingListSize() > 1) {
            for (int j = 0; j < RouteManager.chargingListSize() - 1; j++) {
                if (location.distanceTo(RouteManager.getChargingPoint(j)) < chargingPointDistance) {
                    chargingPointDistance = location.distanceTo(RouteManager.getChargingPoint(j));
                    nearestChargingPoint = RouteManager.getChargingPoint(j);
                }
            }
        }
        return nearestChargingPoint;
    }
}
