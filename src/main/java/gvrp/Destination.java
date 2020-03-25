package gvrp;

public class Destination extends Location {
    //subclass of location
    int pWeight;
    public Destination(String name, int x, int y, int pWeight) {
        super(name, x, y);
        //add to list of required destinations when created
        this.pWeight = pWeight;
        RouteManager.addLocation(this);
    }
    //only subclass to return non 0 result
    @Override
    public int getLoad(){
        return this.pWeight;
    }

    @Override
    public String toString() {
        return " " + getName() + " " + getLat() + ", " + getLong()+ " ";
    }
}
