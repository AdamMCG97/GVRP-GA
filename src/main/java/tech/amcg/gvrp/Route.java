package tech.amcg.gvrp;

import java.util.*;

public class Route {
    //class for each individual
    private static ArrayList<Location> locationList = new ArrayList<Location>();
    private ArrayList<Location> route = new ArrayList<Location>();
    private ArrayList<Location> chargedRoute = new ArrayList<Location>();
    private double distance = 0;
    private double fitness = 0;
    private double loadDistance = 0;
    public Vehicle vehicle;

    //default constructor
    public Route(){
        for (int i = 0; i< RouteManager.numberOfLocations(); i++) {
            route.add(null);
        }
    }

    //check if any genes are not defined in chromosome
    public boolean fullRoute(){
        boolean result = true;
        for(int i = 0; i< routeSize()-1; i++) {
            if(route.get(i)==null){
                result = false;
            }
        }
        return result;
    }

    public Route(ArrayList route){
        this.route = route;
    }

    public int routeSize() {
            return route.size();
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    //change gene in chromosome and reset fitness
    public void setLocation(int position, Location location){
        route.set(position, location);
        fitness = 0;
        distance = 0;
        //loadDistance = 0;
    }

    public Location getLocation(int position){
        return (Location) route.get(position);
    }

    //generate new chromosome
    public void generateIndividual() {
        //for each gene in chromosome
        for (int locationIndex = 0; locationIndex < RouteManager.numberOfLocations(); locationIndex++){
            //if start depot, choose randomly from list of depots
            if(locationIndex == 0) {
                int firstDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                Location firstDepot = RouteManager.getDepot(firstDepotPos);
                setLocation(locationIndex, firstDepot);
                locationList.add(firstDepot);
            }
            //if end depot, choose randomly from list of depots
            else if (locationIndex == (RouteManager.numberOfLocations()-1)) {
                int lastDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                Location lastDepot = RouteManager.getDepot(lastDepotPos);
                setLocation(locationIndex, lastDepot);
                locationList.add(lastDepot);
            }
            //for all other genes, add from list of required locations, in order
            else {
                setLocation(locationIndex, RouteManager.getLocation(locationIndex));
                locationList.add(RouteManager.getLocation(locationIndex));
            }
        }
        //randomise gene sequence except start and end depot
        Collections.shuffle(route.subList(1,(RouteManager.numberOfLocations()-2)));
        //add charging points where necessary
        this.includeRecharge();
    }

    public double getDistance(){
        //calculate total distance for route
        if (distance == 0){
            double totalDistance = 0;
            for (int locationIndex = 0; locationIndex < routeSize()-1; locationIndex++){
                Location startLocation = getLocation(locationIndex);
                Location destinationLocation = getLocation(locationIndex+1);
                totalDistance = totalDistance + startLocation.distanceTo(destinationLocation);
            }
            distance = totalDistance;
        }
        return distance;
    }



    public double getLoadDist() {
        //calculate total of distance travelled multiplied by load carried
        if(loadDistance == 0) {
            double totalLoadDistance = 0;
            for (int locationIndex = 0; locationIndex < routeSize()-1; locationIndex++){
                Location startLocation = getLocation(locationIndex);
                Location destinationLocation = getLocation(locationIndex+1);
                totalLoadDistance = totalLoadDistance + startLocation.loadDistanceTo(startLocation.distanceTo(destinationLocation), route);
            }
            loadDistance = totalLoadDistance;
        }
        return loadDistance;

    }

    //fitness function to assess each chromosome
    public double getFitness() {
        if (fitness == 0) {
            boolean valid = true;
            fitness = ((100/getDistance()) + (300/(getLoadDist()/1000)));
            if(!getLocation(0).isDepot() || !getLocation(routeSize()-1).isDepot()) {
                valid = false;
            }
            for(int i=1; i < RouteManager.destinationsListSize()-1; i++) {
                if(!containsLocation(RouteManager.getLocation(i))){
                    valid = false;
                }
            }
            if(valid){
                fitness = fitness * 3;
            }
        }
        return fitness;
    }

    public boolean containsLocation(Location location){
        return route.contains(location);
    }

    public void addLocation(int position, Location location) {
        route.add(position, location);
        fitness = 0;
        distance = 0;
        //loadDistance = 0;
    }
    //removing genes from chromosome, crucially, evaluation functions are not reset so chromosome maintains fitness value prior to gene removal
    public void removeLocationByIndex(int i) {
        route.remove(i);
    }

    //add charging points to route where necessary
    public void includeRecharge(){
        //keep track of distance travelled since charge and range
        double currentTotalDistance = 0;
        int r = vehicle.getRange();
        double range = r;
        //if vehicle has range, i.e. if vehicle is electric, else nothing is done
        if(range > 0) {
            //for each location visited on route
            for(int i = 0; i < routeSize()-1; i++){
                Location startLocation = getLocation(i);
                Location destinationLocation = getLocation(i+1);
                //if vehicle cannot make it to next location without charging, add nearest charging point as next location on route
                if(currentTotalDistance + startLocation.distanceTo(destinationLocation) > range)  {
                    //efficiency could be added finding destinations on way to charging point without going over range
                    addLocation(i + 1, RouteManager.nearestChargingPoint(startLocation));
                    //reset distance since recharge
                    currentTotalDistance = 0;
                    i = i +1;
                }
                //if vehicle can make it to next location on route, but cannot make it to charging station nearest next location
                else if((currentTotalDistance + startLocation.distanceTo(destinationLocation) + destinationLocation.distanceTo(RouteManager.nearestChargingPoint(destinationLocation))) > range) {
                    //if vehicle cannot make it to charging station nearest to next location, add nearest charging point to current location as next location on route
                    if(currentTotalDistance + startLocation.distanceTo(RouteManager.nearestChargingPoint(destinationLocation)) > range){
                        addLocation(i + 1, RouteManager.nearestChargingPoint(startLocation));
                        //reset distance since recharge
                        currentTotalDistance = 0;
                        i = i +1;
                    }
                    //if vehicle can make it to destinations nearest charging point, add that as next location before original destination
                    else {
                        addLocation(i + 1, RouteManager.nearestChargingPoint(destinationLocation));
                        //reset distance since recharge
                        currentTotalDistance = 0;
                        i = i +1;
                    }
                }
                //if none of these apply, move on to next location and add distance travelled to total
                else if(currentTotalDistance + startLocation.distanceTo(destinationLocation) < range) {
                    currentTotalDistance = currentTotalDistance + startLocation.distanceTo(destinationLocation);
                }
            }
        }
    }

    //remove all charging points that were added to route
    public void removeRecharge() {
        int range = 0;
        range = vehicle.getRange();
        //if vehicle is electric
        if(range > 0) {
            for(int i = 1; i < routeSize()-1; i++){
                //remove all instances of depots or charging points that are not start or end depot
                if(getLocation(i) instanceof ChargingPoint || getLocation(i) instanceof Depot) {
                    removeLocationByIndex(i);
                }
            }
        }
    }

    @Override
    public String toString() {
        String genes = "|";
        for (int i = 0; i< routeSize(); i++) {
            genes = genes + getLocation(i) + "|";
        }
        return genes;
    }

}
