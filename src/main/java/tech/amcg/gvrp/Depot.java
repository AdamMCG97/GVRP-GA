package tech.amcg.gvrp;

public class Depot extends Location {
    //subclass of location
    public Depot(String name, int x, int y) {
        super(name, x, y);
        //add this instance to list of depots and charging points when created
        RouteManager.setDepot(this);
        RouteManager.addChargingPoint(this);
    }
    @Override
    //only returns true if a location is this type of subclass
    public boolean isDepot(){
        return true;
    }

    public int getLoad(){
        return 0;
    }
    @Override
    public String toString() {
        return " " + getName() + " " + getLat() + ", " + getLong()+ " ";
    }
}
