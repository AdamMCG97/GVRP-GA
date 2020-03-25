package gvrp;

public class Vehicle {
    int capacity;
    public Vehicle (int capacity) {
        this.capacity = capacity;
        FleetManager.addVehicle(this);
    }

    public boolean isElectric() {
        return false;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getRange() {
        return 0;
    }

    public Vehicle() {
    }
}
