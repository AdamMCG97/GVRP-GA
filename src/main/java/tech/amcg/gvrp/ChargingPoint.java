package tech.amcg.gvrp;

public class ChargingPoint extends Location {
    //subclass of location
    public ChargingPoint(String name, int x, int y) {
        super(name, x, y);
        //add to list of charging points when created
        RouteManager.addChargingPoint(this);
    }




}
