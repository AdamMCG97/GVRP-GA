package gvrp;

public class Algorithm {
    /*
    Main Java class in which all evolutionary techniques are applied to a population
     */
    //algorithm parameters
    private static final double mutationRate = 0.02;
    private static final int tournamentSize = 5;
    private static final boolean elitism = true;

    public static Population evolvePop(Population population){
        //create new empty population of the same size to save next generations individuals in
        Population newPop = new Population(population.populationSize(), false);
        BinarySearch bS = new BinarySearch();
        int elitistmOffset = 0;
        //define amount of top individuals to be saved for next generation without evolving
        int elistismAmount =  (population.populationSize()/10);
        Vehicle vehicle = FleetManager.getVehicle(0);
        //remove charging points from all individuals in population as they'll become redundant after crossover
        for(int i = 0; i < population.populationSize(); i++) {
            population.getRoute(i).removeRecharge();
        }
            //for the remaining empty positions in the new population
        for (int i = elitistmOffset; i < newPop.populationSize(); i++) {
            //select parents by tournament selection
            Route parent1 = tournamentSelection(population);
            Route parent2 = tournamentSelection(population);
            //ensure parents are unique
            while(parent2 == parent1){
                parent2 = tournamentSelection(population);
            }
            //create offspring via OX
            Route child = crossover(parent1, parent2);
            //set vehicle for all new routes
            child.setVehicle(vehicle);
            //if new individual is unique in new population so far, add to population, else discard individual and breed again
            newPop.sort(i+1);
            if(bS.bSearch(newPop.getArray(), 0, i, child)) {
                i = i - 1;
            }
            else {
                newPop.saveRoute(i, child);
            }
            newPop.sort(i+1);
        }
        //mutate all individuals in new population
        for (int i = elitistmOffset; i < newPop.populationSize(); i++) {
            mutate(newPop.getRoute(i));
        }
        //if elitism is implemented | it always should be
        if (elitism) {
            //save top individuals to next generation before evolving
            population.sort(population.populationSize());
            newPop.saveRoute(0, population.getFittest());
            //System.out.println("test");
            for(int i = 1; i < elistismAmount; i++) {
                newPop.saveRoute(i, population.getRoute(population.populationSize()-i));
            }
            //offset pointer for new population correctly
            elitistmOffset = elistismAmount;
        }

        //population finished evolving, reapply charging stations where necessary to new population
        for(int i = 0; i < population.populationSize(); i++) {
            newPop.getRoute(i).includeRecharge();
        }
        //return evolved population
        return newPop;
    }

    private static void mutate(Route route){
        //for every gene in an individual
        for (int routePos1 = 0; routePos1< route.routeSize(); routePos1++){
            //control mutation at specified rate
            if(Math.random()<mutationRate){
                int routePos2;
                //if gene is a start/end depot, mutate by swapping in list of possible depots
                if(routePos1 == 0) {
                    int firstDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                    Location firstDepot = RouteManager.getDepot(firstDepotPos);
                    route.setLocation(routePos1, firstDepot);
                } else if(routePos1 == (route.routeSize()-1)) {
                    int lastDepotPos = (int) (RouteManager.depotListSize() * Math.random());
                    Location lastDepot = RouteManager.getDepot(lastDepotPos);
                    route.setLocation(routePos1, lastDepot);
                } else {
                    //else swap selected gene with other random gene that is not start/end depot or itself
                    routePos2 = (int) (route.routeSize() * Math.random());
                    while(routePos2 == routePos1 || routePos2 == 0 || routePos2 == (route.routeSize() - 1)) {
                        routePos2 = (int) (route.routeSize() * Math.random());
                    }
                    Location location1 = route.getLocation(routePos1);
                    Location location2 = route.getLocation(routePos2);
                    route.setLocation(routePos2, location1);
                    route.setLocation(routePos1, location2);
                }
            }
        }
    }

    private static Route tournamentSelection(Population pop){
        //initialise empty tournament population
        Population tournament = new Population(tournamentSize, false);
        //fill tournament population with random individuals
        for (int i=0; i<tournamentSize; i++) {
            int index = (int) (Math.random() * pop.populationSize());
            tournament.saveRoute(i, pop.getRoute(index));
        }
        //find fittest in tournament
        return tournament.getFittest();
    }

    public static Route crossover(Route parent1, Route parent2) {
        //create null offspring
        Route child = new Route();
        //randomly select crossover points
        int num1 = (int) (Math.random() * parent1.routeSize());
        int num2 = (int) (Math.random() * parent1.routeSize());
        //ensure crossover points are not the same or too low
        while(num1 == num2 || num1+num2<3) {
            num2 = (int) (Math.random() * parent1.routeSize());
        }
        int startPosition = Math.min(num1, num2);
        int endPosition = Math.max(num1, num2);
        //ensure crossover points do not include start/end depot
        if(startPosition == 0) {
            startPosition = 1;
        }
        if(endPosition == RouteManager.numberOfLocations()-1) {
            endPosition = (RouteManager.numberOfLocations()-2);
        }
        if(endPosition == 0) {
            endPosition = 2;
        }
        //assign parent 2's start and end depot to child
        child.setLocation(0, parent2.getLocation(0));
        child.setLocation(parent2.routeSize()-1, parent2.getLocation(parent2.routeSize()-1));

        //splice crossover section from parent 1 into same section in child
        for(int i = 0; i<child.routeSize(); i++) {
            if(i > startPosition && i < endPosition) {
                child.setLocation(i, parent1.getLocation(i));
            }
        }
        //fill gaps in correct OX order from parent 2
        int currentPosition = endPosition;
        int checkPosition = endPosition;
        while(!child.fullRoute()) {
            if(!child.containsLocation(parent2.getLocation(checkPosition))){
                child.setLocation(currentPosition, parent2.getLocation(checkPosition));
                if(currentPosition + 1 == parent2.routeSize()-1) {
                    currentPosition = 1;
                }
                else{
                    currentPosition = currentPosition + 1;
                }
                if(checkPosition + 1 == parent2.routeSize()-1) {
                    checkPosition = 1;
                }
                else{
                    checkPosition = checkPosition + 1;
                }
            }
            else{
                while(child.containsLocation(parent2.getLocation(checkPosition))) {
                    if(checkPosition + 1 == parent2.routeSize()-1) {
                        checkPosition = 1;
                    }
                    else{
                        checkPosition = checkPosition + 1;
                    }
                }
                child.setLocation(currentPosition, parent2.getLocation(checkPosition));
                if(currentPosition + 1 == parent2.routeSize()-1) {
                    currentPosition = 1;
                }
                else{
                    currentPosition = currentPosition + 1;
                }
                if(checkPosition + 1 == parent2.routeSize()-1) {
                    checkPosition = 1;
                }
                else{
                    checkPosition = checkPosition + 1;
                }
            }
        }
        return child;
    }


}
