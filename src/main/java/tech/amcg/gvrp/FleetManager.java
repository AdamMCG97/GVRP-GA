package tech.amcg.gvrp;

import java.util.ArrayList;

public class FleetManager {
    //retains list of all vehicles that have been defined
    private static final ArrayList<Vehicle> fleet = new ArrayList<Vehicle>();

    public static void addVehicle(Vehicle vehicle) {
        fleet.add(vehicle);
    }

    public static Vehicle getVehicle(int index) {
       return fleet.get(index);
    }
}
