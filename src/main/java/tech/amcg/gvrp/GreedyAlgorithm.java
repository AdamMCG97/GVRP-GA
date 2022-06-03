package tech.amcg.gvrp;

public class GreedyAlgorithm {
    public Route generateDistance() {
        //greedy algorithm with regard to smallest distance
        Route result = new Route();
        Location potentialNextLoc, bestLoc;
        //for each gene in an individual
        for (int locationIndex = 0; locationIndex < RouteManager.numberOfLocations(); locationIndex++) {
            bestLoc = RouteManager.getLocation(locationIndex);
            double distance;
            double bestDistance;
            //if selecting start depot, choose randomly from list of depots
            if (locationIndex == 0) {
                int firstDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                Location firstDepot = RouteManager.getDepot(firstDepotPos);
                result.setLocation(locationIndex, firstDepot);
            } else if (locationIndex == (RouteManager.numberOfLocations() - 1)) {
                //if selecting end depot, choose depot with least distance from last destination
                bestDistance = Double.MAX_VALUE;
                for (int i = 0; i < RouteManager.depotListSize(); i++) {
                    potentialNextLoc = RouteManager.getDepot(i);
                    distance = result.getLocation(locationIndex - 1).distanceTo(potentialNextLoc);
                    if (distance < bestDistance) {
                        bestDistance = distance;
                        bestLoc = potentialNextLoc;
                    }
                }
                result.setLocation(locationIndex, bestLoc);
            } else {
                //when selecting each required location, find shortest distance from current point that hasnt already been visited
                bestDistance = Double.MAX_VALUE;
                for (int i = 0; i < RouteManager.numberOfLocations(); i++) {
                    potentialNextLoc = RouteManager.getLocation(i);
                    int j = 0;
                    while (result.containsLocation(potentialNextLoc) || potentialNextLoc.isDepot()) {
                        potentialNextLoc = RouteManager.getLocation(j);
                        j = j + 1;
                    }
                    if (!result.containsLocation(potentialNextLoc) && !potentialNextLoc.isDepot()) {
                        distance = result.getLocation(locationIndex - 1).distanceTo(potentialNextLoc);
                        if (distance < bestDistance) {
                            bestDistance = distance;
                            bestLoc = potentialNextLoc;
                        }
                    }
                }
                result.setLocation(locationIndex, bestLoc);
            }
        }
        return result;
    }

    public Route generateWeight() {
        //greedy algorithm with regard to biggest weight to reduce loadXdistance
        Route result = new Route();
        Location potentialNextLoc, bestLoc;
        for (int locationIndex = 0; locationIndex < RouteManager.numberOfLocations(); locationIndex++) {
            bestLoc = RouteManager.getLocation(locationIndex);
            int weight;
            //if selecting start depot, choose randomly from list of depots
            if (locationIndex == 0) {
                int firstDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                Location firstDepot = RouteManager.getDepot(firstDepotPos);
                result.setLocation(locationIndex, firstDepot);
            } else if (locationIndex == (RouteManager.numberOfLocations() - 1)) {
                //if selecting end depot, choose randomly from list of depots
                int lastDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                Location lastDepot = RouteManager.getDepot(lastDepotPos);
                result.setLocation(locationIndex, lastDepot);
            } else {
                //when selecting each required location, find largest weight from remaining required locations
                int bestWeight = 0;
                for (int i = 0; i < RouteManager.numberOfLocations(); i++) {
                    potentialNextLoc = RouteManager.getLocation(i);
                    int j = 0;
                    while (result.containsLocation(potentialNextLoc) || potentialNextLoc.isDepot()) {
                        potentialNextLoc = RouteManager.getLocation(j);
                        j = j + 1;
                    }
                    if (!result.containsLocation(potentialNextLoc) && !potentialNextLoc.isDepot()) {
                        weight = potentialNextLoc.getLoad();
                        if (weight > bestWeight) {
                            bestWeight = weight;
                            bestLoc = potentialNextLoc;
                        }
                    }
                }
                result.setLocation(locationIndex, bestLoc);
            }
        }
        return result;
    }
}
