package tech.amcg.gvrp;

public class Population {

    private final Route[] population;

    public int size(){
        return population.length;
    }

    public void saveRoute(int index, Route route){
        population[index] = route;
    }

    public Population(int populationSize, boolean initialise) {
        //create new population from parameters
        population = new Route[populationSize];
        BinarySearch binarySearch = new BinarySearch();
        GreedyAlgorithm greedy = new GreedyAlgorithm();
        if (initialise) {
            //if initialising new population, use greedy algorithm for first 2 individuals
            for (int i = 0; i < size(); i++) {
                Route newRoute = new Route();
                Vehicle vehicle = FleetManager.getVehicle(0);
                if(i == 0) {
                    newRoute = greedy.generateDistance();
                    saveRoute(i, newRoute);
                    newRoute.setVehicle(vehicle);
                    //add charging points where necessary
                    newRoute.includeRecharge();
                }
                else if (i == 1) {
                    newRoute = greedy.generateWeight();
                    saveRoute(i, newRoute);
                    newRoute.setVehicle(vehicle);
                    //add charging points where necessary
                    newRoute.includeRecharge();
                }
                //initialise rest of new population randomly but ensure no duplicates
                else {
                    newRoute.setVehicle(vehicle);
                    newRoute.generateIndividual();
                    this.sort(i + 1);
                    if (binarySearch.search(population, 0, i, newRoute)) {
                        i = i - 1;
                    } else {
                        saveRoute(i, newRoute);
                    }
                }
            }
        }
    }

    public Route getRoute(int index){
        return population[index];
    }

    public Route getFittest(){
        //find and return fittest individual in the population
        Route fittest = population[0];
        for (int i = 1; i < size(); i++){
            if (fittest.getFitness() <= getRoute(i).getFitness()) {
                fittest = getRoute(i);
            }
        }
        return fittest;
    }

    public Route[] getArray() {
        return this.population;
    }

    public void sort(int arrayLength) {
        //sort population with mergesort
        MergeSort mergeSort = new MergeSort();
        mergeSort.sort(population, arrayLength);
    }
}
