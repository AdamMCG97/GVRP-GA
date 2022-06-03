package tech.amcg.gvrp;

public class ElectricVehicle extends Vehicle {
    //subclass of vehicle
    int range;

    public ElectricVehicle(int capacity, int range) {
        super(capacity);
        this.range = range;
    }

    //only subclass of vehicle to return non 0 value
    public int getRange() {
        return this.range;
    }

    public boolean isElectric() {
        return true;
    }
}
