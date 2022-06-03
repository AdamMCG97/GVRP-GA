package tech.amcg.gvrp;

public class runGA {

    public static void main(String[] args) {
        int maxSuccess = 50;
        int maxFailure = 500;
        int successCount = 0;
        int failureCount = 0;
        int generationCount = 0;
        double currentFitness = 0;
        Route currentFittest, fittestStart;

        new ChargingPoint("CP1",42, 160);
        new ChargingPoint("CP2",12, 20);
        new ChargingPoint("CP3",120, 200);
        new ChargingPoint("CP4",50, 50);
        new ChargingPoint("CP5",40, 60);
        new ChargingPoint("CP6",140, 110);
        new ChargingPoint("CP7",110, 40);
        new ChargingPoint("CP8",60, 160);
        new ChargingPoint("CP9",190, 10);
        new ChargingPoint("CP10",180, 150);
        Depot city13 = new Depot("Depot Placeholder 1",120, 80);
        RouteManager.addLocation(city13);
        new Destination("London",100, 100,  400);
        new Destination("Bournemouth",120, 110,  40);
        new Destination("Christchurch",80, 180, 5);
        new Destination("Winchester",140, 180, 25);
        new Destination("Southampton",20, 160, 20);
        new Destination("Woking",100, 160, 200);
        new Destination("Basingstoke",200, 160, 10);
        new Destination("Guilford",140, 140, 5);
        new Destination("Portsmouth",40, 120, 1);
        new Destination("Exeter",100, 120, 12);
        new Destination("Bristol",180, 100,6);
        new Destination("Twickenham",60, 80, 8);
        new Destination("Essex",180, 60, 20);
        new Destination("Brighton",20, 40, 70);
        new Destination("Worthing",100, 40, 20);
        new Destination("Loughborough",200, 40, 10);
        new Destination("Nottingham",20, 20, 15);
        new Destination("Leicester",150, 150, 22);
        new Destination("Bath",16, 20, 9);
        Depot city21 = new Depot("Depot Placeholder 2",130, 70);
        RouteManager.addLocation(city21);
        new Depot("Bicester Depot", 15, 70);
        new Depot("Richmond Depot",135, 70 );
        new Depot("Westminster Depot",140, 90 );
        new ElectricVehicle(500,2500);
        int popSize = (int) (RouteManager.destinationsListSize() * 25) /10;
        Population population = new Population(popSize, true);
        currentFittest = population.getFittest();
        fittestStart = currentFittest;
        currentFitness = population.getFittest().getFitness();
        while(successCount < maxSuccess && failureCount < maxFailure) {
            population.sort(population.populationSize());
            population = Algorithm.evolvePop(population);
            generationCount = generationCount + 1;
            if(population.getFittest().getFitness() > currentFitness) {
                currentFitness = population.getFittest().getFitness();
                successCount = successCount + 1;
                failureCount = 0;
            }
            else if(population.getFittest().getFitness() <= currentFitness) {
                failureCount = failureCount + 1;
            }
        }
        population.sort(population.populationSize());
        System.out.println("Finished. ");
        System.out.println(population.getFittest());
        System.out.println("Start Distance: " + fittestStart.getDistance() + " Final Distance: " + population.getFittest().getDistance() + ". Calculated in " + generationCount + " generations.");
        System.out.println("Start LoadxDistance: " + fittestStart.getLoadDist() + " Final LoadxDistance: " + population.getFittest().getLoadDist());
        System.out.println("Improvements: " + successCount + " Start Fitness: "+ fittestStart.getFitness() +" End Fitness: " + population.getFittest().getFitness());
    }
}
